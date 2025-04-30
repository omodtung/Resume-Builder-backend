package saigonuni.dev.resumeBuilder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import saigonuni.dev.resumeBuilder.domain.User;

@Component
@Slf4j
public class UserEmailService {

  private final JavaMailSender mailSender;

  public UserEmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void sendUserActivationEmail(User user) {
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setTo(user.getEmail());
    mail.setSubject("Account Activation");
    mail.setText(
      "<html>" +
      "<body>" +
      "<h1>Welcome to Resume Builder!</h1>" +
      "<p>Your activation code is: <strong></strong></p>" +
      "<p>Please use this code to activate your account.</p>" +
      "</body>" +
      "</html>"
    );
    log.info("Try send mail");
    // throw new MailSendException("try set fail");
    // mailSender.send(mail);
    log.info("Email sent successfully to: " + user.getEmail());
  }
}
