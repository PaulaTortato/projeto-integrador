package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.InboundOrder;
import com.grupo6.projetointegrador.model.entity.Section;

import java.time.LocalDate;
import java.util.List;

public class InboundOrderFactory {
    public static InboundOrder build(Section section) {
        InboundOrder inboundOrder = new InboundOrder();

        inboundOrder.setWarehouse(section.getWarehouse());
        inboundOrder.setWarehouseOperator(section.getWarehouse().getWarehouseOperator());
        inboundOrder.setSection(section);

        inboundOrder.setId(1L);
        inboundOrder.setItemBatches(List.of(ItemBatchFactory.build(inboundOrder)));
        inboundOrder.setOrderDate(LocalDate.of(2020, 11, 9));

        return inboundOrder;
    }
}
