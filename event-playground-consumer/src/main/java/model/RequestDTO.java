package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RequestDTO(
        UUID uuid,
        LocalDateTime created,
        EventType eventName,
        List<String> mountains,
        LocalDate date,
        String note
) {}
