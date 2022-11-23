package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;
import com.grupo6.projetointegrador.model.entity.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {
    ItemBatch findByProductId(Long productId);

    @Query(value = "SELECT * FROM item_batch AS t1 LEFT JOIN product AS t2 ON t1.product_id = t2.id " +
            "LEFT JOIN seller AS t3 ON t2.seller_id = t3.id " +
            "WHERE t1.product_id = ?1 AND t3.active = 'ATIVO'", nativeQuery = true)
    List<ItemBatch> findAllByProductId(Long productId);

    @Query(value = "SELECT * FROM item_batch AS t1 LEFT JOIN product AS t2 ON t1.product_id = t2.id " +
            "LEFT JOIN seller AS t3 ON t2.seller_id = t3.id " +
            "WHERE t1.product_id = ?1 AND t3.active = 'ATIVO' ORDER BY t1.id ASC", nativeQuery = true)
    List<ItemBatch> findAllByProductIdOrderByIdAsc(Long productId);

    @Query(value = "SELECT * FROM item_batch AS t1 LEFT JOIN product AS t2 ON t1.product_id = t2.id " +
            "LEFT JOIN seller AS t3 ON t2.seller_id = t3.id " +
            "WHERE t1.product_id = ?1 AND t3.active = 'ATIVO' ORDER BY t1.product_quantity ASC", nativeQuery = true)
    List<ItemBatch> findAllByProductIdOrderByProductQuantityAsc(Long productId);

    @Query(value = "SELECT * FROM item_batch AS t1 LEFT JOIN product AS t2 ON t1.product_id = t2.id " +
            "LEFT JOIN seller AS t3 ON t2.seller_id = t3.id " +
            "WHERE t1.product_id = ?1 AND t3.active = 'ATIVO' ORDER BY t1.due_date ASC", nativeQuery = true)
    List<ItemBatch> findAllByProductIdOrderByDueDateAsc(Long productId);
    /**
     * @param productId
     * @param productQuantity
     * @return Optional<ItemBatch>
     */
    @Query(value = "SELECT t1.* FROM `item_batch` AS t1 LEFT JOIN product AS t2 ON t1.product_id = t2.id " +
            "LEFT JOIN seller AS t3 ON t2.seller_id = t3.id " +
            "WHERE t1.due_date > CURRENT_DATE + 21 AND t1.product_id = (?1) " +
            "AND t1.product_quantity >= (?2) AND t3.active = 'ATIVO' ORDER BY t1.due_date ASC LIMIT 1 ;", nativeQuery = true)
    Optional<ItemBatch> findByDueDate21AndProductIdAndQty(Long productId, int productQuantity);

    @Query(value = "SELECT t1.id AS itemBatchId," +
      "    t1.product_id AS productId," +
      "    t1.category as category," +
      "    t1.due_date AS dueDate," +
      "    t1.product_quantity AS quantity" +
      "    FROM item_batch AS t1" +
      "    LEFT JOIN inbound_order AS t2 ON t2.id = t1.inbound_order_id" +
            "    LEFT JOIN product AS t3 ON t3.id = t1.product_id" +
            "    LEFT JOIN seller AS t4 ON t4.id = t3.seller_id" +
      "    WHERE t2.section_id = ?1" +
      "    AND t1.product_quantity > 0" +
            "    AND t4.active = 'ATIVO'" +
      "    AND due_date BETWEEN curdate() AND curdate() + interval ?2 day ORDER BY dueDate", nativeQuery = true)
    List<DueDateItemBatchDto> findByDueDateWithSectionId(Long sectionId, int days);

    @Query(value = "SELECT t1.id AS itemBatchId," +
            "    t1.product_id AS productId," +
            "    t1.category as category," +
            "    t1.due_date AS dueDate," +
            "    t1.product_quantity AS quantity" +
            "    FROM item_batch AS t1" +
            "    LEFT JOIN product AS t2 ON t2.id = t1.product_id" +
            "    LEFT JOIN seller AS t3 ON t3.id = t2.seller_id" +
            "    WHERE t1.category = ?1" +
            "    AND t3.active = 'ATIVO'" +
            "    AND t1.product_quantity > 0" +
            "    AND due_date BETWEEN curdate() AND curdate() + interval ?2 day" +
            "    ORDER BY CASE WHEN ?3 = 'desc' THEN dueDate end DESC," +
            "    CASE WHEN ?3 != 'desc' THEN dueDate end ASC;", nativeQuery = true)
    Optional<List<DueDateItemBatchDto>> findByDueDateWithCategory(String category, int days, String order);
}
