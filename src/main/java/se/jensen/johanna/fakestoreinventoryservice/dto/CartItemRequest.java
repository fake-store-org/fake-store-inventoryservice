package se.jensen.johanna.fakestoreinventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CartItemRequest(
    @NotNull(message = "Please provide a product id.")
    UUID productId,
    @NotNull(message = "Please provide a quantity.")
    Integer quantity
) {

}
