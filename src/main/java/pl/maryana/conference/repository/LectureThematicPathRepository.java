package pl.maryana.conference.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.ThematicPath;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Repository
public class LectureThematicPathRepository {

    private List<Lecture> lectures = new ArrayList<>();
    private Set<ThematicPath> thematicPathSet = new HashSet<>();

    private final int lectureNumber;

    private final int thematicPathNumber;

    public LectureThematicPathRepository(@Value("${conference.lecture.number}") int lectureNumber, @Value("${conference.lecture.number}") int thematicPathNumber ){

        this.thematicPathNumber = thematicPathNumber;
        this.lectureNumber = lectureNumber;

        for(int i = 1; i<= lectureNumber; i++){
            this.lectures.addAll(generateAllLecturesByOrderAndNumberOfPaths(i, thematicPathNumber));
        }
    }

    public List<Lecture> findAll(){
        return this.lectures;
    }

    public Optional<Lecture> findById(long id){
        return this.lectures.stream().filter(l -> l.getId() == id).findFirst();
    }

    public Set<ThematicPath> findAllThematicPaths(){
        return this.thematicPathSet;
    }

    private List<Lecture> generateAllLecturesByOrderAndNumberOfPaths(int lectureOrder, int numberOfPaths){

        int yearOfConference = 2023;
        int monthOfConference = 6;
        int dayOfConference = 1;

        LocalDate dateOfConference = LocalDate.of(yearOfConference,monthOfConference, dayOfConference);
        LocalTime startTimeOfConference = LocalTime.of(10, 0);
        int durationOfEachLecture = 105;
        int breakBetweenLectures = 15;

        List<Lecture> lectureList = new ArrayList<>();
        for(int i=1; i <= numberOfPaths; i++){
            LocalDateTime startTime = LocalDateTime.of(dateOfConference, startTimeOfConference.plusMinutes((long) (lectureOrder - 1) * (durationOfEachLecture + breakBetweenLectures)));
            long lectureId = (lectureOrder-1)* 3L + i;

            ThematicPath thematicPath = new ThematicPath(i,String.format("Thematic Path %d", i));
            thematicPathSet.add(thematicPath);

            lectureList.add(new Lecture(lectureId, lectureOrder, thematicPath,
                    String.format("Description of the lecture %d of thematic path %d",lectureOrder, i), startTime, durationOfEachLecture));
        }

        return lectureList;
    }

}
