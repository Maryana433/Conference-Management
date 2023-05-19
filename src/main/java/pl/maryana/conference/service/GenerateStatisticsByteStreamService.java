package pl.maryana.conference.service;

import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.ThematicPath;

import java.util.List;
import java.util.Set;


public interface GenerateStatisticsByteStreamService {

    byte[] generateLectureParticipationInfo(List<Lecture> lectureList);
    byte[] generateThematicPathParticipationInfo(Set<ThematicPath> thematicPaths);
}
