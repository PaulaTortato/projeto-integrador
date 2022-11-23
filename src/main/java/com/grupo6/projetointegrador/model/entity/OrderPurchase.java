package com.grupo6.projetointegrador.model.entity;

import com.grupo6.projetointegrador.model.enumeration.StatusOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Buyer buyer;

    private LocalDate dateOrder;

    @OneToMany(
            mappedBy = "orderPurchase",
            cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}
    )
    private List<ProductOrder> productOrders;

    @Enumerated(EnumType.ORDINAL)
    private StatusOrder status;
}
