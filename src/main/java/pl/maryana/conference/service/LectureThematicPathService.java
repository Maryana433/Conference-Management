package pl.maryana.conference.service;

import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.ThematicPath;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LectureThematicPathService {

    List<Lecture> findAll();
    Optional<Lecture> findById(long lectureId);
    Set<ThematicPath> findAllThematicPaths();
}
