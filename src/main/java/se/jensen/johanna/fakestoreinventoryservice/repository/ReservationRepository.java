package se.jensen.johanna.fakestoreinventoryservice.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.johanna.fakestoreinventoryservice.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

  @EntityGraph(attributePaths = "reservedItems")
  Optional<Reservation> findByReservationId(UUID reservationId);

  @EntityGraph(attributePaths = "reservedItems")
  Optional<Reservation> findByOrderId(UUID orderId);


  Boolean existsByOrderId(UUID orderId);

  @EntityGraph(attributePaths = "reservedItems")
  List<Reservation> findByExpiresAtBefore(Instant expiresAt);

}
