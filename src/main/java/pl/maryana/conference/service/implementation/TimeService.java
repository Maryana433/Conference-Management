package pl.maryana.conference.service.implementation;


import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeService {

    public boolean isExpired(LocalDateTime lectureLocalDateTime){
        return LocalDateTime.now().isAfter(lectureLocalDateTime);
    }
}
