package pl.maryana.conference.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Lecture {

    private long id;
    private ThematicPath thematicPath;
    private String description;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Lecture(long id, ThematicPath thematicPath, String description, LocalDateTime startDateTime, LocalDateTime endDateTime ) {
        this.id = id;
        this.thematicPath = thematicPath;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }


    @Override
    public String toString() {
        return "Lecture{" +
                "thematicPath='" + thematicPath + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startDateTime +
                ", minutDuration=" + endDateTime +
                '}';
    }
}
