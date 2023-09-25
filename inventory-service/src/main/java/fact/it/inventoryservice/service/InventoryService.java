package fact.it.inventoryservice.service;

import fact.it.inventoryservice.dto.InventoryResponse;
import fact.it.inventoryservice.model.StockItem;
import fact.it.inventoryservice.repository.InventoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @PostConstruct
    public void loadData() {
        if(inventoryRepository.count() <= 0){
            StockItem stockItem = new StockItem();
            stockItem.setSkuCode("tube6in");
            stockItem.setQuantity(100);

            StockItem stockItem1 = new StockItem();
            stockItem1.setSkuCode("beam10ft");
            stockItem1.setQuantity(0);

            inventoryRepository.save(stockItem);
            inventoryRepository.save(stockItem1);
        }
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(stockItem ->
                        InventoryResponse.builder()
                                .skuCode(stockItem.getSkuCode())
                                .isInStock(stockItem.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
