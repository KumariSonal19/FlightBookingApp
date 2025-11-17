package com.flightapp.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking", indexes = {
    @Index(name = "idx_pnr", columnList = "pnrNumber"),
    @Index(name = "idx_booking_status", columnList = "bookingStatus")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;
    
    @Column(unique = true, nullable = false)
    private String pnrNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    
    @Column(nullable = false)
    private Integer numberOfPassengers;
    
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;
    
    @Column(length = 20, nullable = false)
    private String bookingStatus = "CONFIRMED";
    
    @Column(nullable = false)
    private LocalDateTime bookingDate;
    
    @Column
    private LocalDate journeyDate;
    
    @Column
    private LocalDate returnDate;
    
    @Column(length = 20, nullable = false)
    private String tripType = "ONE_WAY";
    
    @Column
    private LocalDateTime cancellationDate;
    
    @Column(length = 500)
    private String cancellationReason;
    
    @Column(precision = 12, scale = 2)
    private BigDecimal refundAmount;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Passenger> passengers;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.bookingDate = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
