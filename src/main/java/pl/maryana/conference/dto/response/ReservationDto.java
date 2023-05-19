package pl.maryana.conference.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.Reservation;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationDto {

    private long reservationId;
    private long lectureId;
    private String thematicPath;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy H:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy H:mm:ss")
    private LocalDateTime endTime;
    private String description;

    public ReservationDto(Reservation reservation){

        Lecture lecture = reservation.getLecture();

        this.reservationId = reservation.getId();
        this.lectureId = lecture.getId();
        this.thematicPath = lecture.getThematicPath().getName();
        this.startTime = lecture.getStartDateTime();
        this.endTime = lecture.getEndDateTime();
        this.description = lecture.getDescription();
    }


}
