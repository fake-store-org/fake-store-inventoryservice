package se.jensen.johanna.fakestoreinventoryservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
  LIMITED_STOCK(HttpStatus.CONFLICT),
  ILLEGAL_STATE(HttpStatus.INTERNAL_SERVER_ERROR),
  NOT_FOUND(HttpStatus.NOT_FOUND);

  private final HttpStatus status;

  ErrorType(HttpStatus status) {
    this.status = status;
  }
}
