package orderlog.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    private String userName;
    private int quantity;
    private BigDecimal totalPrice;
    private boolean valid;
    private String summaryInformation;
}
