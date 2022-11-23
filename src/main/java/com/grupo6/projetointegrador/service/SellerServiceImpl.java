package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.InactiveSellerBatchDto;
import com.grupo6.projetointegrador.dto.UpdateSellerDto;
import com.grupo6.projetointegrador.dto.ZipCodeDto;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.ProductRepo;
import com.grupo6.projetointegrador.repository.SellerRepo;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerRepo sellerRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * This method returns a new Seller.
     * Or throws a {@link BusinessRuleException} if the zip code doesn't match the address given.
     * Or throws a {@link BusinessRuleException} if the email provided already exists in the database.
     *
     * @param createSellerDto This is the entry object of seller information,
     *                        containing first name, last name, email, address, house number and zip code.
     * @return A Seller object containing the information received.
     */
    @Override
    @Transactional
    public Seller createSeller(CreateSellerDto createSellerDto) {
        checkZipCode(createSellerDto.getZipCode(), createSellerDto.getAddress());
        checkEmail(createSellerDto.getEmail(), null);

        Seller newSeller = new Seller();
        List<Product> products = new ArrayList<>();

        newSeller.setProducts(products);
        newSeller.setActive(Active.ATIVO);
        newSeller.setAddress(createSellerDto.getAddress());
        newSeller.setZipCode(createSellerDto.getZipCode());
        newSeller.setEmail(createSellerDto.getEmail());
        newSeller.setHouseNumber(createSellerDto.getHouseNumber());
        newSeller.setFirstName(createSellerDto.getFirstName());
        newSeller.setLastName(createSellerDto.getLastName());

        return sellerRepo.save(newSeller);
    }

    /**
     * This method returns a Seller.
     * Or throws a {@link NotFoundException} if the seller is not found.
     *
     * @param id This is the id of the searched seller.
     * @return A Seller object of the searched seller.
     */
    @Override
    public Seller findSeller(Long id) {
        return sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
    }

    /**
     * This method returns an updated Seller.
     * Or throws a {@link NotFoundException} if the seller is not found.
     * Or throws a {@link BusinessRuleException} if the seller is inactive.
     *
     * @param id This is the id of the seller set to be updated.
     * @param updateSellerDto This is the entry object of seller information,
     *                        it may contain first name, last name, email, address, house number and zip code.
     * @return A Seller object containing the updated information.
     */
    @Override
    @Transactional
    public Seller updateSeller(Long id, UpdateSellerDto updateSellerDto) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
        if (seller.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Usuário inativo.");
        }

        updateCheckZipAndAddress(updateSellerDto, seller);
        if (updateSellerDto.getEmail() != null) {
            checkEmail(updateSellerDto.getEmail(), seller.getId());
            seller.setEmail(updateSellerDto.getEmail());
        }

        if (updateSellerDto.getAddress() != null) {
            seller.setAddress(updateSellerDto.getAddress());
        }
        if (updateSellerDto.getZipCode() != null) {
            seller.setZipCode(updateSellerDto.getZipCode());
        }
        if (updateSellerDto.getHouseNumber() != null) {
            seller.setHouseNumber(updateSellerDto.getHouseNumber());
        }
        if (updateSellerDto.getFirstName() != null) {
            seller.setFirstName(updateSellerDto.getFirstName());
        }
        if (updateSellerDto.getLastName() != null) {
            seller.setLastName(updateSellerDto.getLastName());
        }

        return sellerRepo.save(seller);
    }

    /**
     * This method makes a Seller inactive.
     * Or throws a {@link NotFoundException} if the seller is not found.
     * Or throws a {@link BusinessRuleException} if the seller is already inactive.
     *
     * @param id This is the id of the seller set to be deleted.
     */
    @Override
    public void deleteSeller(Long id) {
        Seller seller = sellerRepo.findById(id).orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
        if (seller.getActive().equals(Active.INATIVO)) {
            throw new BusinessRuleException("Usuário já está inativo.");
        }
        seller.setActive(Active.INATIVO);
        sellerRepo.save(seller);
    }

    /**
     * This method gets a list of batches that belongs to an inactive seller, if the product quantity is above 0.
     * Or throws a {@link NotFoundException} if the warehouse doesn't exist.
     * Or throws a {@link NotFoundException} if there is no inactive seller batches for this warehouse.
     *
     * @param warehouseId This is the warehouse id.
     * @return List<InactiveSellerBatchDto> a list of InactiveSellerBatchDto containing the seller id,
     *                                      the active status of the seller, the product id, the product quantity,
     *                                      the section id and the section category.
     */
    @Override
    public List<InactiveSellerBatchDto> getInactiveSellerBatches(Long warehouseId) {
        warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
        List<InactiveSellerBatchDto> inactiveSellerBatches = productRepo
                .findBatchesInWarehouseFromInactiveSellers(warehouseId);
        if (inactiveSellerBatches.isEmpty()) {
            throw new NotFoundException("Nenhum lote de vendedor inativo encontrado neste armazém.");
        }
        return inactiveSellerBatches;
    }

    /**
     * This method checks if the zip code matches the address received.
     * Or throws a {@link BusinessRuleException} if the zip code isn't valid.
     * Or throws a {@link BusinessRuleException} if the zip code doesn't match the address provided.
     *
     * @param zipCode This is the seller's zip code.
     * @param address This is the seller's address.
     */
    private void checkZipCode(String zipCode, String address) {
        ZipCodeDto zipCodeDtoDto = restTemplate
                .getForEntity("https://viacep.com.br/ws/"+zipCode+"/json/", ZipCodeDto.class)
                .getBody();
        if (zipCodeDtoDto.getErro() != null) {
            throw new BusinessRuleException("CEP inválido.");
        }
        if (!zipCodeDtoDto.getLogradouro().equals(address)) {
            throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
        }
    }

    /**
     * This method checks if the email received already exists in the database, excluding the own seller entry.
     * Or throws a {@link BusinessRuleException} if the email provided already exists in the database
     *                                           and is not associated with the current seller.
     *
     * @param email This is the seller's email.
     * @param id This is the seller's id.
     */
    private void checkEmail(String email, Long id) {
        List<Seller> sellers = sellerRepo.findAll();
        sellers.forEach((seller) -> {
            if (seller.getEmail().matches(email) && !seller.getId().equals(id)) {
                throw new BusinessRuleException("Email já cadastrado.");
            }
        });
    }

    /**
     * This method checks if the zip code matches the address received, depending on the information provided.
     * * Or throws a {@link BusinessRuleException} if the zip code or address provided doesn't match each other
     *                                             or the information from the database.
     *
     * @param updateSellerDto This is the entry object of seller information,
     *                        containing first name, last name, email, address, house number and zip code.
     * @param seller Seller object containing all the seller's information.
     */
    private void updateCheckZipAndAddress(UpdateSellerDto updateSellerDto, Seller seller) {
        if(updateSellerDto.getZipCode() != null && updateSellerDto.getAddress() != null) {
            checkZipCode(updateSellerDto.getZipCode(), updateSellerDto.getAddress());
        } else if (updateSellerDto.getZipCode() != null) {
            ZipCodeDto zipCodeDtoDto = restTemplate
                    .getForEntity("https://viacep.com.br/ws/"+updateSellerDto.getZipCode()+"/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(seller.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        } else if (updateSellerDto.getAddress() != null) {
            ZipCodeDto zipCodeDtoDto = restTemplate
                    .getForEntity("https://viacep.com.br/ws/"+seller.getZipCode()+"/json/", ZipCodeDto.class)
                    .getBody();
            if (!zipCodeDtoDto.getLogradouro().equals(updateSellerDto.getAddress())) {
                throw new BusinessRuleException("CEP não corresponde ao endereço passado.");
            }
        }
    }
}
