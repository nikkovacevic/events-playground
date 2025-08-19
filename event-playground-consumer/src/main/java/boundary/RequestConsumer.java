package boundary;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import model.RequestDTO;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RequestConsumer {

    private static final Logger log = Logger.getLogger(RequestConsumer.class);

    @Inject
    ObjectMapper objectMapper;

    @Channel("request-received")
    Emitter<RequestDTO> persistRequestEmitter;


    @Incoming("requests-in")
    public void consume(Object msg) {
        log.infof("Step 0 - Message received from artemis: %s", msg);
        try {
            RequestDTO dto = objectMapper.readValue(msg.toString(), RequestDTO.class);
            persistRequestEmitter.send(dto).whenComplete((success, failure) -> {
                if (failure != null) {
                    log.errorf(failure, "Step X - %s - Error processing message: %s", dto.uuid(), dto);
                    // TODO: push to DLQ
                } else {
                    log.infof("Step X - %s - Message processed successfully: %s", dto.uuid(), dto);
                }
            });
        } catch (Exception e) {
            log.errorf(e, "Error parsing message: %s", msg);
        }
    }

}
