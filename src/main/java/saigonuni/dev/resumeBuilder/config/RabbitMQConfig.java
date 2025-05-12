package saigonuni.dev.resumeBuilder.config;

import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // Define the email queue
  @Bean
  public Queue emailQueue() {
    return QueueBuilder
      .durable("emailQueue")
      .withArgument("x-dead-letter-exchange", "email-dlx-exchange") // Forward failed messages to DLX
      .withArgument("x-dead-letter-routing-key", "email-dlx-routing-key")
      .withArgument("x-message-ttl", 60000) // Time to live for the messages in the original queue (optional)
      .build();
  }

  // Define the exchange for email queue
  @Bean
  public DirectExchange exchange() {
    return new DirectExchange("email-exchange");
  }

  // Binding email queue to exchange with routing key
  @Bean
  public Binding emailQueueBinding(Queue emailQueue, DirectExchange exchange) {
    return BindingBuilder
      .bind(emailQueue)
      .to(exchange)
      .with("email-routing-key");
  }

  // Define the Dead Letter Queue (DLQ)
  @Bean
  public Queue emailDLQ() {
    return new Queue("emailDLQ");
  }

  // Define the Dead Letter Exchange (DLX)
  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange("email-dlx-exchange");
  }

  // Binding DLQ to DLX with its routing key
  @Bean
  public Binding dlqBinding(Queue emailDLQ, DirectExchange deadLetterExchange) {
    return BindingBuilder
      .bind(emailDLQ)
      .to(deadLetterExchange)
      .with("email-dlx-routing-key");
  }

  //------------------------------- AI Service
  public static final String RAG_QUEUE_NAME = "ragQueue";

  @Bean
  public Queue ragQueue() {
    // Durable queue
    return new Queue(RAG_QUEUE_NAME, true);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // RabbitTemplate will be auto-configured by Spring Boot,
  // but if you want to explicitly set the message converter (good practice):
  @Bean
  public RabbitTemplate rabbitTemplate(
    ConnectionFactory connectionFactory,
    MessageConverter messageConverter
  ) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    // The reply-timeout can also be set here if not in properties
    // rabbitTemplate.setReplyTimeout(60000);
    return rabbitTemplate;
  }
}
