package service;

import jakarta.enterprise.context.ApplicationScoped;
import model.RequestDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RequestService {

    private static final Logger log = Logger.getLogger(RequestService.class);

    @Incoming("request-received")
    @Outgoing("request-persisted")
    public RequestDTO persistRequest(RequestDTO requestDTO) {
        log.infof("Step 1 - %s - Persisting request: %s", requestDTO.uuid(), requestDTO);
        return requestDTO;
    }


}
