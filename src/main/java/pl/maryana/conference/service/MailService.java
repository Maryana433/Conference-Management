package pl.maryana.conference.service;

import pl.maryana.conference.model.Lecture;

public interface MailService {

    void sendEmail(String sendTo, String login, Lecture lecture);
}
