package fact.it.inventoryservice;

import fact.it.inventoryservice.dto.InventoryResponse;
import fact.it.inventoryservice.model.StockItem;
import fact.it.inventoryservice.repository.InventoryRepository;
import fact.it.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceUnitTests {

    @InjectMocks
    private InventoryService inventoryService;

    @Mock
    private InventoryRepository inventoryRepository;

    @Test
    public void testIsInStock() {
        // Arrange
        List<String> skuCodes = Arrays.asList("sku1", "sku2");
        StockItem stockItem1 = new StockItem(1L, "sku1", 5);
        StockItem stockItem2 = new StockItem(2L, "sku2", 0);
        List<StockItem> stockItems = Arrays.asList(stockItem1, stockItem2);

        when(inventoryRepository.findBySkuCodeIn(skuCodes)).thenReturn(stockItems);

        // Act
        List<InventoryResponse> inventoryResponses = inventoryService.isInStock(skuCodes);

        // Assert
        assertEquals(2, inventoryResponses.size());
        assertEquals("sku1", inventoryResponses.get(0).getSkuCode());
        assertEquals(true, inventoryResponses.get(0).isInStock());
        assertEquals("sku2", inventoryResponses.get(1).getSkuCode());
        assertEquals(false, inventoryResponses.get(1).isInStock());

        verify(inventoryRepository, times(1)).findBySkuCodeIn(skuCodes);
    }
}