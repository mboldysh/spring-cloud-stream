package orderhandler.validation;

import orderhandler.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PropertySource("classpath:validation.properties")
public final class OrderValidator {

    @Value("${countable_threshold}")
    private BigDecimal countableThreshold;

    @Value("${liquid_threshold}")
    private BigDecimal liquidsThreshold;

    @Value("${success_validation}")
    private String successValidation;

    @Value("${validation_failed}")
    private String validationFailed;

    private OrderValidator() {}

    public Order validateCountableOrder(Order order) {
       return validateOrder(order, countableThreshold);
    }

    public Order validateLiquidOrder(Order order) {
        return validateOrder(order, liquidsThreshold);
    }

    private Order validateOrder(Order order, BigDecimal threshold) {
        if (order.getTotalPrice().compareTo(threshold) > 0) {
            order.setValid(false);
            order.setSummaryInformation(validationFailed);
        } else {
            order.setValid(true);
            order.setSummaryInformation(successValidation);
        }
        return order;
    }
}
