package com.tecsup.minishop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.minishop.model.Product;
import com.tecsup.minishop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Limpiamos la BD antes de cada test para garantizar aislamiento
        productRepository.deleteAll();
    }

    // ---------------------------------------------------------------
    // TEST 1: POST /api/products — crear producto, esperar 201 Created
    // ---------------------------------------------------------------
    @Test
    @DisplayName("POST /api/products — debe crear un producto y retornar 201")
    void shouldCreateProductAndReturn201() throws Exception {
        // ARRANGE
        Product product = Product.builder()
                .name("Webcam Logitech")
                .price(250.00)
                .stock(20)
                .build();

        // ACT & ASSERT
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Webcam Logitech"))
                .andExpect(jsonPath("$.price").value(250.00))
                .andExpect(jsonPath("$.stock").value(20));
    }

    // ---------------------------------------------------------------
    // TEST 2: GET /api/products — retornar lista completa
    // ---------------------------------------------------------------
    @Test
    @DisplayName("GET /api/products — debe retornar lista de productos")
    void shouldReturnAllProducts() throws Exception {
        // ARRANGE — insertamos directamente en la BD de prueba
        productRepository.save(Product.builder().name("Disco SSD").price(180.00).stock(10).build());
        productRepository.save(Product.builder().name("RAM 16GB").price(120.00).stock(8).build());

        // ACT & ASSERT
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Disco SSD"))
                .andExpect(jsonPath("$[1].name").value("RAM 16GB"));
    }

    // ---------------------------------------------------------------
    // TEST 3: GET /api/products/{id} — retornar producto correcto
    // ---------------------------------------------------------------
    @Test
    @DisplayName("GET /api/products/{id} — debe retornar el producto correcto")
    void shouldReturnProductById() throws Exception {
        // ARRANGE
        Product saved = productRepository.save(
                Product.builder().name("Impresora HP").price(350.00).stock(4).build()
        );

        // ACT & ASSERT
        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Impresora HP"))
                .andExpect(jsonPath("$.price").value(350.00));
    }

    // ---------------------------------------------------------------
    // TEST 4: GET /api/products/{id} con ID inexistente — esperar 500
    // ---------------------------------------------------------------
    @Test
    @DisplayName("GET /api/products/{id} — debe retornar 500 cuando el ID no existe")
    void shouldReturn500WhenProductNotFound() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/api/products/9999"))
                .andExpect(status().is5xxServerError());
    }
}
