package service;

import io.smallrye.common.annotation.Blocking;
import jakarta.el.BeanNameELResolver;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import model.DLQMessage;
import model.EventType;
import model.Mountain;
import model.MountainInfo;
import model.MountainState;
import model.MountainTask;
import model.Request;
import model.RequestRepository;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import java.util.UUID;
import java.util.concurrent.StructuredTaskScope;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
import static model.MountainState.CLIMBED;
import static model.MountainState.WISHLISTED;
import static model.RequestState.PROCESSED;

@ApplicationScoped
public class EnrichmentService {

    private static final Logger log = Logger.getLogger(EnrichmentService.class);

    @Inject
    ExternalMockService externalMockService;

    @Inject
    RequestRepository requestRepository;

    @Channel("mountain-enrichment-requested")
    Emitter<MountainTask> mountainTaskEmitter;

    @Incoming("request-validated")
    public void createMountainEntry(Request request) {
        log.infof("Step 3 - %s - Creating mountain entries: %s", request.getRequestUuid(), request);
        request.getMountains().forEach(mountain -> mountainTaskEmitter.send(new MountainTask(request.getRequestUuid(), mountain, request.getEventType())));
    }

    @Incoming("mountain-enrichment-requested")
    @Outgoing("mountain-enrichment-completed")
    public Mountain enrichMountain(MountainTask task) throws Exception {
        log.infof("Step 3a - %s - Creating mountain entry: %s", task.requestUuid(), task.name());

        MountainInfo scrapedData = scrapeAdditionalMountainData(task.name());

        final Mountain mountain = new Mountain();
        mountain.setUuid(UUID.randomUUID());
        mountain.setRequestUuid(task.requestUuid());
        mountain.setName(task.name());
        mountain.setState(getEventState(task.eventType()));
        mountain.setElevation(scrapedData.elevation());
        mountain.setCoordinates(scrapedData.coordinates());
        mountain.setRegion(scrapedData.region());

        return mountain;
    }

    @Incoming("mountain-enrichment-completed")
    @Outgoing("mountain-persisted")
    @Transactional(REQUIRES_NEW)
    public Mountain persistMountain(Mountain mountain) {
        log.infof("Step 3b - %s - Persisting mountain entry: %s", mountain.getRequestUuid(), mountain.getUuid());

        mountain.persist();
        return mountain;
    }

    @Incoming("mountain-persisted")
    @Transactional(REQUIRES_NEW)
    public Mountain collectMountains(Mountain mountain) {
        Request request = requestRepository.findByUuidPessimistic(mountain.getRequestUuid())
                .orElseThrow(() -> new IllegalStateException("Request not found in DB"));

        request.incrementProcessedCount();
        if (request.isAllProcessed()) {
            request.setRequestState(PROCESSED);
            log.infof("Step 4 - %s - All mountains processed. Request completed", mountain.getRequestUuid(), mountain.getUuid());
        } else {
            log.infof("Step 4 - %s - Processed mountain %s, waiting for others", mountain.getRequestUuid(), mountain.getName());
        }

        return mountain;
    }

    private MountainState getEventState(EventType eventType) {
        return switch (eventType) {
            case MOUNTAIN_CLIMBED -> CLIMBED;
            case MOUNTAIN_WISHLISTED -> WISHLISTED;
        };
    }

    private MountainInfo scrapeAdditionalMountainData(String mountainName) throws Exception {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            var elevationTask = scope.fork(() ->
                externalMockService.scrapeMountainElevation(mountainName).orElse(null)
            );
            var coordinatesTask = scope.fork(() ->
                externalMockService.scrapeMountainCoordinates(mountainName).orElse(null)
            );
            var regionTask = scope.fork(() ->
                externalMockService.scrapeMountainRegion(mountainName).orElse(null)
            );

            scope.join();
            scope.throwIfFailed();

            return new MountainInfo(
                    elevationTask.get(),
                    coordinatesTask.get(),
                    regionTask.get()
            );
        }
    }

}
