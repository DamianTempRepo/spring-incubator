package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.service.BookingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<?> getBookingByCustomerId(@RequestBody Integer id) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> getBookingByReferenceNumber(@RequestBody Integer id) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}