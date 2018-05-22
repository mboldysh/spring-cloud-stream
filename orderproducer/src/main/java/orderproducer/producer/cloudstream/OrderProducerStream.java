package orderproducer.producer.cloudstream;

import orderproducer.dto.OrderDto;
import orderproducer.producer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@EnableBinding(Source.class)
public class OrderProducerStream implements OrderProducer {

    private Source sourceChannel;

    @Autowired
    public OrderProducerStream(Source sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    @Override
    public void sendOrderMessage(OrderDto orderDto) {
        Message message = MessageBuilder.withPayload(OrderDto.createOrderFromDto(orderDto))
                .setHeader("goodsType", orderDto.getGoodsType()).build();
        sourceChannel.output().send(message);
    }
}
