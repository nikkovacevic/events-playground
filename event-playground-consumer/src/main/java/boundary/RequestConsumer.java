package boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.DLQMessage;
import model.RequestDTO;
import model.RequestRepository;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import service.RequestService;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;

@ApplicationScoped
public class RequestConsumer {

    private static final Logger log = Logger.getLogger(RequestConsumer.class);

    @Inject
    ObjectMapper objectMapper;

    @Inject
    RequestService requestService;

    @Channel("request-received")
    Emitter<RequestDTO> persistRequestEmitter;

    @Channel("requests-dlq")
    Emitter<DLQMessage> dlqMessageEmitter;

    @Incoming("requests-in")
    public void consume(Object msg) {
        log.infof("Step 0 - Message received from artemis: %s", msg);
        try {
            RequestDTO dto = objectMapper.readValue(msg.toString(), RequestDTO.class);
            persistRequestEmitter.send(dto).whenComplete((success, failure) -> {
                if (failure != null) {
                    log.errorf("Step X - %s - Error processing message: %s", dto.uuid(), failure.getMessage());
                    dlqMessageEmitter.send(new DLQMessage(dto.uuid(), failure.getMessage()));
                } else {
                    log.infof("Step X - %s - Message processed successfully: %s", dto.uuid(), dto);
                }
            });
        } catch (Exception e) {
            log.errorf(e, "Error parsing message: %s", msg);
        }
    }

    @Incoming("requests-dlq")
    @Transactional(REQUIRES_NEW)
    public void handleDlq(DLQMessage msg) {
        requestService.invalidateRequest(msg.uuid(), msg.reason());
    }

}
