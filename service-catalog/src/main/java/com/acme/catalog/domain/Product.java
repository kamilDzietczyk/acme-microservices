package com.acme.catalog.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products",
    indexes = @Index(name="idx_products_sku", columnList = "sku"),
    uniqueConstraints = @UniqueConstraint(name="uk_products_sku", columnNames = "sku"))
public class Product {
  @Id
  @Column(nullable = false, updatable = false)
  private UUID id = UUID.randomUUID();

  @Column(nullable = false, length = 64)
  private String sku;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(name = "description", columnDefinition = "text")
  private String description;

  @Column(name = "price_amount", nullable = false, precision = 19, scale = 2)
  private BigDecimal priceAmount;

  @Column(name = "price_currency", nullable = false, length = 3)
  private String priceCurrency = "PLN";

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private Set<InventoryItem> inventory = new HashSet<>();

  // get/set + helper
  public UUID getId() { return id; }
  public String getSku() { return sku; }
  public void setSku(String sku) { this.sku = sku; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String d) { this.description = d; }
  public BigDecimal getPriceAmount() { return priceAmount; }
  public void setPriceAmount(BigDecimal a) { this.priceAmount = a; }
  public String getPriceCurrency() { return priceCurrency; }
  public void setPriceCurrency(String c) { this.priceCurrency = c; }
  public Set<InventoryItem> getInventory() { return inventory; }
  public void addInventory(InventoryItem item){ item.setProduct(this); inventory.add(item); }

}
