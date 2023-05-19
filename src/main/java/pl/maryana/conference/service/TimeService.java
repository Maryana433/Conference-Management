package pl.maryana.conference.service;

import java.time.LocalDateTime;

public interface TimeService {
    boolean isExpired(LocalDateTime lectureLocalDateTime);
}
