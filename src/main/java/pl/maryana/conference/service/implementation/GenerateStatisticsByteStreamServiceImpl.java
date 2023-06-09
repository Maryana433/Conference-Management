package pl.maryana.conference.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.maryana.conference.exception.OutputStreamException;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.model.ThematicPath;
import pl.maryana.conference.service.GenerateStatisticsByteStreamService;
import pl.maryana.conference.service.ReservationService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GenerateStatisticsByteStreamServiceImpl implements GenerateStatisticsByteStreamService {


    private final ReservationService reservationService;
    private final int lectureLimit;
    private final int numberOfLecturesOfPath;

    public GenerateStatisticsByteStreamServiceImpl(ReservationService reservationService, @Value("${conference.lecture.limit}") int limit,
                                                   @Value("${conference.lecture.number}") int numberOfLecturesOfPath) {
        this.reservationService = reservationService;
        this.lectureLimit = limit;
        this.numberOfLecturesOfPath = numberOfLecturesOfPath;
    }


    public byte[] generateLectureParticipationInfo( List<Lecture> lectureList){

        try(ByteArrayOutputStream stream  = new ByteArrayOutputStream()) {

            for (Lecture l : lectureList) {
                stream.write(("Thematic Path : " + l.getThematicPath().getName()).getBytes());
                stream.write(System.lineSeparator().getBytes());
                stream.write(("Description : " + l.getDescription()).getBytes());
                stream.write(System.lineSeparator().getBytes());
                stream.write(("Start time : " + l.getStartDateTime().toString()).getBytes());
                stream.write(System.lineSeparator().getBytes());
                double participationLecture = reservationService.numberOfReservationsOfLecture(l.getId())/(lectureLimit* 1.0f) * 100.0;
                stream.write((Math.round(participationLecture *100)/100.00+ "%").getBytes());
                stream.write(System.lineSeparator().getBytes());
                stream.write("------------------".getBytes());
                stream.write(System.lineSeparator().getBytes());
            }
            log.info("Statistics for lectures generated");
            return stream.toByteArray();
        }catch (IOException e){
                throw new OutputStreamException("Server error generate statistics");
        }
    }



    public byte[] generateThematicPathParticipationInfo(Set<ThematicPath> thematicPathSet){

        try(ByteArrayOutputStream stream  = new ByteArrayOutputStream()) {
            for (ThematicPath path: thematicPathSet) {

                stream.write(("Thematic Path : " + path.getName()).getBytes());
                stream.write(System.lineSeparator().getBytes());
                double participationPerPath = reservationService.numberOfReservationsOfThematicPath(path.getId())/(lectureLimit * numberOfLecturesOfPath * 1.0f) * 100.0;
                stream.write((Math.round(participationPerPath * 100)/100.00 +  "%").getBytes());
                stream.write(System.lineSeparator().getBytes());
                stream.write("------------------".getBytes());
                stream.write(System.lineSeparator().getBytes());

            }
            log.info("Statistics for thematic paths generated");
            return stream.toByteArray();

        }catch (IOException e){
            throw new OutputStreamException("Server error generate statistics");
        }
    }
}
