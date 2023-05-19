package pl.maryana.conference.service.implementation;


import org.springframework.stereotype.Service;
import pl.maryana.conference.service.TimeService;

import java.time.LocalDateTime;

@Service
public class TimeServiceImpl implements TimeService {

    public boolean isExpired(LocalDateTime lectureLocalDateTime){
        return LocalDateTime.now().isAfter(lectureLocalDateTime);
    }
}
