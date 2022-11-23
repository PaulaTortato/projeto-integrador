package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.model.entity.Section;
import com.grupo6.projetointegrador.model.entity.Warehouse;
import com.grupo6.projetointegrador.model.entity.WarehouseOperator;
import com.grupo6.projetointegrador.model.enumeration.Category;
import lombok.Setter;

import java.util.List;

@Setter
public class WarehouseFactory {
    public static Warehouse build() {
        Warehouse warehouse = new Warehouse();

        warehouse.setId(1L);
        warehouse.setSections(genericSections(warehouse));
        warehouse.setWarehouseOperator(genericWarehouseOperator());
        return warehouse;
    }

    private static List<Section> genericSections(Warehouse warehouse) {
        List<Section> sections = List.of(
                new Section(1L, warehouse, 200L, Category.FRESCO),
                new Section(2L, warehouse, 200L, Category.CONGELADO)
        );
        warehouse.setSections(sections);
        return sections;
    }

    private static WarehouseOperator genericWarehouseOperator() {
        return new WarehouseOperator(1L, null);
    }
}
