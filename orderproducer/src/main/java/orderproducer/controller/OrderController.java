package orderproducer.controller;

import orderproducer.dto.OrderDto;
import orderproducer.producer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    private OrderProducer orderProducer;

    @Autowired
    public OrderController(OrderProducer orderProducer) {
        this.orderProducer = orderProducer;
    }

    @PostMapping("/order")
    public ResponseEntity createOrder(@Valid @RequestBody OrderDto orderDto) {
        orderProducer.sendOrderMessage(orderDto);
        return ResponseEntity.ok().body("Order was sent");
    }
}
