package model;

import java.util.UUID;

public record DLQMessage(
        UUID uuid,
        String reason
) {
}
