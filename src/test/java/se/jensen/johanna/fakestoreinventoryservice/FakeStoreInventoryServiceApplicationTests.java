package se.jensen.johanna.fakestoreinventoryservice;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@SpringBootTest
@ActiveProfiles("test")
class FakeStoreInventoryServiceApplicationTests {

  @MockitoBean
  private SqsAsyncClient sqsAsyncClient;

  @MockitoBean
  private SqsTemplate sqsTemplate;

  @Test
  void contextLoads() {
  }

}
