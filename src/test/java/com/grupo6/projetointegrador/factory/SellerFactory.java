package com.grupo6.projetointegrador.factory;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.model.entity.Product;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.Category;

import java.math.BigDecimal;
import java.util.List;

public class SellerFactory {
    private static Long id;

    private static List<Product> products;

    public static Seller build(CreateSellerDto createSellerDto) {
        Seller seller = new Seller();

        if (id == null) id = 1L;
        if (products == null || products.isEmpty()) products = List.of(genericProduct(seller));

        seller.setId(id);
        seller.setFirstName(createSellerDto.getFirstName());
        seller.setLastName(createSellerDto.getLastName());
        seller.setEmail(createSellerDto.getEmail());
        seller.setAddress(createSellerDto.getAddress());
        seller.setHouseNumber(createSellerDto.getHouseNumber());
        seller.setZipCode(createSellerDto.getZipCode());
        seller.setActive(Active.ATIVO);
        seller.setProducts(products);

        return seller;
    }

    private static Product genericProduct(Seller seller) {
        return new Product(1L, BigDecimal.valueOf(5), Category.FRESCO, seller);
    }
}
