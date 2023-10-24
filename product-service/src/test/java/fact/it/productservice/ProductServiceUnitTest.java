package fact.it.productservice;

import fact.it.productservice.dto.ProductRequest;
import fact.it.productservice.dto.ProductResponse;
import fact.it.productservice.model.Product;
import fact.it.productservice.repository.ProductRepository;
import fact.it.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct() {
        // Arrange
        ProductRequest productRequest = new ProductRequest();
        productRequest.setSkuCode("SKU123");
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(BigDecimal.valueOf(100));

        // Act
        productService.createProduct(productRequest);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        Product product = new Product();
        product.setId("1");
        product.setSkuCode("SKU123");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(100));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act
        List<ProductResponse> products = productService.getAllProducts();

        // Assert
        assertEquals(1, products.size());
        assertEquals("SKU123", products.get(0).getSkuCode());
        assertEquals("Test Product", products.get(0).getName());
        assertEquals("Test Description", products.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(100), products.get(0).getPrice());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllProductsBySkuCode() {
        // Arrange
        Product product = new Product();
        product.setId("1");
        product.setSkuCode("SKU123");
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(100));

        when(productRepository.findBySkuCodeIn(Arrays.asList("SKU123"))).thenReturn(Arrays.asList(product));

        // Act
        List<ProductResponse> products = productService.getAllProductsBySkuCode(Arrays.asList("SKU123"));

        // Assert
        assertEquals(1, products.size());
        assertEquals("1", products.get(0).getId());
        assertEquals("SKU123", products.get(0).getSkuCode());
        assertEquals("Test Product", products.get(0).getName());
        assertEquals("Test Description", products.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(100), products.get(0).getPrice());

        verify(productRepository, times(1)).findBySkuCodeIn(Arrays.asList(product.getSkuCode()));
    }
}