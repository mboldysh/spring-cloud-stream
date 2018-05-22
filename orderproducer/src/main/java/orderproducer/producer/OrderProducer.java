package orderproducer.producer;

import orderproducer.dto.OrderDto;

public interface OrderProducer {

    void sendOrderMessage(OrderDto orderDto);
}
