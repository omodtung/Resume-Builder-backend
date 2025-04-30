package saigonuni.dev.resumeBuilder.consumer;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import saigonuni.dev.resumeBuilder.domain.User;

import saigonuni.dev.resumeBuilder.repository.UserRepository ;
import saigonuni.dev.resumeBuilder.service.UserEmailService ;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "myapp.runDLQ.enabled", havingValue = "true")
@Slf4j
public class EmailDLQListener {

  private final UserRepository userRepository;
  private final UserEmailService userEmailService;
  private final RabbitTemplate rabbitTemplate;

  public EmailDLQListener(UserEmailService userEmailService, UserRepository userRepository,
      RabbitTemplate rabbitTemplate) {
    log.info("Init EmailDLQListener");  
    this.userEmailService = userEmailService;
    this.userRepository = userRepository;
    this.rabbitTemplate = rabbitTemplate;
  }

  @RabbitListener(queues = "emailDLQ", ackMode = "MANUAL")
  public void sendActivationEmail(String userIdStr, Channel channel, Message message) throws IOException {
    MessageProperties messageProperties = message.getMessageProperties();
    Map<String, Object> headers = messageProperties.getHeaders();
    var xDeath = messageProperties.getXDeathHeader();
    log.info("xDeath " + xDeath);
    log.info("headers " + headers);
    log.info("userIdStr " + userIdStr);

    try {
      User user = userRepository.findById(Long.parseLong(userIdStr))
      .orElseThrow(() -> new RuntimeException("Cannot find userId " + userIdStr));
      // User user = optionalUser.get();
      // if (user.getIsSentActivationCode()) {
      //   // handle deduplication
      //   log.info("User " + userIdStr + " already activated");
      //   channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
      //   return;
      // }
      userEmailService.sendUserActivationEmail(user);
      // user.setIsSentActivationCode(true);
      // userRepository.save(user);
      log.info("Saved isSentActivationCode");
      // channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
      log.info("ack message to queue");
    } catch (Exception ex) {
      log.error("reject", ex);
      channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
    }
  }
}
