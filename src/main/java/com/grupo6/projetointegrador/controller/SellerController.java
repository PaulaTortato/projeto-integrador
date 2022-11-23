package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.dto.InactiveSellerBatchDto;
import com.grupo6.projetointegrador.dto.UpdateSellerDto;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/seller")
@Validated
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody @Valid CreateSellerDto createSellerDto) {
        return new ResponseEntity<>(sellerService.createSeller(createSellerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> findSeller(@PathVariable Long id) {
        return new ResponseEntity<>(sellerService.findSeller(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id,
                                               @RequestBody @Valid UpdateSellerDto updateSellerDto) {
        return new ResponseEntity<>(sellerService.updateSeller(id, updateSellerDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/warehouse/{id}")
    public ResponseEntity<List<InactiveSellerBatchDto>> getInactiveSellerBatches(@PathVariable Long id) {
        return new ResponseEntity<>(sellerService.getInactiveSellerBatches(id), HttpStatus.OK);
    }
}
