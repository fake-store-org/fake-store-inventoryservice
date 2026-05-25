package se.jensen.johanna.fakestoreinventoryservice.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.johanna.fakestoreinventoryservice.dto.AvailabilityResponse;
import se.jensen.johanna.fakestoreinventoryservice.dto.CartItemRequest;
import se.jensen.johanna.fakestoreinventoryservice.dto.ReservationRequest;
import se.jensen.johanna.fakestoreinventoryservice.exception.LimitedStockException;
import se.jensen.johanna.fakestoreinventoryservice.model.Inventory;
import se.jensen.johanna.fakestoreinventoryservice.model.Reservation;
import se.jensen.johanna.fakestoreinventoryservice.model.ReservationItem;
import se.jensen.johanna.fakestoreinventoryservice.repository.InventoryRepository;
import se.jensen.johanna.fakestoreinventoryservice.repository.ReservationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

  private final InventoryRepository inventoryRepository;
  private final ReservationRepository reservationRepository;

  @Transactional
  public void reserveCart(ReservationRequest request) {
    log.info("Reserving cart {} for order {}...", request.cartItemRequests(), request.orderId());
    AvailabilityResponse response = getCartAvailability(request.cartItemRequests());
    if (!response.allAvailable()) {
      log.warn("Unable to reserve cart at checkout. Not all items are available. Updated cart: {}",
          response.updatedCart());
      throw new LimitedStockException("All items are not available.");
    }

    List<ReservationItem> reservationItems = request.cartItemRequests().stream()
        .map(cartItem -> ReservationItem.create(
            cartItem.productId(),
            cartItem.quantity()
        ))
        .toList();

    List<Inventory> inventory = fetchInventory(reservationItems);
    Map<UUID, Integer> toUpdate = buildQuantityMap(reservationItems);

    inventory.forEach(i -> i.reserveStock(toUpdate.get(i.getProductId())));

    Reservation reservation = Reservation.reserve(reservationItems, request.orderId());
    // cascade all children
    reservationRepository.save(reservation);
    inventoryRepository.saveAll(inventory);
    log.info("Successfully reserved cart for order {}. reservation id: {}",
        request.orderId(), reservation.getReservationId());
  }

  public AvailabilityResponse getCartAvailability(Set<CartItemRequest> cartItemRequests) {
    Set<UUID> productIds = cartItemRequests.stream()
        .map(CartItemRequest::productId)
        .collect(Collectors.toSet());

    List<Inventory> inventory = inventoryRepository.findByProductIdIn(productIds);
    Map<UUID, Integer> requestedCart = cartItemRequests.stream()
        .collect(Collectors.toMap(CartItemRequest::productId, CartItemRequest::quantity));
    Set<CartItemRequest> updatedCart = new HashSet<>();
    boolean allAvailable = true;
    for (Inventory i : inventory) {
      int requestedQuantity = requestedCart.get(i.getProductId());
      if (!i.isAvailable(requestedQuantity)) {
        allAvailable = false;
        int updatedQuantity = i.availableQuantity();
        updatedCart.add(new CartItemRequest(i.getProductId(), updatedQuantity));
      } else {
        updatedCart.add(new CartItemRequest(i.getProductId(), requestedQuantity));
      }
    }
    return new AvailabilityResponse(updatedCart, allAvailable);

  }


  /**
   * Confirms a reservation for paid order. Reduces stock and updates reserved amount. Reservation
   * is deleted so it's not caught by scheduler.
   */
  @Transactional
  public void confirmReservation(UUID orderId) {
    log.info("Committing reservation for order{}", orderId);
    Reservation reservation = reservationRepository.findByOrderId(orderId)
        .orElseThrow(() -> {
          log.error("Reservation for order {} not found when trying to commit reservation event",
              orderId);
          return new IllegalStateException("Reservation not found");
        });

    List<ReservationItem> items = reservation.getReservedItems();
    List<Inventory> inventory = fetchInventory(items);
    Map<UUID, Integer> toUpdate = buildQuantityMap(items);

    log.info("Updating inventory for: {}", toUpdate);

    inventory.forEach(i -> i.commitReservation(toUpdate.get(i.getProductId())));
    reservationRepository.delete(reservation);
    inventoryRepository.saveAll(inventory);
    log.info("Reservation {} committed.", orderId);
  }

  private List<Inventory> fetchInventory(List<ReservationItem> items) {
    Set<UUID> productIds = items.stream()
        .map(ReservationItem::getProductId)
        .collect(Collectors.toSet());
    return inventoryRepository.findByProductIdIn(productIds);
  }

  private Map<UUID, Integer> buildQuantityMap(List<ReservationItem> items) {
    return items.stream()
        .collect(Collectors.toMap(
            ReservationItem::getProductId,
            ReservationItem::getQuantity,
            Integer::sum
        ));
  }

  /**
   * Updates and deletes reservations that have been left unresolved
   */
  @Scheduled(fixedRate = 900000)
  @Transactional
  public void expireReservations() {
    log.info("Checking for expired reservations...");
    List<Reservation> expired = reservationRepository.findByExpiresAtBefore(Instant.now());
    if (expired.isEmpty()) {
      log.info("No expired reservations found at {}.", Instant.now());
      return;
    }
    log.info("Found {} expired reservations.", expired.size());

    Set<UUID> productIds = expired.stream()
        .flatMap(r -> r.getReservedItems().stream())
        .map(ReservationItem::getProductId)
        .collect(Collectors.toSet());
    List<Inventory> inventory = inventoryRepository.findByProductIdIn(productIds);
    Map<UUID, Integer> toRelease = expired.stream()
        .flatMap(r -> r.getReservedItems().stream())
        .collect(Collectors.toMap(
            ReservationItem::getProductId,
            ReservationItem::getQuantity,
            Integer::sum
        ));

    inventory.forEach(i -> i.releaseReservedStock(toRelease.get(i.getProductId())));
    reservationRepository.deleteAll(expired);
    inventoryRepository.saveAll(inventory);
    log.info("Expired reservations deleted. Updated: {}", toRelease);

  }


}
