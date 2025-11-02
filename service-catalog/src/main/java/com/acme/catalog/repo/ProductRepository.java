package com.acme.catalog.repo;

import com.acme.catalog.domain.Product;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
  Optional<Product> findBySku(String sku);

  @EntityGraph(attributePaths = "inventory")
  Optional<Product> findWithInventoryBySku(String sku);
}
