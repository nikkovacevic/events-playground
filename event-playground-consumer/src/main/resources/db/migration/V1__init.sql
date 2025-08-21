-- Request table
CREATE TABLE request (
                         request_uuid UUID PRIMARY KEY,
                         created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         event_type VARCHAR(50),
                         date DATE NOT NULL,
                         note VARCHAR(255),
                         request_state VARCHAR(50),
                         invalidation_reason VARCHAR(255),
                         processed_mountain_count BIGINT DEFAULT 0
);

-- ElementCollection for mountains
CREATE TABLE request_mountains (
                                   request_request_uuid UUID NOT NULL,
                                   mountain VARCHAR(255) NOT NULL,
                                   CONSTRAINT fk_request_mountains FOREIGN KEY (request_request_uuid) REFERENCES request (request_uuid) ON DELETE CASCADE
);

CREATE INDEX idx_request_mountains_request_uuid ON request_mountains(request_request_uuid);

-- Mountain table
CREATE TABLE mountain (
                          uuid UUID PRIMARY KEY,
                          request_uuid UUID,
                          created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          name VARCHAR(255) NOT NULL,
                          elevation BIGINT,
                          coordinates VARCHAR(255),
                          region VARCHAR(255),
                          state VARCHAR(50),
                          CONSTRAINT fk_mountain_request FOREIGN KEY (request_uuid) REFERENCES request (request_uuid) ON DELETE CASCADE
);
