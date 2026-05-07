package com.retreatreserve.infrastructure.adapter.out.persistence.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuestDetailsEmbeddable {
    
    @Column(name = "number_of_guests", nullable = false)
    private Integer numberOfGuests;
    
    @Column(name = "guest_name", length = 100)
    private String guestName;
    
    @Column(name = "guest_phone", length = 20)
    private String guestPhone;
}
