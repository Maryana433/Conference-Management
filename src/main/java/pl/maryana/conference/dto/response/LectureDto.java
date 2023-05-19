package pl.maryana.conference.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import pl.maryana.conference.model.Lecture;

import java.time.LocalDateTime;

@Getter
@Setter
public class LectureDto {

    private long id;
    private String thematicPath;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy H:mm")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy H:mm")
    private LocalDateTime endTime;

    public LectureDto(Lecture lecture){

        String dateTimePattern = "dd-MM-yyyy H:mm";

        this.id = lecture.getId();
        this.thematicPath = lecture.getThematicPath().getName();
        this.description = lecture.getDescription();
        this.startTime = lecture.getStartDateTime();
        this.endTime = lecture.getEndDateTime();
    }
}
