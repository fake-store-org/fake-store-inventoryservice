package se.jensen.johanna.fakestoreinventoryservice.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Reservation {


  @Id
  @GeneratedValue
  private UUID reservationId;

  private UUID orderId;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "reservation_id")
  private List<ReservationItem> reservedItems;


  private Instant reservedAt;

  @NotNull
  private Instant expiresAt;

  @Version
  private Long version;

  @PrePersist
  protected void onCreate() {
    this.reservedAt = Instant.now();
  }

  public static Reservation reserve(List<ReservationItem> reservedItems, UUID orderId) {
    return Reservation.builder()
        .orderId(orderId)
        .reservedItems(reservedItems)
        .reservedAt(Instant.now())
        .expiresAt(Instant.now().plus(15, ChronoUnit.MINUTES))
        .build();

  }


}
