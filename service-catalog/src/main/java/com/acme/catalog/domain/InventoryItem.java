package com.acme.catalog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;

@Entity
@Table(name = "inventory",
    uniqueConstraints = @UniqueConstraint(name="uk_inventory_product_location", columnNames={"product_id","location_code"}),
    indexes = {
        @Index(name="idx_inventory_product", columnList="product_id"),
        @Index(name="idx_inventory_location", columnList="location_code")
    })
public class InventoryItem {
  @Id
  @Column(nullable = false, updatable = false)
  private UUID id = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name="fk_inventory_product"))
  private Product product;

  @Column(name = "location_code", nullable = false, length = 64)
  private String locationCode;

  @Column(name = "available_qty", nullable = false)
  private int availableQty;

  @Column(name = "reserved_qty", nullable = false)
  private int reservedQty;

  // get/set
  public UUID getId() { return id; }
  public Product getProduct() { return product; }
  public void setProduct(Product product) { this.product = product; }
  public String getLocationCode() { return locationCode; }
  public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
  public int getAvailableQty() { return availableQty; }
  public void setAvailableQty(int availableQty) { this.availableQty = availableQty; }
  public int getReservedQty() { return reservedQty; }
  public void setReservedQty(int reservedQty) { this.reservedQty = reservedQty; }

}
