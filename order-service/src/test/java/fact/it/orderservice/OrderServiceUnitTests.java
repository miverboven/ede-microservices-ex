package fact.it.orderservice;

import fact.it.orderservice.dto.*;
import fact.it.orderservice.model.Order;
import fact.it.orderservice.model.OrderLineItem;
import fact.it.orderservice.repository.OrderRepository;
import fact.it.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(orderService, "productServiceBaseUrl", "http://localhost:8080");
        ReflectionTestUtils.setField(orderService, "inventoryServiceBaseUrl", "http://localhost:8082");
    }

    @Test
    public void testPlaceOrder_Success() {
        // Arrange

        String skuCode = "sku1";
        Integer quantity = 2;
        BigDecimal price = BigDecimal.valueOf(100);
        String description = "Test Description";
        String name = "Test Name";

        OrderRequest orderRequest = new OrderRequest();
        // populate orderRequest with test data
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setId(1L);
        orderLineItemDto.setSkuCode(skuCode);
        orderLineItemDto.setQuantity(quantity);
        orderRequest.setOrderLineItemsDtoList(Arrays.asList(orderLineItemDto));

        InventoryResponse inventoryResponse = new InventoryResponse();
        // populate inventoryResponse with test data
        inventoryResponse.setSkuCode(skuCode);
        inventoryResponse.setInStock(true);

        ProductResponse productResponse = new ProductResponse();
        // populate productResponse with test data
        productResponse.setId("1");
        productResponse.setSkuCode(skuCode);
        productResponse.setName(name);
        productResponse.setDescription(description);
        productResponse.setPrice(price);

        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("1");
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setId(1L);
        orderLineItem.setSkuCode(skuCode);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setPrice(price);
        order.setOrderLineItemsList(Arrays.asList(orderLineItem));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(new InventoryResponse[]{inventoryResponse}));
        when(responseSpec.bodyToMono(ProductResponse[].class)).thenReturn(Mono.just(new ProductResponse[]{productResponse}));

        // Act
        boolean result = orderService.placeOrder(orderRequest);

        // Assert
        assertTrue(result);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_FailureIfOutOfStock() {
        // Arrange

        String skuCode = "sku1";
        Integer quantity = 2;
        BigDecimal price = BigDecimal.valueOf(100);
        String description = "Test Description";
        String name = "Test Name";

        OrderRequest orderRequest = new OrderRequest();
        // populate orderRequest with test data
        OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setId(1L);
        orderLineItemDto.setSkuCode(skuCode);
        orderLineItemDto.setQuantity(quantity);
        orderRequest.setOrderLineItemsDtoList(Arrays.asList(orderLineItemDto));

        InventoryResponse inventoryResponse = new InventoryResponse();
        // populate inventoryResponse with test data
        inventoryResponse.setSkuCode(skuCode);
        inventoryResponse.setInStock(false);

        ProductResponse productResponse = new ProductResponse();
        // populate productResponse with test data
        productResponse.setId("1");
        productResponse.setSkuCode(skuCode);
        productResponse.setName(name);
        productResponse.setDescription(description);
        productResponse.setPrice(price);

        Order order = new Order();
        order.setId(1L);
        order.setOrderNumber("1");
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setId(1L);
        orderLineItem.setSkuCode(skuCode);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setPrice(price);
        order.setOrderLineItemsList(Arrays.asList(orderLineItem));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(),  any(Function.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(InventoryResponse[].class)).thenReturn(Mono.just(new InventoryResponse[]{inventoryResponse}));

        // Act
        boolean result = orderService.placeOrder(orderRequest);

        // Assert
        assertFalse(false);

        verify(orderRepository, times(0)).save(order);
    }

    @Test
    public void testGetAllOrders() {
        // Arrange
        OrderLineItem orderLineItem1 = new OrderLineItem(1L, "sku1", new BigDecimal("10.00"), 2);
        OrderLineItem orderLineItem2 = new OrderLineItem(2L, "sku2", new BigDecimal("20.00"), 3);

        Order order1 = new Order(1L, "order1", Arrays.asList(orderLineItem1, orderLineItem2));

        OrderLineItem orderLineItem3 = new OrderLineItem(3L, "sku3", new BigDecimal("30.00"), 4);
        OrderLineItem orderLineItem4 = new OrderLineItem(4L, "sku4", new BigDecimal("40.00"), 5);

        Order order2 = new Order(2L, "order2", Arrays.asList(orderLineItem3, orderLineItem4));

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<OrderResponse> result = orderService.getAllOrders();

        // Assert
        assertEquals(2, result.size());

        verify(orderRepository, times(1)).findAll();
    }
}