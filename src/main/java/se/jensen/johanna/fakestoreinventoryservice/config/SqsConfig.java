package se.jensen.johanna.fakestoreinventoryservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class SqsConfig {

  @Bean
  public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
      SqsAsyncClient sqsAsyncClient, ObjectMapper objectMapper) {
    SqsMessagingMessageConverter converter = new SqsMessagingMessageConverter();
    converter.setObjectMapper(objectMapper);
    converter.setPayloadTypeMapper(msg -> null);

    return SqsMessageListenerContainerFactory.builder()
        .sqsAsyncClient(sqsAsyncClient)
        .configure(options -> options.messageConverter(converter))
        .build();
  }

}
