package com.grupo6.projetointegrador.repository;

import com.grupo6.projetointegrador.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepo extends JpaRepository<Buyer, Long> {
}

