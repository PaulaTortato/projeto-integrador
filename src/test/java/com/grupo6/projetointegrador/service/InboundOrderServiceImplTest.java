package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.CreateItemBatchDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.dto.UpdateItemBatchDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceImplTest {
    @Mock
    private InboundOrderRepo inboundOrderRepo;

    @Mock
    private WarehouseRepo warehouseRepo;

    @Mock
    private WarehouseOperatorRepo warehouseOperatorRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private SectionRepo sectionRepo;

    @InjectMocks
    private InboundOrderServiceImpl inboundOrderService;

    @Test
    void createInboundOrder_saveInboundOrder_AllProvidedDataIsValid() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();
        Seller seller = setupSellerContainingTwoProducts();

        // When
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        InboundOrder inboundOrder = InboundOrderFactory
                .build(section);
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.of(warehouse));
        Mockito.when(sectionRepo.findById(1L)).thenReturn(Optional.of(section));
        Mockito.when(warehouseOperatorRepo.findById(1L)).thenReturn(Optional.of(warehouse.getWarehouseOperator()));
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(inboundOrder.getItemBatches().get(0).getProduct()));
        Mockito.when(productRepo.findSellerByProductId(ArgumentMatchers.any())).thenReturn(Optional.of(seller));
        Mockito.when(inboundOrderRepo.save(ArgumentMatchers.any())).thenReturn(inboundOrder);
        List<ItemBatchDto> itemBatchDtos = inboundOrderService.createInboundOrder(createInboundOrderDto);

        // Then
        assertThat(itemBatchDtos).isNotEmpty();
    }

    @Test
    void createInboundOrder_throwException_whenWarehouseDoesNotExists() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();

        // When / Then
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(createInboundOrderDto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void createInboundOrder_throwException_whenOperatorIsNotFromWarehouse() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();
        createInboundOrderDto.setWarehouseOperatorId(2L);

        // When / Then
        WarehouseOperator warehouseOperator = new WarehouseOperator(2L, null);
        Warehouse warehouse = WarehouseFactory.build();

        Section section = warehouse.getSections().get(0);
        InboundOrder inboundOrder = InboundOrderFactory
                .build(section);
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.of(warehouse));
        Mockito.when(sectionRepo.findById(1L)).thenReturn(Optional.of(section));
        Mockito.when(warehouseOperatorRepo.findById(2L)).thenReturn(Optional.of(warehouseOperator));
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(inboundOrder.getItemBatches().get(0).getProduct()));

        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(createInboundOrderDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createInboundOrder_throwException_whenSectionIsNotFromWarehouse() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();
        createInboundOrderDto.setSectionId(2L);

        // When / Then
        Section section = new Section(2L, new Warehouse(2L, null, null), 20L, Category.FRESCO);
        Warehouse warehouse = WarehouseFactory.build();

        InboundOrder inboundOrder = InboundOrderFactory
                .build(section);
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.of(warehouse));
        Mockito.when(sectionRepo.findById(2L)).thenReturn(Optional.of(section));
        Mockito.when(warehouseOperatorRepo.findById(1L)).thenReturn(Optional.of(warehouse.getWarehouseOperator()));
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(inboundOrder.getItemBatches().get(0).getProduct()));

        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(createInboundOrderDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createInboundOrder_throwException_whenSectionVolumeIsNotAvailable() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();
        createInboundOrderDto.getItemBatches().get(0).setVolume(200000L);

        // When / Then
        Warehouse warehouse = WarehouseFactory.build();
        Section section = warehouse.getSections().get(0);
        InboundOrder inboundOrder = InboundOrderFactory
                .build(section);
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.of(warehouse));
        Mockito.when(sectionRepo.findById(1L)).thenReturn(Optional.of(section));
        Mockito.when(warehouseOperatorRepo.findById(1L)).thenReturn(Optional.of(warehouse.getWarehouseOperator()));
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(inboundOrder.getItemBatches().get(0).getProduct()));

        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(createInboundOrderDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void createInboundOrder_throwException_whenProductTypeIsDifferentFromSection() {
        // Given
        CreateInboundOrderDto createInboundOrderDto = setupCreateInboundOrderDto();
        createInboundOrderDto.setSectionId(2L);

        // When / Then
        Section section = new Section(2L, new Warehouse(2L, null, null), 20L, Category.CONGELADO);

        InboundOrder inboundOrder = InboundOrderFactory
                .build(section);
        Warehouse warehouse = WarehouseFactory.build();
        Mockito.when(warehouseRepo.findById(1L)).thenReturn(Optional.of(warehouse));
        Mockito.when(sectionRepo.findById(2L)).thenReturn(Optional.of(section));
        Mockito.when(warehouseOperatorRepo.findById(1L)).thenReturn(Optional.of(warehouse.getWarehouseOperator()));
        Mockito.when(productRepo.findById(1L)).thenReturn(Optional.of(inboundOrder.getItemBatches().get(0).getProduct()));

        assertThatThrownBy(() -> inboundOrderService.createInboundOrder(createInboundOrderDto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateItemBatch_changeItemAndCreateOne_whenInboundAndProductExists() {
        // Given
        List<Product> products = setupSellerContainingTwoProducts().getProducts();
        InboundOrder savedInboundOrder = setupGenericInboundOrder();
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );
        UpdateItemBatchDto createFeijaoItemBatch = new UpdateItemBatchDto(
                null,
                2L,
                5,
                LocalDate.of(2021, 8, 20),
                LocalDateTime.of(2021, 3, 20, 1, 30, 10),
                2L,
                LocalDate.of(2020, 11, 20),
                BigDecimal.valueOf(60)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedInboundOrder));
        Mockito.when(productRepo.findById(1L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(1L)).findFirst());
        Mockito.when(productRepo.findById(2L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(2L)).findFirst());

        // Then
        List<ItemBatchDto> result = inboundOrderService.updateItemBatch(
                1L,
                List.of(updateArrozItemBatch, createFeijaoItemBatch)
        );
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getItemBatchId()).isEqualTo(updateArrozItemBatch.getItemBatchId());
        assertThat(result.get(0).getPrice()).isEqualTo(updateArrozItemBatch.getPrice());
        assertThat(result.get(0).getProductId()).isEqualTo(updateArrozItemBatch.getProductId());
        assertThat(result.get(0).getVolume()).isEqualTo(updateArrozItemBatch.getVolume());
        assertThat(result.get(0).getDueDate()).isEqualTo(updateArrozItemBatch.getDueDate());
        assertThat(result.get(0).getManufacturingDate()).isEqualTo(updateArrozItemBatch.getManufacturingDate());

        assertThat(result.get(1).getPrice()).isEqualTo(createFeijaoItemBatch.getPrice());
        assertThat(result.get(1).getProductId()).isEqualTo(createFeijaoItemBatch.getProductId());
        assertThat(result.get(1).getVolume()).isEqualTo(createFeijaoItemBatch.getVolume());
        assertThat(result.get(1).getDueDate()).isEqualTo(createFeijaoItemBatch.getDueDate());
        assertThat(result.get(1).getManufacturingDate()).isEqualTo(createFeijaoItemBatch.getManufacturingDate());
    }

    @Test
    void updateItemBatch_throwsNotFoundException_whenInboundOrderDoesNotExists() {
        // Given
        UpdateItemBatchDto dto = new UpdateItemBatchDto();

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(dto))
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateItemBatch_throwsNotFoundException_whenProductDoesNotExists() {
        // Given
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(setupGenericInboundOrder()));

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(updateArrozItemBatch))
        ).isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateItemBatch_throwsBusinessException_whenSectionCategoryDifferFromProductsOne() {
        List<Product> products = setupSellerContainingTwoProducts().getProducts();
        products.get(0).setCategory(Category.REFRIGERADO);

        InboundOrder savedInboundOrder = setupGenericInboundOrder();
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                4L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedInboundOrder));
        Mockito.when(productRepo.findById(1L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(1L)).findFirst());

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(updateArrozItemBatch))
        ).isInstanceOf(BusinessRuleException.class);
    }

    @Test
    void updateItemBatch_throwsBusinessException_whenSectionVolumeIsNotAvailable() {
        List<Product> products = setupSellerContainingTwoProducts().getProducts();

        InboundOrder savedInboundOrder = setupGenericInboundOrder();
        UpdateItemBatchDto updateArrozItemBatch = new UpdateItemBatchDto(
                1L,
                1L,
                1,
                LocalDate.of(2021, 10, 20),
                LocalDateTime.of(2021, 10, 20, 1, 30, 10),
                400000L,
                LocalDate.of(2021, 11, 20),
                BigDecimal.valueOf(50)
        );

        // When
        Mockito.when(inboundOrderRepo.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(savedInboundOrder));
        Mockito.when(productRepo.findById(1L))
                .thenReturn(products.stream().filter(product -> product.getId().equals(1L)).findFirst());

        // Then
        assertThatThrownBy(
                () -> inboundOrderService.updateItemBatch(1L, List.of(updateArrozItemBatch))
        ).isInstanceOf(BusinessRuleException.class);
    }

    private InboundOrder setupGenericInboundOrder() {
        return InboundOrderFactory.build(WarehouseFactory.build().getSections().get(0));
    }

    private Seller setupSellerContainingTwoProducts() {
        Seller seller = new Seller(
                1L,
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua Canopus",
                123,
                "86070180",
                Active.ATIVO,
                null
        );

        Product arroz = new Product(1L, BigDecimal.valueOf(5), Category.FRESCO, seller);
        Product feijao = new Product(2L, BigDecimal.valueOf(8), Category.FRESCO, seller);

        seller.setProducts(List.of(arroz, feijao));
        return seller;
    }

    private CreateInboundOrderDto setupCreateInboundOrderDto() {
        CreateItemBatchDto createItemBatchDto = new CreateItemBatchDto(
                1L,
                10,
                LocalDate.now(),
                LocalDateTime.now(),
                20L,
                LocalDate.now(),
                BigDecimal.valueOf(40)
        );
        return new CreateInboundOrderDto(
                1L,
                1L,
                1L,
                List.of(createItemBatchDto)
        );
    }
}
