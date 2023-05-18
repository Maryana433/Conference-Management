package pl.maryana.conference.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.maryana.conference.model.Lecture;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto {

    private long id;
    private String thematicPath;
    private String description;
    private String startTime;
    private String endTime;

    public LectureDto(Lecture lecture){

        String dateTimePattern = "dd-MM-yyyy H:mm";

        this.id = lecture.getId();
        this.thematicPath = lecture.getThematicPath();
        this.description = lecture.getDescription();
        this.startTime = lecture.getStartDateTime().format(DateTimeFormatter.ofPattern(dateTimePattern));
        this.endTime = lecture.getStartDateTime().plusMinutes(lecture.getMinutDuration()).
                format(DateTimeFormatter.ofPattern(dateTimePattern));
    }
}
