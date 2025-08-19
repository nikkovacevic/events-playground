package service;

import jakarta.enterprise.context.ApplicationScoped;
import model.RequestDTO;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ValidationService {

    private static final Logger log = Logger.getLogger(ValidationService.class);

    @Incoming("request-persisted")
    public void validateRequest(RequestDTO requestDTO) {
        log.infof("Step 2 - %s - Validating request: %s", requestDTO.uuid(), requestDTO);
        throw new IllegalArgumentException("Ne gre skozi");
    }
}
