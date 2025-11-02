package com.acme.catalog;

import com.acme.catalog.domain.InventoryItem;
import com.acme.catalog.domain.Product;
import com.acme.catalog.repo.InventoryItemRepository;
import com.acme.catalog.repo.ProductRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = ServiceCatalogApplication.class)
class RepositoryTests {

  @Container
  static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:16");

  @DynamicPropertySource
  static void datasourceProps(DynamicPropertyRegistry r) {
    r.add("spring.datasource.url", db::getJdbcUrl);
    r.add("spring.datasource.username", db::getUsername);
    r.add("spring.datasource.password", db::getPassword);
  }
  @Autowired
  ProductRepository products;
  @Autowired
  InventoryItemRepository inventory;

  @Test
  void savesAndLoadsProductWithInventory() {
    var p = new Product();
    p.setSku("SKU-001");
    p.setName("Laptop");
    p.setPriceAmount(BigDecimal.valueOf(3999.00));

    var i = new InventoryItem();
    i.setLocationCode("WH-1");
    i.setAvailableQty(10);
    i.setReservedQty(0);

    p.addInventory(i);
    products.saveAndFlush(p);

    var loaded = products.findWithInventoryBySku("SKU-001").orElseThrow();
    assertThat(loaded.getInventory()).hasSize(1);
  }

  @Test
  void uniqueSkuIsEnforced() {
    var p1 = new Product(); p1.setSku("DUP"); p1.setName("A"); p1.setPriceAmount(BigDecimal.TEN);
    var p2 = new Product(); p2.setSku("DUP"); p2.setName("B"); p2.setPriceAmount(BigDecimal.TEN);
    products.saveAndFlush(p1);
    assertThatThrownBy(() -> { products.saveAndFlush(p2); })
        .hasMessageContaining("uk_products_sku");
  }

  @Test
  void negativeQtyRejectedByCheckConstraint() {
    var p = new Product(); p.setSku("SKU-NEG"); p.setName("X"); p.setPriceAmount(BigDecimal.ONE);
    products.saveAndFlush(p);

    var item = new InventoryItem();
    item.setProduct(p);
    item.setLocationCode("WH-1");
    item.setAvailableQty(-1);
    item.setReservedQty(0);

    assertThatThrownBy(() -> inventory.saveAndFlush(item))
        .hasMessageContaining("ck_inventory_qty_nonnegative");
  }
}
