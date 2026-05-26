package se.jensen.johanna.fakestoreinventoryservice.messaging;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import se.jensen.johanna.fakestoreinventoryservice.dto.ConfirmReservationEventDTO;
import se.jensen.johanna.fakestoreinventoryservice.repository.ReservationRepository;
import se.jensen.johanna.fakestoreinventoryservice.service.ReservationService;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

  private final ReservationRepository reservationRepository;
  private final ReservationService reservationService;

  @SqsListener("${app.queues.confirm-reservation}")
  public void handleOrderConfirmation(ConfirmReservationEventDTO event) {
    log.info("Received order confirmation {}", event);
    if (!reservationRepository.existsByOrderId(event.orderId())) {
      log.warn("Reservation for order {} not found. Abort confirmation",
          event.orderId());
      return;
    }
    reservationService.confirmReservation(event.orderId());
  }

}
