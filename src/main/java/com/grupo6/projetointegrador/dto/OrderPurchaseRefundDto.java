package com.grupo6.projetointegrador.dto;

import com.grupo6.projetointegrador.model.enumeration.RefundReason;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OrderPurchaseRefundDto {
    private Long id;
    private RefundReason reason;
    private LocalDate refundDate;
    private OrderPurchaseDto purchase;
}
