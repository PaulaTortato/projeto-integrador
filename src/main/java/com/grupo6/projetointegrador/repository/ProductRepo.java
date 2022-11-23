package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.InactiveSellerBatchDto;
import com.grupo6.projetointegrador.dto.WarehouseDto;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p FROM Product p")
    Page<Product> findPageableProducts(Pageable pageable);

    @Query(value = "SELECT p FROM Product p LEFT JOIN Seller s ON p.seller = s WHERE p.category = ?1 AND s.active = 'ATIVO'")
    Page<Product> findProductsByCategory(Pageable pageable, Category category);

    Page<Product> findPageableProductsByCategory(Pageable pageable, String category);

    @Query(value = "SELECT t3.warehouse_id AS warehouse, SUM(t2.product_quantity) as quantity FROM `product` t1 " +
            "RIGHT JOIN item_batch t2 ON t2.product_id = t1.id " +
            "LEFT JOIN inbound_order t3 ON t3.id = t2.inbound_order_id " +
            "LEFT JOIN seller AS t4 ON t4.id = t1.seller_id " +
            "WHERE t1.id = (?1) AND t4.active = 'ATIVO' AND t2.product_quantity > 0 " +
            "GROUP BY t3.warehouse_id", nativeQuery = true)
    List<WarehouseDto> findWarehousesByProduct(Long id);

    @Query(value = "SELECT s FROM Seller s LEFT JOIN Product p ON s = p.seller WHERE p.id = ?1")
    Optional<Seller> findSellerByProductId(Long id);

    @Query(value = "SELECT t3.seller_id AS sellerId, " +
            "t4.active, " +
            "t1.product_id AS productId, " +
            "t1.product_quantity AS quantity, " +
            "t2.section_id AS sectionId, " +
            "t1.category FROM item_batch AS t1 " +
            "LEFT JOIN inbound_order AS t2 ON t1.inbound_order_id = t2.id " +
            "LEFT JOIN product AS t3 ON t1.product_id = t3.id " +
            "LEFT JOIN seller AS t4 ON t3.seller_id = t4.id " +
            "WHERE t2.warehouse_id = ?1 AND t4.active = 'INATIVO' AND t1.product_quantity > 0", nativeQuery = true)
    List<InactiveSellerBatchDto> findBatchesInWarehouseFromInactiveSellers(Long warehouseId);
}
