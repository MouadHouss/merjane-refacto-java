package com.nimbleways.springboilerplate.entities;

import com.nimbleways.springboilerplate.dto.product.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lead_time")
    private Integer leadTime;

    @Column(name = "available")
    private Integer available;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Column(name = "name")
    private String name;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "season_start_date")
    private LocalDate seasonStartDate;

    @Column(name = "season_end_date")
    private LocalDate seasonEndDate;



    public void decreaseStock() {
        if (available <= 0) {
            throw new IllegalStateException("Stock already empty");
        }
        this.available--;
    }

    public boolean isAvailable() {
        return available > 0;
    }

    public boolean isNotExpired() {
        return !LocalDate.now().isAfter(this.getExpiryDate());
    }

    public boolean isInSeason() {
        return LocalDate.now().isAfter(this.getSeasonStartDate()) && LocalDate.now().isBefore(this.getSeasonEndDate());
    }

    public void removeStock() {
        this.available = 0;
    }

    public boolean canBeDeliveredBeforeSeasonEnd() {
        return LocalDate.now().plusDays(this.leadTime).isBefore(this.getSeasonEndDate());
    }

    public boolean isSeasonNotStarted() {
        return LocalDate.now().isBefore(this.getSeasonStartDate());
    }

}
