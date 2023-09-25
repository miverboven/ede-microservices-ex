package fact.it.orderservice.dto;

import fact.it.orderservice.model.OrderLineItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderNumber;
    private List<OrderLineItemDto> orderLineItemsList;
}
