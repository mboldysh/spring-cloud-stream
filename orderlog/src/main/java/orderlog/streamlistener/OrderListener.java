package orderlog.streamlistener;

import lombok.extern.slf4j.Slf4j;
import orderlog.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
@Slf4j
public class OrderListener {

    @StreamListener(Sink.INPUT)
    public void logOrder(Order order) {
        log.info("{}", order);
    }
}
