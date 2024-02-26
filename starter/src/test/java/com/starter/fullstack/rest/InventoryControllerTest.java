package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.api.Product;
import com.starter.fullstack.dao.InventoryDAO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InventoryControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private ObjectMapper objectMapper;

  private InventoryDAO inventoryDAO;
  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("1");
    this.inventory.setName("Item 1");
    this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  @Test
  public void testCreate() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("1");
    this.inventory.setName("Item 1");
    this.inventoryDAO.create(inventory);
    // When/Then
    this.mockMvc.perform(post("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
        .andExpect(status().isOk());

    Assert.assertEquals(3, this.mongoTemplate.findAll(Inventory.class).size());
  }
}