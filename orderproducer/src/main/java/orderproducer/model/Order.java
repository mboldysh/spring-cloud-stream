package orderproducer.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class Order {

    private String userName;
    private int quantity;
    private BigDecimal totalPrice;
}
