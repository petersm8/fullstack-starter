package com.starter.fullstack.rest;

import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.dao.InventoryDAO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InventoryControllerTest {

  @Autowired
  private MongoTemplate mongoTemplate;

  private InventoryDAO inventoryDAO;

  private InventoryController inventoryController;

  private Inventory inventory;

  @Before
  public void setup() throws Throwable {
    this.inventory = new Inventory();
    this.inventory.setId("ID");
    this.inventory.setName("TEST");
    this.inventoryDAO = new InventoryDAO(this.mongoTemplate);
    this.inventory = this.mongoTemplate.save(this.inventory);
    this.inventoryController = new InventoryController(this.inventoryDAO);
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
    this.inventoryController.create(this.inventory);

    Assert.assertEquals(2, this.mongoTemplate.findAll(Inventory.class).size());
  }
}
