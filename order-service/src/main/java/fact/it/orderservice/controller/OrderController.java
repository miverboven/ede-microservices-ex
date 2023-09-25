package fact.it.orderservice.controller;

import fact.it.orderservice.dto.OrderRequest;
import fact.it.orderservice.dto.OrderResponse;
import fact.it.orderservice.model.Order;
import fact.it.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        boolean result = orderService.placeOrder(orderRequest);
        return (result ? "Order placed successfully" : "Order placement failed");
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllProducts() {
        return orderService.getAllOrders();
    }
}
