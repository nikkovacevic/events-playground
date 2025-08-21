package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request extends PanacheEntityBase {

    @Id
    private UUID requestUuid;
    @CreationTimestamp
    private LocalDateTime created;
    @UpdateTimestamp
    private LocalDateTime updated;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @ElementCollection
    private List<String> mountains = new ArrayList<>();
    @Column(nullable = false)
    private LocalDate date;
    private String note;
    @Enumerated(EnumType.STRING)
    private RequestState requestState;
    private String invalidationReason;
    private Long processedMountainCount = 0L;

    public boolean isAllProcessed() {
        return processedMountainCount == mountains.size();
    }

    public void incrementProcessedCount() {
        if (this.processedMountainCount == null) {
            this.processedMountainCount = 0L;
        }
        this.processedMountainCount++;
    }

}
