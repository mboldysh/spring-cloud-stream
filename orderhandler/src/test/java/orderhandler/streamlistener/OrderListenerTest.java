package orderhandler.streamlistener;

import orderhandler.model.GoodsType;
import orderhandler.model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext
public class OrderListenerTest {

    private static final String SUCCESS_VALIDATION = "Success validation";
    private static final String VALIDATION_FAILED = "Validation failed. Total price greater then threshold.";
    private static final BigDecimal COUNTABLE_THRESHOLD = new BigDecimal(10);
    private static final BigDecimal LIQUIDS_THRESHOLD = new BigDecimal(10);

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private Processor processor;

    @Parameterized.Parameter(0)
    public String userName;

    @Parameterized.Parameter(1)
    public int quantity;

    @Parameterized.Parameter(2)
    public BigDecimal totalPrice;

    @Parameterized.Parameter(3)
    public boolean valid;

    @Parameterized.Parameter(4)
    public String summaryInformation;

    @Parameterized.Parameter(5)
    public String goodsType;

    @Parameterized.Parameters
    public static List<Object> data() {
        List<Object> data = new ArrayList<>();
        data.add(new Object[]{"test", 1, COUNTABLE_THRESHOLD.subtract(new BigDecimal(1)), true,
                SUCCESS_VALIDATION, GoodsType.COUNTABLE.toString()});
        data.add(new Object[]{"test", 1, COUNTABLE_THRESHOLD.add(new BigDecimal(1)), false, VALIDATION_FAILED,
                GoodsType.COUNTABLE.toString()});
        data.add(new Object[]{"test", 1, LIQUIDS_THRESHOLD.subtract(new BigDecimal(1)), true,
                SUCCESS_VALIDATION, GoodsType.LIQUIDS.toString()});
        data.add(new Object[]{"test", 1, LIQUIDS_THRESHOLD.add(new BigDecimal(1)), false, VALIDATION_FAILED,
                GoodsType.LIQUIDS.toString()});
        return data;
    }

    private Order order;
    private Order validateOrder;

    @Before
    public void init() {
        order = new Order();
        order.setUserName(userName);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        validateOrder = new Order();
        validateOrder.setUserName(userName);
        validateOrder.setQuantity(quantity);
        validateOrder.setTotalPrice(totalPrice);
        validateOrder.setValid(valid);
        validateOrder.setSummaryInformation(summaryInformation);
    }

    @Test
    public void shouldWriteMessageWithValidatedOrderToOutputTopicWhenSendOrderToInputTopic() throws InterruptedException, IOException {
        MessageHeaders messageHeaders = new MessageHeaders(Collections.singletonMap("goodsType", goodsType));
        processor.input().send(new GenericMessage<>(order, messageHeaders));
        BlockingQueue<Message<?>> messages = messageCollector.forChannel(processor.output());
        ObjectMapper objectMapper = new ObjectMapper();
        Order actualOrder = objectMapper.readValue(messages.take().getPayload().toString(), Order.class);
        assertEquals(validateOrder, actualOrder);
    }
}