package orderproducer.producer.cloudstream;

import orderproducer.model.GoodsType;
import orderproducer.dto.OrderDto;
import orderproducer.model.Order;
import orderproducer.producer.OrderProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class OrderProducerStreamTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private OrderProducer orderProducer;

    @Autowired
    private Source source;

    @Autowired
    private MessageCollector messageCollector;

    @Parameterized.Parameter(0)
    public String userName;

    @Parameterized.Parameter(1)
    public int quantity;

    @Parameterized.Parameter(2)
    public BigDecimal totalPrice;

    @Parameterized.Parameter(3)
    public GoodsType goodsType;

    @Parameterized.Parameters
    public static List<Object> data() {
        List<Object> data = new ArrayList<>();
        data.add(new Object[]{"test", 1, new BigDecimal(1), GoodsType.COUNTABLE});
        data.add(new Object[]{"test", 1, new BigDecimal(1), GoodsType.LIQUIDS});
        return data;
    }

    private OrderDto orderDto;

    private Order expectedOrder;

    @Before
    public void init() {
        orderDto = new OrderDto();
        orderDto.setUserName(userName);
        orderDto.setTotalPrice(totalPrice);
        orderDto.setQuantity(quantity);
        orderDto.setGoodsType(goodsType);
        expectedOrder = new Order();
        expectedOrder.setUserName(userName);
        expectedOrder.setTotalPrice(totalPrice);
        expectedOrder.setQuantity(quantity);
    }

    @Test
    public void test() throws InterruptedException, IOException {
        orderProducer.sendOrderMessage(orderDto);
        BlockingQueue<Message<?>> messages = messageCollector.forChannel(source.output());
        ObjectMapper objectMapper = new ObjectMapper();
        Message<?> message = messages.take();
        String goodsTypeHeader = message.getHeaders().get("goodsType").toString();
        Order actualOrder = objectMapper.readValue(message.getPayload().toString(), Order.class);
        assertEquals(goodsType.toString(), goodsTypeHeader);
        assertEquals(expectedOrder, actualOrder);
    }

}