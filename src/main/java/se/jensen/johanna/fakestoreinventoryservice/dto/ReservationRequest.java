package se.jensen.johanna.fakestoreinventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record ReservationRequest(
    @NotNull(message = "Please add items to cart.")
    Set<CartItemRequest> cartItemRequests,
    UUID orderId
) {

}
