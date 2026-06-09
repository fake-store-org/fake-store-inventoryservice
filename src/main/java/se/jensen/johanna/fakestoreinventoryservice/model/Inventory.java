package se.jensen.johanna.fakestoreinventoryservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class Inventory {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID inventoryId;

  @NotNull
  private UUID productId;
  @NotNull
  private Integer inStock;

  @Builder.Default
  @ColumnDefault("0")
  private Integer reserved = 0;

  private Instant createdAt;
  private Instant updatedAt;

  @Version
  private Long version;


  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  public void reserveStock(Integer quantity) {
    this.reserved += quantity;
  }

  public void releaseReservedStock(Integer quantity) {
    this.reserved -= quantity;
  }

  public void commitReservation(Integer quantity) {
    this.inStock -= quantity;
    this.reserved -= quantity;
  }

  public Boolean isAvailable(Integer requestedQuantity) {
    return this.inStock - this.reserved >= requestedQuantity;
  }

  public Integer availableQuantity() {
    return this.inStock - this.reserved;
  }


}
