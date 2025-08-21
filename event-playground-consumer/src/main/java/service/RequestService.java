package service;

import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.Request;
import model.RequestDTO;
import model.RequestRepository;
import model.RequestState;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
import static model.RequestState.INVALID;
import static model.RequestState.RECEIVED;

@ApplicationScoped
public class RequestService {

    private static final Logger log = Logger.getLogger(RequestService.class);

    @Inject
    RequestRepository requestRepository;

    @Incoming("request-received")
    @Outgoing("request-persisted")
    @Blocking
    public Request persistRequest(RequestDTO requestDTO) {
        log.infof("Step 1 - %s - Persisting request: %s", requestDTO.uuid(), requestDTO);
        return persistEntity(requestDTO);
    }

    @Transactional(REQUIRES_NEW)
    public void invalidateRequest(UUID uuid, String invalidationReason) {
        Request entity = requestRepository.findByUuid(uuid)
                .orElseThrow(() -> new IllegalStateException("Request not found in DB"));

        entity.setRequestState(INVALID);
        entity.setInvalidationReason(invalidationReason);
    }

    @Transactional(REQUIRES_NEW)
    public Request persistEntity(RequestDTO requestDTO) {
        Request request = Request.builder()
                .requestUuid(requestDTO.uuid())
                .eventType(requestDTO.eventName())
                .mountains(requestDTO.mountains())
                .date(requestDTO.date())
                .note(requestDTO.note())
                .requestState(RECEIVED)
                .build();

        request.persist();
        return request;
    }


}
