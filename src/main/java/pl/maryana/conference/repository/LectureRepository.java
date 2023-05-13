package pl.maryana.conference.repository;

import org.springframework.stereotype.Repository;
import pl.maryana.conference.model.Lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class LectureRepository {

    private final List<Lecture> lectures;

    public LectureRepository(){
        this.lectures = new ArrayList<>();
        Stream.of(generateAllLecturesByOrderAndNumberOfPaths(1,3),
                generateAllLecturesByOrderAndNumberOfPaths(2,3),
                generateAllLecturesByOrderAndNumberOfPaths(3,3)).forEach(lectures::addAll);
    }

    public List<Lecture> findAll(){
        return this.lectures;
    }

    public Optional<Lecture> findById(long id){
        return this.lectures.stream().filter(l -> l.getId() == id).findFirst();
    }

    private List<Lecture> generateAllLecturesByOrderAndNumberOfPaths(int lectureOrder, int numberOfPaths){
        List<Lecture> lectureList = new ArrayList<>();
        for(int i=1; i <= numberOfPaths; i++){
            lectureList.add(new Lecture((lectureOrder-1)* 3L + i, lectureOrder, String.format("Thematic Path %d", i),
                    String.format("Description of the lecture %d of thematic path %d",lectureOrder, i)));
        }
        return lectureList;
    }

}
