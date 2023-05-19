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

    private final List<Lecture> lectures = new ArrayList<>();
    private final Set<ThematicPath> thematicPathSet = new HashSet<>();

    public LectureThematicPathRepository(@Value("${conference.lecture.number}") int lectureNumber, @Value("${conference.lecture.number}") int thematicPathNumber ){

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

            LocalDateTime startTime = LocalDateTime.of(dateOfConference, startTimeOfConference
                    .plusMinutes((long) (lectureOrder - 1) * (durationOfEachLecture + breakBetweenLectures)));

            LocalDateTime endTime = startTime.plusMinutes(durationOfEachLecture);

            ThematicPath thematicPath = new ThematicPath(i,String.format("Thematic Path %d", i));
            thematicPathSet.add(thematicPath);

            long lectureId = 3L *(lectureOrder - 1) + i;
            lectureList.add(new Lecture(lectureId, thematicPath,
                    String.format("Description of the lecture %d of thematic path %d",lectureOrder, i), startTime, endTime));
        }

        return lectureList;
    }

}
