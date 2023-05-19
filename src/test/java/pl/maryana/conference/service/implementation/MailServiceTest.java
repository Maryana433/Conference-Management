package pl.maryana.conference.service.implementation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.repository.LectureThematicPathRepository;
import pl.maryana.conference.service.MailService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {MailServiceImpl.class, LectureThematicPathRepository.class})
public class MailServiceTest {

    @Autowired
    private MailService mailService;

    @Autowired
    private LectureThematicPathRepository lectureThematicPathRepository;

    @Value("${email.message.file-name}")
    private String fileName;


    @Test
    public void shouldAppendIntoFileInformationAboutMessages() throws IOException {

        // given
        long lectureId1 = 1L;
        Lecture lecture1 = lectureThematicPathRepository.findById(lectureId1).get();
        String sendTo1 = "maryanamartyniuk@gmail.com";
        String login1 = "login";

        long lectureId2 = 2L;
        Lecture lecture2 = lectureThematicPathRepository.findById(lectureId2).get();
        String sendTo2 = "maryanamartyniuk2@gmail.com";
        String login2 = "login2";

        //then
        mailService.sendEmail(sendTo1, login1, lecture1);
        mailService.sendEmail(sendTo2, login2, lecture2);

        //when
        String text = Files.readString(Paths.get(fileName));
        assertTrue(text.contains(login1));
        assertTrue(text.contains(sendTo1));
        assertTrue(text.contains(lecture1.toString()));

        assertTrue(text.contains(login2));
        assertTrue(text.contains(sendTo2));
        assertTrue(text.contains(lecture2.toString()));
    }


    @AfterEach
    void removeFile() {
        File file = new File(String.valueOf(Paths.get(fileName)));
        file.delete();
    }

}