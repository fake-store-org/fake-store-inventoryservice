package se.jensen.johanna.fakestoreinventoryservice.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    int status,
    String error,
    String errorCode,
    String message,
    String path,
    Map<String, String> fieldErrors
) {

}
