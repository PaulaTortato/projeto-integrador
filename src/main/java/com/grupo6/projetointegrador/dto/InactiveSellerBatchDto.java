package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.Category;

public interface InactiveSellerBatchDto {
    Long getSellerId();
    Active getActive();
    Long getProductId();
    Integer getQuantity();
    Long getSectionId();
    Category getCategory();
}
