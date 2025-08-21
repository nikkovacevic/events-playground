package model;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;
import model.Request;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RequestRepository implements PanacheRepository<Request> {

    public Optional<Request> findByUuid(UUID uuid) {
        return find("requestUuid", uuid).firstResultOptional();
    }

    public Optional<Request> findByUuidPessimistic(UUID uuid) {
        return find("requestUuid", uuid)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResultOptional();
    }

}
