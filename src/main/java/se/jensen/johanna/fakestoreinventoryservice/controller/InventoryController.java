package se.jensen.johanna.fakestoreinventoryservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.jensen.johanna.fakestoreinventoryservice.dto.AvailabilityRequest;
import se.jensen.johanna.fakestoreinventoryservice.dto.AvailabilityResponse;
import se.jensen.johanna.fakestoreinventoryservice.service.ReservationService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

  private final ReservationService reservationService;

  @PostMapping("/check-stock")
  public ResponseEntity<AvailabilityResponse> checkAvailability(
      @RequestBody @Valid AvailabilityRequest request) {
    return ResponseEntity.ok()
        .body(reservationService.getCartAvailability(request.cartItemRequests()));
  }

}
