package fact.it.inventoryservice.repository;

import fact.it.inventoryservice.model.StockItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface InventoryRepository extends JpaRepository<StockItem, Long> {
    List<StockItem> findBySkuCodeIn(List<String> skuCode);
}
