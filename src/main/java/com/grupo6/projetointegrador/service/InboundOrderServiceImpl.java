package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.*;
import com.grupo6.projetointegrador.exception.BusinessRuleException;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundOrderServiceImpl implements InboundOrderService {

    private final InboundOrderRepo inboundOrderRepo;
    private final WarehouseRepo warehouseRepo;

    private final WarehouseOperatorRepo warehouseOperatorRepo;

    private final SectionRepo sectionRepo;

    private final ProductRepo productRepo;

    public InboundOrderServiceImpl(
            InboundOrderRepo inboundOrderRepo,
            WarehouseRepo warehouseRepo,
            WarehouseOperatorRepo warehouseOperatorRepo,
            ProductRepo productRepo,
            SectionRepo sectionRepo
    ) {
        this.inboundOrderRepo = inboundOrderRepo;
        this.warehouseRepo = warehouseRepo;
        this.warehouseOperatorRepo = warehouseOperatorRepo;
        this.productRepo = productRepo;
        this.sectionRepo = sectionRepo;
    }

    /**
     * This method receives a DTO with a list of items and ids.
     * The main goal is to store those items on the provided section.<p>
     * Also, check the {@link #validateInboundOrderCreation(List, Warehouse, WarehouseOperator, Section, List)} method for validation details.<p>
     * @param createInboundOrderDto This is the object that will be sent by the frontend.
     * @return A List<ItemBatchDto> object with the stored items.
     */
    @Override
    @Transactional
    public List<ItemBatchDto> createInboundOrder(CreateInboundOrderDto createInboundOrderDto) {
        Warehouse warehouse = findWarehouseOrThrowNotFound(createInboundOrderDto.getWarehouseId());
        Section section = findSectionOrThrowNotFound(createInboundOrderDto.getSectionId());
        WarehouseOperator warehouseOperator = findWarehouseOperatorOrThrowNotFound(createInboundOrderDto.getWarehouseOperatorId());
        List<Product> products = createInboundOrderDto.getItemBatches().stream()
                .map((batchDto) -> findProductOrThrowNotFound(batchDto.getProductId()))
                .collect(Collectors.toList());

        validateInboundOrderCreation(
                createInboundOrderDto.getItemBatches(),
                warehouse,
                warehouseOperator,
                section,
                products
        );

        InboundOrder createdInboundOrder = new InboundOrder();
        List<ItemBatch> itemBatches = createInboundOrderDto.getItemBatches().stream().map((batchDto) -> {
            Product foundProduct = products.stream()
                    .filter(product -> product.getId().equals(batchDto.getProductId()))
                    .findFirst().get();
            return batchDto.toItemBatch(createdInboundOrder, foundProduct);
        }).collect(Collectors.toList());

        createdInboundOrder.setOrderDate(LocalDate.now());
        createdInboundOrder.setSection(section);
        createdInboundOrder.setWarehouse(warehouse);
        createdInboundOrder.setWarehouseOperator(warehouseOperator);
        createdInboundOrder.setItemBatches(itemBatches);

        InboundOrder savedInboundOrder = inboundOrderRepo.save(createdInboundOrder);

        return savedInboundOrder.getItemBatches().stream().map(ItemBatchDto::fromItemBatch).collect(Collectors.toList());
    }

    /**
     * Receives the InboundOrder Id and a list o ItemBatch.
     * It'll update the provided InboundOrder ItemBatches<p>
     * Also, check the {@link #validateInboundOrderUpdate(List, InboundOrder, List)} method for validation details.<p>
     * @param inboundOrderId This is the InboundOrder ID.
     * @param updateItemBatchDtos This is the object that will be sent by the frontend.
     * @return A List<ItemBatchDto> object with the stored items.
     */
    @Override
    @Transactional
    public List<ItemBatchDto> updateItemBatch(Long inboundOrderId, List<UpdateItemBatchDto> updateItemBatchDtos) {
        InboundOrder inboundOrder = inboundOrderRepo.findById(inboundOrderId)
                .orElseThrow(() -> new NotFoundException("Ordem de entrada não encontrado."));
        List<Product> products = updateItemBatchDtos.stream()
                .map(updateDto -> findProductOrThrowNotFound(updateDto.getProductId()))
                        .collect(Collectors.toList());
        validateInboundOrderUpdate(updateItemBatchDtos, inboundOrder, products);

        List<ItemBatch> updatedItemBatches = updateItemBatchDtos.stream()
                .map(itemBatchDto -> itemBatchDto.toItemBatch(
                        inboundOrder,
                        products.stream()
                                .filter(product -> itemBatchDto.getProductId().equals(product.getId()))
                                .findFirst().get()
                ))
                .collect(Collectors.toList());

        inboundOrder.setItemBatches(updatedItemBatches);
        inboundOrderRepo.save(inboundOrder);

        return updatedItemBatches.stream()
                .map(ItemBatchDto::fromItemBatch)
                .collect(Collectors.toList());
    }

    /**
     * Method to find a warehouse by id or throw a not found exception.
     *
     * @param warehouseId The ID of the warehouse.
     * @return A warehouse object or {@link NotFoundException} - if none found.<p>
     */
    private Warehouse findWarehouseOrThrowNotFound(Long warehouseId){
        return warehouseRepo.findById(warehouseId).orElseThrow(() -> new NotFoundException("Armazém não encontrado."));
    }

    /**
     * Method to find a warehouse operator by id or throw a not found exception.
     *
     * @param warehouseOperatorId The ID of the warehouse operator to be updated.
     * @return A WarehouseOperator object or {@link NotFoundException} - if none found.<p>
     */
    private WarehouseOperator findWarehouseOperatorOrThrowNotFound(Long warehouseOperatorId) {
        return warehouseOperatorRepo.findById(warehouseOperatorId).orElseThrow(() -> new NotFoundException("Operador não encontrado."));
    }

    /**
     * Method to find a Section giving It id.
     *
     * @param sectionId The id of the section.
     * @return A section with the given id or {@link NotFoundException} - if none found.
     */
    private Section findSectionOrThrowNotFound(Long sectionId){
        return sectionRepo.findById(sectionId).orElseThrow(() -> new NotFoundException("Seção não encontrada."));
    }
    /**
     * Method to find a product giving id.
     *
     * @param productId The id of the product.
     * @return A product with the given id or {@link NotFoundException} - if none found.
     */
    private Product findProductOrThrowNotFound(Long productId) {
        return productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }

    /**
     * Validates if an InboundOrder can be created.
     * @param itemBatchDtos List of items to be created.
     * @param warehouse Warehouse where It'll be stored.
     * @param warehouseOperator Who is going to store It.
     * @param section Section where It'll be stored.
     * @param products Products to be stored.
     */
    private void validateInboundOrderCreation(
            List<CreateItemBatchDto> itemBatchDtos,
            Warehouse warehouse,
            WarehouseOperator warehouseOperator,
            Section section,
            List<Product> products
    ) {
        Long volumeToBeStored = itemBatchDtos.stream().map(CreateItemBatchDto::getVolume)
                .reduce(0L, Long::sum);

        verifyWarehouseMatchWithOperator(warehouse, warehouseOperator);
        verifyWarehouseMatchSection(section, warehouse);
        verifyIfProductsCategoryDifferFromSection(products, section);
        verifyIfSectionCanStoreItems(section, volumeToBeStored);
        verifySeller(products);
    }

    /**
     * Validates if the InboundOrder ItemBatches can be updated.
     * @param itemBatchDtos List of InboundOrder items to be updated.
     * @param inboundOrder InboundOrder to be updated.
     * @param products Products to be updated.
     */
    private void validateInboundOrderUpdate(
            List<UpdateItemBatchDto> itemBatchDtos,
            InboundOrder inboundOrder,
            List<Product> products
    ) {
        Long volumeToBeStored = itemBatchDtos.stream().map(UpdateItemBatchDto::getVolume)
                .reduce(0L, Long::sum);
        Section section = inboundOrder.getSection();
        verifyIfProductsCategoryDifferFromSection(products, section);
        verifyIfSectionCanStoreItems(section, volumeToBeStored);
    }

    /**
     * Verify if the section has enough volume to store the items.
     * @param section The id of the section.
     * @param volumeToBeStored The quantity of volume to be used.
     * @throws BusinessRuleException if volume is not valid.
     */
    private void verifyIfSectionCanStoreItems(Section section, Long volumeToBeStored) {
        if (section.getVolume().compareTo(volumeToBeStored) < 0) {
            throw new BusinessRuleException("Volume do lote é maior que a capacidade disponível.");
        }
    }

    /**
     * Verify if the Section and Warehouse are related.
     * @param section The section.
     * @param warehouse The warehouse.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyWarehouseMatchSection(Section section, Warehouse warehouse) {
        if(!section.getWarehouse().getId().equals(warehouse.getId())){
            throw new BusinessRuleException("Esta seção não faz parte do armazém.");
        }
    }

    /**
     * Verify if the provided Products categories are equal to the given Section.
     * @param products Product list.
     * @param section Section to be compared.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyIfProductsCategoryDifferFromSection(List<Product> products, Section section) {
        boolean validStorageType = products.stream()
                .map(Product::getCategory)
                .allMatch(storageType -> section.getCategory().getName().equals(storageType.getName()));
        if(!validStorageType) {
            throw new BusinessRuleException("A categoria do Produto não é compatível com a seção.");
        }
    }

    /**
     * Verify if the Warehouse and Warehouse Operator are related.
     * @param warehouseOperator The operator.
     * @param warehouse The warehouse.
     * @throws BusinessRuleException if does not match.
     */
    private void verifyWarehouseMatchWithOperator(Warehouse warehouse, WarehouseOperator warehouseOperator) {
        if(!warehouse.getWarehouseOperator().getId().equals(warehouseOperator.getId())){
            throw new BusinessRuleException("Este operador não faz parte do armazém.");
        }
    }

    private void verifySeller(List<Product> products) {
        products.forEach((batch) -> {
            Seller seller = productRepo.findSellerByProductId(batch.getId())
                    .orElseThrow(() -> new NotFoundException("Vendedor não encontrado."));
            if(seller.getActive() == Active.INATIVO) {
                throw new BusinessRuleException("Vendedor inativo.");
            }
        });
    }
}
