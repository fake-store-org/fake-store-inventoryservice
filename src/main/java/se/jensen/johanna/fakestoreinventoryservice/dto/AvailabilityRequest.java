package se.jensen.johanna.fakestoreinventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record AvailabilityRequest(
    @NotNull(message = "Please add items.")
    Set<CartItemRequest> cartItemRequests
) {

}
