package pl.maryana.conference.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.maryana.conference.model.Lecture;
import pl.maryana.conference.service.MailService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
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

    }

    private String generateMessage(String sendTo, String login, Lecture lecture){
        return String.format("---------------------\n" +
                "Data : %s\n" +
                "Send to : %s\n"+
                "Message : %s\n",
                LocalDateTime.now(), sendTo, login + " have registered to lecture " + lecture);
    }
}
