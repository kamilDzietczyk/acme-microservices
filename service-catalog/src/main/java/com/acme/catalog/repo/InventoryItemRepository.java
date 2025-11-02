package com.acme.catalog.repo;

import com.acme.catalog.domain.InventoryItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, UUID> {
  List<InventoryItem> findByProduct_Id(UUID productId);
  List<InventoryItem> findByLocationCode(String locationCode);
}
