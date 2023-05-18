package pl.maryana.conference.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maryana.conference.exception.LectureNotFound;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.repository.LectureRepository;
import pl.maryana.conference.service.LectureService;

import java.util.List;
import java.util.Optional;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepository lectureRepository;

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Lecture> findById(long lectureId) {
        return lectureRepository.findById(lectureId);
    }
}
