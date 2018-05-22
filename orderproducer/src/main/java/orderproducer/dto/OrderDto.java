package orderproducer.dto;

import lombok.Data;
import orderproducer.model.GoodsType;
import orderproducer.model.Order;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class OrderDto {

    @NotNull
    private String userName;

    @NotNull
    private GoodsType goodsType;

    @NotNull
    @Min(value = 1, message = "Quantity should be positive number")
    private int quantity;

    @NotNull
    @Digits(integer = 6, fraction = 2, message = "totalPrice should be positive number")
    private BigDecimal totalPrice;

    public static Order createOrderFromDto(OrderDto orderDto) {
        Order order = new Order();
        order.setUserName(orderDto.getUserName());
        order.setQuantity(orderDto.getQuantity());
        order.setTotalPrice(orderDto.getTotalPrice());
        return order;
    }

}
