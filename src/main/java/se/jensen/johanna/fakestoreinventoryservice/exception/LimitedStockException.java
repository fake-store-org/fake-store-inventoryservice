package se.jensen.johanna.fakestoreinventoryservice.exception;

public class LimitedStockException extends DomainException {

  public LimitedStockException(String message) {
    super(message, ErrorType.LIMITED_STOCK);
  }
}
