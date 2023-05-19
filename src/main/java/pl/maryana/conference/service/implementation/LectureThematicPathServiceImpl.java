package pl.maryana.conference.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.ThematicPath;
import pl.maryana.conference.repository.LectureThematicPathRepository;
import pl.maryana.conference.service.LectureThematicPathService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LectureThematicPathServiceImpl implements LectureThematicPathService {

    private final LectureThematicPathRepository lectureThematicPathRepository;

    @Autowired
    public LectureThematicPathServiceImpl(LectureThematicPathRepository lectureThematicPathRepository) {
        this.lectureThematicPathRepository = lectureThematicPathRepository;
    }

    @Override
    public List<Lecture> findAll() {
        return lectureThematicPathRepository.findAll();
    }

    @Override
    public Optional<Lecture> findById(long lectureId) {
        return lectureThematicPathRepository.findById(lectureId);
    }

    @Override
    public Set<ThematicPath> findAllThematicPaths() {
        return lectureThematicPathRepository.findAllThematicPaths();
    }
}
