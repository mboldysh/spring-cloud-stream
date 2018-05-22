package orderhandler.streamlistener;

import orderhandler.model.Order;
import orderhandler.validation.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;

@EnableBinding(Processor.class)
public class OrderListener {

    private Processor processor;

    private OrderValidator orderValidator;

    @Autowired
    public OrderListener(Processor processor, OrderValidator orderValidator) {
        this.processor = processor;
        this.orderValidator = orderValidator;
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['goodsType']=='COUNTABLE'")
    public void validateCountableOrder(Order order) {
        Order validateOrder = orderValidator.validateCountableOrder(order);
        processor.output().send(MessageBuilder.withPayload(validateOrder).build());
    }

    @StreamListener(target = Processor.INPUT, condition = "headers['goodsType']=='LIQUIDS'")
    public void validateLiquidsOrder(Order order) {
        Order validateOrder = orderValidator.validateLiquidOrder(order);
        processor.output().send(MessageBuilder.withPayload(validateOrder).build());
    }
}
