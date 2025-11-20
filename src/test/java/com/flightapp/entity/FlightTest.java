package com.flightapp.entity;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FlightTest {

    @Test
    void dummy() {
        Flight flight = new Flight();
        assertNotNull(flight);
    }
}
