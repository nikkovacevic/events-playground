package service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.EventType;
import model.Request;
import model.RequestRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.time.LocalDate;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
import static model.RequestState.INVALID;
import static model.RequestState.VALIDATED;

@ApplicationScoped
public class ValidationService {

    private static final Logger log = Logger.getLogger(ValidationService.class);

    @Inject
    ExternalMockService externalMockService;

    @Inject
    RequestRepository requestRepository;

    @Inject
    RequestService requestService;

    @Incoming("request-persisted")
    @Outgoing("request-validated")
    public Request validateRequest(Request request) {
        log.infof("Step 2 - %s - Validating request: %s", request.getRequestUuid(), request);
        validateMountain(request);
        validateDate(request);
        return request;
    }

    private void validateMountain(Request request) {
        if (externalMockService.getMountainsInfo(request.getMountains()).isEmpty()) {
            invalidateRequest("Unknown mountain present", request.getMountains().toString());
        }
    }

    private void validateDate(Request request) {
        EventType requestEventType = request.getEventType();
        LocalDate requestDate = request.getDate();

        String invalidationReason = null;

        switch (requestEventType) {
            case MOUNTAIN_CLIMBED -> {
                if (requestDate.isAfter(LocalDate.now())) {
                    invalidationReason = "Date should be in the past";
                }
            }
            case MOUNTAIN_WISHLISTED -> {
                if (requestDate.isBefore(LocalDate.now())) {
                    invalidationReason = "Date should be in the future";
                }
            }
        }

        if (invalidationReason != null) {
            invalidateRequest(invalidationReason, requestDate.toString());
        }

    }

    public void invalidateRequest(String invalidationReason, String invalidationParameter) {
        throw new IllegalArgumentException(String.format("%s: %s", invalidationReason, invalidationParameter));
    }
}
