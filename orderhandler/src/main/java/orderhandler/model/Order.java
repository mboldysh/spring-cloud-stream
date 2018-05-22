package orderhandler.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Objects;

@Data
public class Order {
    private String userName;
    private Integer quantity;
    private BigDecimal totalPrice;
    private boolean valid;
    private String summaryInformation;
}
