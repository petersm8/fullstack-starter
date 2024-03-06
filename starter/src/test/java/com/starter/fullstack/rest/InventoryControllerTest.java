package com.starter.fullstack.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starter.fullstack.api.Inventory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private MongoTemplate mongoTemplate;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("ID");
    this.inventory.setName("TEST");
    this.inventory.setProductType("TEST");
    this.inventory = this.mongoTemplate.save(this.inventory);
  }

  @After
  public void teardown() {
    this.mongoTemplate.dropCollection(Inventory.class);
  }

  @Test
  public void create() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("OTHER ID");
    this.inventory.setName("ALSO TEST");
    this.inventory.setProductType("TEST");
    Assert.assertEquals(1, this.mongoTemplate.findAll(Inventory.class).size());
    this.mockMvc.perform(post("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(this.inventory)))
      .andExpect(status().isOk());

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }

  @Test
  public void deleteTest() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("OTHER ID");
    this.inventory.setName("ALSO TEST");
    this.inventory.setProductType("TEST");
    this.inventory = this.mongoTemplate.save(this.inventory);

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
    this.mockMvc.perform(delete("/inventory")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.inventory.getId()))
      .andExpect(status().isOk());
    
    Assert.assertEquals(1, this.mongoTemplate.findAll(Inventory.class).size());
  }

}
