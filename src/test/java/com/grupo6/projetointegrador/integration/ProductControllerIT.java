package com.grupo6.projetointegrador.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grupo6.projetointegrador.dto.ProductLocationDto;
import com.grupo6.projetointegrador.dto.ProductWarehousesDto;
import com.grupo6.projetointegrador.factory.InboundOrderFactory;
import com.grupo6.projetointegrador.factory.WarehouseFactory;
import com.grupo6.projetointegrador.model.entity.*;
import com.grupo6.projetointegrador.model.enumeration.Active;
import com.grupo6.projetointegrador.model.enumeration.Category;
import com.grupo6.projetointegrador.repository.*;
import com.grupo6.projetointegrador.response.PageableResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private WarehouseRepo warehouseRepo;

    @Autowired
    private WarehouseOperatorRepo warehouseOperatorRepo;

    @Autowired
    private ItemBatchRepo itemBatchRepo;

    @Autowired
    private SectionRepo sectionRepo;

    @Autowired
    private InboundOrderRepo inboundOrderRepo;

    @Autowired
    private SellerRepo sellerRepo;

    @BeforeAll
    void setup() {
        createProductsAndItemBatches();
    }

    @Test
    void findProductById_getOrderedByQuantityAsc_whenOrderValueIsQ() throws Exception {
        // Given
        Long productId = 1L;
        String order = "Q";

        createProductAndSeller(productId, 1L);
        // When
        ResultActions result = mockMvc.perform(get("/api/products/" + productId + "?order=" + order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ProductLocationDto productLocationDto = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                ProductLocationDto.class
        );

        // Then
        int item1Quantity = productLocationDto.getItemBatchLocationDto().get(0).getProductQuantity();
        int item2Quantity = productLocationDto.getItemBatchLocationDto().get(1).getProductQuantity();

        assertThat(productLocationDto).isNotNull();
        assertThat(item1Quantity).isLessThan(item2Quantity);
    }

    @Test
    void findProductById_getOrderedByItemBatchIdAsc_whenOrderValueIsL() throws Exception {
        // Given
        Long productId = 1L;
        String order = "L";

        createProductAndSeller(productId, 1L);
        // When
        ResultActions result = mockMvc.perform(get("/api/products/" + productId + "?order=" + order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ProductLocationDto productLocationDto = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                ProductLocationDto.class
        );

        // Then
        Long item1Id = productLocationDto.getItemBatchLocationDto().get(0).getItemBatchId();
        Long item2Id = productLocationDto.getItemBatchLocationDto().get(1).getItemBatchId();

        assertThat(productLocationDto).isNotNull();
        assertThat(item1Id).isLessThan(item2Id);
    }

    @Test
    void findProductById_getOrderedByDueDateAsc_whenOrderValueIsV() throws Exception {
        // Given
        Long productId = 1L;
        String order = "V";

        createProductAndSeller(productId, 1L);
        // When
        ResultActions result = mockMvc.perform(get("/api/products/" + productId + "?order=" + order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ProductLocationDto productLocationDto = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                ProductLocationDto.class
        );

        // Then
        LocalDate item1DueDate = productLocationDto.getItemBatchLocationDto().get(0).getDueDate();
        LocalDate item2DueDate = productLocationDto.getItemBatchLocationDto().get(1).getDueDate();

        assertThat(productLocationDto).isNotNull();
        assertThat(item1DueDate).isBefore(item2DueDate);
    }

    @Test
    void findProductById_throwNotFound_whenProductDoesNotExists() throws Exception {
        // Given
        Long productId = 100L;
        String order = "V";

        // When / Then
        mockMvc.perform(get("/api/products/" + productId + "?order=" + order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProductById_throwNotFound_whenProductItemBatchDoesNotExists() throws Exception {
        // Given
        Long productId = 2L;
        String order = "V";

        // When / Then
        mockMvc.perform(get("/api/products/" + productId + "?order=" + order)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllFreshProducts_getAllProducts_whenItExists() throws Exception {
        // Given
        String page = "0";

        // When
        ResultActions result = mockMvc.perform(get("/api/products" + "?page=" + page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        PageableResponse pageableResponse = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                PageableResponse.class
        );

        // Then
        assertThat(pageableResponse.getContent().size()).isEqualTo(2);
    }

    @Test
    void findAllFreshProducts_throwNotFound_whenThereAreNoProductForGivenPage() throws Exception {
        // Given
        String page = "10";

        // When / Then
        mockMvc.perform(get("/api/products" + "?page=" + page)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProductsCategory_getFrescoProducts_whenItExists() throws Exception {
        // Given
        String page = "0";
        String category = "FS";

        createProductAndSeller(1L, 1L);
        createProductAndSeller(2L, 1L);
        // When
        ResultActions result = mockMvc.perform(get("/api/products/category-search" + "?page=" + page + "&category=" + category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        PageableResponse pageableResponse = objectMapper.readValue(
                result.andReturn().getResponse().getContentAsString(),
                PageableResponse.class
        );

        // Then
        assertThat(pageableResponse.getContent().size()).isEqualTo(2);
    }

    @Test
    void findProductsCategory_throwNotFound_whenThereAreNoCongeladoProducts() throws Exception {
        // Given
        String page = "0";
        String category = "FF";

        // When
        mockMvc.perform(get("/api/products/category-search" + "?page=" + page + "&category=" + category)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findProductWarehouses_getProductsWarehouse_whenItExists() throws Exception {
        // Given
        Long productId = 1L;

        createProductAndSeller(productId, 1L);
        // When
        mockMvc.perform(get("/api/products/warehouse/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("warehouses").isNotEmpty());
    }

    @Test
    void findProductWarehouses_throwsNotFound_whenItDoesNotExists() throws Exception {
        // Given
        Long productId = 3L;

        // When
        mockMvc.perform(get("/api/products/warehouse/" + productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private List<ItemBatch> createProductsAndItemBatches() {
        Warehouse warehouse = WarehouseFactory.build();
        List<Section> sections = warehouse.getSections();

        warehouseOperatorRepo.save(warehouse.getWarehouseOperator());

        warehouse.setSections(List.of());
        warehouseRepo.save(warehouse);

        sectionRepo.saveAll(sections);

        warehouse.setSections(sections);
        warehouseRepo.save(warehouse);

        InboundOrder inboundOrder = InboundOrderFactory.build(sections.get(0));
        inboundOrder.setItemBatches(List.of());

        inboundOrderRepo.save(inboundOrder);

        Product product = productRepo.save(new Product(1L, BigDecimal.valueOf(10), Category.FRESCO, null));
        productRepo.save(new Product(2L, BigDecimal.valueOf(10), Category.FRESCO, null));
        List<ItemBatch> itemBatches = itemBatchRepo.saveAll(
                List.of(
                    new ItemBatch(
                        1L,
                        product,
                        10,
                        LocalDate.now(),
                        LocalDateTime.now(),
                        300L,
                        LocalDate.now().plusDays(50),
                        BigDecimal.valueOf(100),
                            inboundOrder,
                        product.getCategory()
                    ),
                    new ItemBatch(
                            2L,
                            product,
                            5,
                            LocalDate.now(),
                            LocalDateTime.now(),
                            200L,
                            LocalDate.now().plusDays(40),
                            BigDecimal.valueOf(1000),
                            inboundOrder,
                            product.getCategory()
                    )
                )
        );

        inboundOrder.setItemBatches(itemBatches);
        inboundOrderRepo.save(inboundOrder);

        return itemBatches;
    }

    private void createProductAndSeller(Long productId, Long sellerId) {
        Seller seller = sellerRepo.save(new Seller(
                sellerId,
                "Fulano",
                "de Tal",
                "fulano.dtal@teste.com",
                "Rua Canopus",
                123,
                "86070180",
                Active.ATIVO,
                null
        ));
        Product product = productRepo.save(new Product(productId, BigDecimal.valueOf(5), Category.FRESCO, seller));

        seller.setProducts(List.of(product));
        sellerRepo.save(seller);
    }
}
