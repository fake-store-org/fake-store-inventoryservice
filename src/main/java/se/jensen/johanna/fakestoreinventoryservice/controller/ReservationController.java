package se.jensen.johanna.fakestoreinventoryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestoreinventoryservice.dto.ReservationRequest;
import se.jensen.johanna.fakestoreinventoryservice.service.ReservationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  /**
   * Internal endpoint for order to reserve cart for order
   */
  @PostMapping("/reserve-cart")
  public ResponseEntity<Void> reserveCart(
      @RequestBody @Valid ReservationRequest request) {
    reservationService.reserveCart(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .build();
  }

}
