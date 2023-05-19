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
    private int order;
    private ThematicPath thematicPath;
    private String description;

    private LocalDateTime startDateTime;
    private int minutDuration;

    public Lecture(long id, int order, ThematicPath thematicPath, String description, LocalDateTime startDateTime, int minutDuration ) {
        this.id = id;
        this.order = order;
        this.thematicPath = thematicPath;
        this.description = description;
        this.startDateTime = startDateTime;
        this.minutDuration = minutDuration;
    }


    @Override
    public String toString() {
        return "Lecture{" +
                "thematicPath='" + thematicPath + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startDateTime +
                ", minutDuration=" + minutDuration +
                '}';
    }
}
