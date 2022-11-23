package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.InactiveSellerBatchDto;
import com.grupo6.projetointegrador.dto.UpdateSellerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.SellerFactory;
import com.grupo6.projetointegrador.factory.ZipCodeDtoFactory;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.repository.SellerRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SellerServiceImplTest {

    @Mock
    private SellerRepo sellerRepo;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WarehouseRepo warehouseRepo;

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private SellerServiceImpl sellerService;

    @Test
    void createSeller_returnSeller_whenAllDataValid() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller newSeller = SellerFactory.build(createSellerDto);
        ZipCodeDto zipCodeDto = ZipCodeDtoFactory.build(createSellerDto, null);
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(zipCodeDto, HttpStatus.OK));
        Mockito.when(sellerRepo.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sellerRepo.save(ArgumentMatchers.any())).thenReturn(newSeller);
        Seller result = sellerService.createSeller(createSellerDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getZipCode()).isEqualTo(createSellerDto.getZipCode());
        assertThat(result.getAddress()).isEqualTo(createSellerDto.getAddress());
        assertThat(result.getEmail()).isEqualTo(createSellerDto.getEmail());
        assertThat(result.getFirstName()).isEqualTo(createSellerDto.getFirstName());
        assertThat(result.getLastName()).isEqualTo(createSellerDto.getLastName());
        assertThat(result.getHouseNumber()).isEqualTo(createSellerDto.getHouseNumber());
    }

    @Test
    void createSeller_returnBusinessRuleException_whenZipCodeAddressNotMatch() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "22793420"
        );

        // When
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(new ZipCodeDto(
                        createSellerDto.getZipCode(),
                        "Rua Teste",
                        null,
                        null,
                        null,
                        null,
                        null
                        ), HttpStatus.OK));

        // Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createSeller_returnBusinessRuleException_whenZipCodeInvalid() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "00000000"
        );

        // When
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(new ZipCodeDto(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        "Erro"
                ), HttpStatus.OK));

        // Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createSeller_returnBusinessRuleException_whenEmailAlreadyExists() {
        // Given
        CreateSellerDto createSellerDto = new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "26379030"
        );
        List<Seller> sellers = new ArrayList<>();
        sellers.add(SellerFactory.build(setupCreateSellerDto()));

        // When
        ZipCodeDto zipCodeDto = ZipCodeDtoFactory.build(createSellerDto, null);
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(zipCodeDto, HttpStatus.OK));
        Mockito.when(sellerRepo.findAll()).thenReturn(sellers);

        // Then
        assertThatThrownBy(() -> sellerService.createSeller(createSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void findSeller_returnSeller_whenSellerExists() {
        // Given
        Seller seller = SellerFactory.build(setupCreateSellerDto());

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Seller result = sellerService.findSeller(ArgumentMatchers.anyLong());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(seller.getId());
    }

    @Test
    void findSeller_returnNotFoundException_whenSellerDoesntExist() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.findSeller(ArgumentMatchers.anyLong())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateSeller_returnSeller_whenAllDataValid() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        UpdateSellerDto updateSellerDto = new UpdateSellerDto(
                "Ciclano",
                "Teste",
                "ciclano.dtal@teste.com",
                "Rua H",
                890,
                "69902772"
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Seller updatedSeller = new Seller(
                seller.getId(),
                updateSellerDto.getFirstName(),
                seller.getLastName(),
                updateSellerDto.getEmail(),
                seller.getAddress(),
                seller.getHouseNumber(),
                seller.getZipCode(),
                seller.getActive(),
                seller.getProducts()
        );
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        ZipCodeDto zipCodeDto = ZipCodeDtoFactory.build(null, updateSellerDto);
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(zipCodeDto, HttpStatus.OK));
        Mockito.when(sellerRepo.findAll()).thenReturn(new ArrayList<>());
        Mockito.when(sellerRepo.save(ArgumentMatchers.any())).thenReturn(updatedSeller);
        Seller result = sellerService.updateSeller(seller.getId(), updateSellerDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(seller.getId());
        assertThat(result.getZipCode()).isEqualTo(createSellerDto.getZipCode());
        assertThat(result.getAddress()).isEqualTo(createSellerDto.getAddress());
        assertThat(result.getEmail()).isEqualTo(updatedSeller.getEmail());
        assertThat(result.getFirstName()).isEqualTo(updatedSeller.getFirstName());
        assertThat(result.getLastName()).isEqualTo(createSellerDto.getLastName());
        assertThat(result.getHouseNumber()).isEqualTo(createSellerDto.getHouseNumber());
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerIsInactive() {
        // Given

        // When
        Seller seller = SellerFactory.build(setupCreateSellerDto());
        seller.setActive(Active.INATIVO);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), new UpdateSellerDto()))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnNotFoundException_whenSellerDoesntExists() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(1L, new UpdateSellerDto()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerZipCodeAddressDoesntMatch() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        UpdateSellerDto updateSellerDto = new UpdateSellerDto(
                null,
                null,
                null,
                "Rua Teste",
                null,
                "86030190"
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(new ZipCodeDto(
                        updateSellerDto.getZipCode(),
                        "Avenida Teste",
                        null,
                        null,
                        null,
                        null,
                        null
                ), HttpStatus.OK));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerZipCodeDoesntMatchDBAddress() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        UpdateSellerDto updateSellerDto = new UpdateSellerDto(
                null,
                null,
                null,
                null,
                null,
                "86030190"
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(new ZipCodeDto(
                        updateSellerDto.getZipCode(),
                        "Avenida Teste",
                        null,
                        null,
                        null,
                        null,
                        null
                ), HttpStatus.OK));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerAddressDoesntMatchDBZipCode() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        UpdateSellerDto updateSellerDto = new UpdateSellerDto(
                null,
                null,
                null,
                "Rua Teste",
                null,
                null
        );

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(restTemplate.getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(new ResponseEntity<>(new ZipCodeDto(
                        createSellerDto.getZipCode(),
                        createSellerDto.getAddress(),
                        null,
                        null,
                        null,
                        null,
                        null
                ), HttpStatus.OK));

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateSeller_returnBusinessRuleException_whenSellerEmailAlreadyExists() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();
        UpdateSellerDto updateSellerDto = new UpdateSellerDto(
                null,
                null,
                "teste@teste.com",
                null,
                null,
                null
        );
        Seller seller = SellerFactory.build(createSellerDto);
        Seller seller2 = new Seller(
                2L,
                "Beltrana",
                "de Tal",
                "teste@teste.com",
                "Rua Jose Penha Tavares",
                321,
                "68909791",
                Active.ATIVO,
                new ArrayList<>()
        );
        List<Seller> sellers = new ArrayList<>();
        sellers.add(seller);
        sellers.add(seller2);

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        Mockito.when(sellerRepo.findAll()).thenReturn(sellers);

        // Then
        assertThatThrownBy(() -> sellerService.updateSeller(seller.getId(), updateSellerDto))
                .isInstanceOf(BusinessRuleException.class);
        verify(restTemplate, never()).getForEntity(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    void deleteSeller_inactivatesSeller_whenSellerExistsAndActive() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller seller = SellerFactory.build(createSellerDto);
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));
        sellerService.deleteSeller(seller.getId());
        Seller inactiveSeller = sellerService.findSeller(seller.getId());

        // Then
        assertThat(inactiveSeller).isNotNull();
        assertThat(inactiveSeller.getActive()).isEqualTo(Active.INATIVO);
    }

    @Test
    void deleteSeller_throwsNotFoundException_whenSellerDoesntExists() {
        // Given

        // When
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.deleteSeller(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteSeller_throwsBusinessRuleException_whenSellerAlreadyInactive() {
        // Given
        CreateSellerDto createSellerDto = setupCreateSellerDto();

        // When
        Seller seller = new Seller(
                1L,
                createSellerDto.getFirstName(),
                createSellerDto.getLastName(),
                createSellerDto.getEmail(),
                createSellerDto.getAddress(),
                createSellerDto.getHouseNumber(),
                createSellerDto.getZipCode(),
                Active.INATIVO,
                new ArrayList<>()
        );
        Mockito.when(sellerRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seller));

        // Then
        assertThatThrownBy(() -> sellerService.deleteSeller(seller.getId()))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void getInactiveSellerBatches_returnListOfInactiveSellerBatch_whenWarehouseExistsAndInactiveSellerInWarehouse() {
        // Given
        InactiveSellerBatchDto inactiveSellerBatchDto = genericInactiveSellerBatchDto();
        List<InactiveSellerBatchDto> inactiveSellerBatches = new ArrayList<>();
        inactiveSellerBatches.add(inactiveSellerBatchDto);

        Warehouse warehouse = new Warehouse();

        // When
        Mockito.when(warehouseRepo.findById(warehouse.getId())).thenReturn(Optional.of(warehouse));
        Mockito.when(productRepo.findBatchesInWarehouseFromInactiveSellers(warehouse.getId()))
                .thenReturn(inactiveSellerBatches);
        List<InactiveSellerBatchDto> result = sellerService.getInactiveSellerBatches(warehouse.getId());

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isInstanceOf(InactiveSellerBatchDto.class);
        assertThat(result.get(0).getSellerId()).isEqualTo(inactiveSellerBatchDto.getSellerId());
        assertThat(result.get(0).getQuantity()).isEqualTo(inactiveSellerBatchDto.getQuantity());
        assertThat(result.get(0).getProductId()).isEqualTo(inactiveSellerBatchDto.getProductId());
        assertThat(result.get(0).getSectionId()).isEqualTo(inactiveSellerBatchDto.getSectionId());
        assertThat(result.get(0).getCategory()).isEqualTo(inactiveSellerBatchDto.getCategory());
        assertThat(result.get(0).getActive()).isEqualTo(inactiveSellerBatchDto.getActive());
    }

    @Test
    void getInactiveSellerBatches_throwsNotFoundException_whenWarehouseDoesntExist() {
        // Given

        // When
        Mockito.when(warehouseRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> sellerService.getInactiveSellerBatches(ArgumentMatchers.anyLong()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getInactiveSellerBatches_throwsNotFoundException_whenNoInactiveSellerBatchesFound() {
        // Given
        Warehouse warehouse = new Warehouse();

        // When
        Mockito.when(warehouseRepo.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(warehouse));
        Mockito.when(productRepo.findBatchesInWarehouseFromInactiveSellers(ArgumentMatchers.anyLong()))
                .thenReturn(new ArrayList<>());

        // Then
        assertThatThrownBy(() -> sellerService.getInactiveSellerBatches(ArgumentMatchers.anyLong()))
                .isInstanceOf(NotFoundException.class);
    }

    private CreateSellerDto setupCreateSellerDto() {
        return new CreateSellerDto(
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua José Miguel Titonel",
                123,
                "26379030"
        );
    }

    private InactiveSellerBatchDto genericInactiveSellerBatchDto() {
        return new InactiveSellerBatchDto() {
            @Override
            public Long getSellerId() { return 1L; }

            @Override
            public Active getActive() { return Active.INATIVO; }

            @Override
            public Long getProductId() { return 1L; }

            @Override
            public Integer getQuantity() { return 5; }

            @Override
            public Long getSectionId() { return 1L; }

            @Override
            public Category getCategory() { return Category.FRESCO; }
        };
    }
}
