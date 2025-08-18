package service;

import jakarta.enterprise.context.ApplicationScoped;
import model.ConsumerMessage;
import model.EventType;
import model.RequestAction;
import model.RequestDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import java.time.LocalDateTime;
import java.util.UUID;

import static model.EventType.MOUNTAIN_CLIMBED;
import static model.EventType.MOUNTAIN_WISHLISTED;
import static org.eclipse.microprofile.reactive.messaging.OnOverflow.Strategy.UNBOUNDED_BUFFER;

@ApplicationScoped
public class RequestService {

    @Channel("requests")
    @OnOverflow(UNBOUNDED_BUFFER)
    Emitter<ConsumerMessage> consumerMessageEmitter;

    public void sendMessagesToConsumer(RequestDTO requestDTO) {
        ConsumerMessage message = createMessage(requestDTO);
        sendMessage(message);
    }

    private void sendMessage(ConsumerMessage message) {
        consumerMessageEmitter.send(message);
    }

    private ConsumerMessage createMessage(RequestDTO requestDTO) {
        return new ConsumerMessage(
                UUID.randomUUID(),
                LocalDateTime.now(),
                getConsumerEvent(requestDTO.requestAction()),
                requestDTO.mountain(),
                requestDTO.date(),
                requestDTO.note());
    }

    private EventType getConsumerEvent(RequestAction requestAction) {
        EventType eventType = null;
        switch (requestAction) {
            case CLIMBED -> eventType = MOUNTAIN_CLIMBED;
            case ADD_TO_WISHLIST -> eventType = MOUNTAIN_WISHLISTED;
        }
        return eventType;
    }
}
