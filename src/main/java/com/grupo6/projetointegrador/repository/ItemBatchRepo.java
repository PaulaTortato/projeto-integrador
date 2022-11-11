package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemBatchRepo extends JpaRepository<ItemBatch, Long> {

  /**
   * @param productId
   * @param productQuantity
   * @return List<ItemBatch> or Null
   */
  @Query(value = "SELECT * FROM `item_batch` WHERE DATEDIFF(due_date, CURDATE()) > 21 AND product_id = (?1) " +
          "AND product_quantity >= (?2) ORDER BY due_date ASC LIMIT 1 ;", nativeQuery = true)
  ItemBatch findByDueDateAndQty(Long productId, int productQuantity);
}