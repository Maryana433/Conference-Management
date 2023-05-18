package pl.maryana.conference.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.service.MailService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Value("${email.message.file-name}")
    private String fileName;

    @Override
    public void sendEmail(String sendTo, String login, Lecture lecture){

        try(FileWriter file = new FileWriter(fileName, true)){
            file.append(generateMessage(sendTo, login, lecture));
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Email was send to " + sendTo);

    }

    private String generateMessage(String sendTo, String login, Lecture lecture){
        return String.format("---------------------\n" +
                "Data : %s\n" +
                "Send to : %s\n"+
                "Message : %s\n",
                LocalDateTime.now(), sendTo, login + " have registered to lecture " + lecture);
    }
}
