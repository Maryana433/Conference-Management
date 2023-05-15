package pl.maryana.conference.service;

import pl.maryana.conference.model.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {

    List<Lecture> findAll();
    Optional<Lecture> findById(long lectureId);
}
