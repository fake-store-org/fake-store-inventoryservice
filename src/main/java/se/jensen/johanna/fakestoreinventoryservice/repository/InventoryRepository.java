package se.jensen.johanna.fakestoreinventoryservice.repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.johanna.fakestoreinventoryservice.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

  List<Inventory> findByProductIdIn(Set<UUID> productIds);


}
