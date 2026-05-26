package se.jensen.johanna.fakestoreinventoryservice.dto;

import java.util.UUID;

public record ConfirmReservationEventDTO(
    UUID orderId
) {

}
