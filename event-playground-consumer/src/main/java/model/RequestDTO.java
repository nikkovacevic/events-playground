package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record RequestDTO(
        UUID uuid,
        LocalDateTime created,
        EventType eventName,
        String mountain,
        LocalDate date,
        String note
) {}
