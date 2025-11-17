package com.flightapp.entity;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "passenger", indexes = {
    @Index(name = "idx_booking_id", columnList = "booking_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passengerId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(nullable = false)
    private String passengerName;
    
    @Column(length = 10)
    private String gender;
    
    @Column
    private Integer age;
    
    @Column
    private LocalDate dateOfBirth;
    
    @Column(length = 100)
    private String email;
    
    @Column(length = 15)
    private String phoneNumber;
    
    @Column(length = 30)
    private String mealPreference;
    
    @Column(length = 10)
    private String seatNumber;
    
    @Column
    private Integer baggageAllowanceKg;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isActive == null) {
            this.isActive = true;
        }
        if (this.baggageAllowanceKg == null) {
            this.baggageAllowanceKg = 20;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
