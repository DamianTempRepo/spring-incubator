package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.service.BookingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customers")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingsService bookingsService;

    public BookingsController(BookingsService bookingsService) {
        this.bookingsService = bookingsService;
    }

    /*@PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Customer customer) {
        LOGGER.info("Processing customer creation request for customer={}", customer);

        final Customer savedCustomer = customersService.createCustomer(customer);

        LOGGER.trace("Customer created");
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }*/

}