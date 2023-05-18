package pl.maryana.conference.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.Reservation;
import pl.maryana.conference.model.User;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ReservationDto {

    private long reservationId;
    private long lectureId;
    private String thematicPath;
    private String startTime;
    private String endTime;

    public ReservationDto(Reservation reservation){

        String dateTimePattern = "dd-MM-yyyy H:mm";

        User user = reservation.getUser();
        Lecture lecture = reservation.getLecture();

        this.reservationId = reservation.getId();
        this.lectureId = lecture.getId();
        this.thematicPath = lecture.getThematicPath();
        this.startTime = lecture.getStartDateTime().format(DateTimeFormatter.ofPattern(dateTimePattern));
        this.endTime = lecture.getStartDateTime().plusMinutes(lecture.getMinutDuration()).
                format(DateTimeFormatter.ofPattern(dateTimePattern));
    }


}
