package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.service.BookingsService;
import entelect.training.incubator.spring.customer.model.Customer;
import entelect.training.incubator.spring.flight.model.Flight;
import entelect.training.incubator.spring.loyalty.server.RewardsServiceImpl;
import entelect.training.incubator.spring.notification.sms.client.impl.MoloCellSmsClient;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingsService bookingsService;

    private final RewardsServiceImpl rewardsService;

    private final MoloCellSmsClient moloCellSmsClient;

    private final RestTemplate restTemplate;

    public BookingsController(BookingsService bookingsService, RewardsServiceImpl rewardsService,
                              MoloCellSmsClient moloCellSmsClient, RestTemplate restTemplate) {
        this.bookingsService = bookingsService;
        this.rewardsService = rewardsService;
        this.moloCellSmsClient = moloCellSmsClient;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        HttpHeaders headers = setHttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Customer customer = null;

        try {
            customer = restTemplate.exchange("http://localhost:8201/customers/" + booking.getCustomerId(), HttpMethod.GET, entity, Customer.class).getBody();
            Flight flight = restTemplate.exchange("http://localhost:8202/flights/" + booking.getFlightId(), HttpMethod.GET, entity, Flight.class).getBody();
            final Booking savedBooking  = bookingsService.createBooking(booking);

            updateLoyaltyPoints(customer);
            enqueueNotification(customer, flight);

            return new ResponseEntity<>(savedBooking, HttpStatus.OK);
        }
        catch (HttpClientErrorException e) {

            if (customer == null) {
                LOGGER.warn("Failed to find customer with id={}. Error status code={}", booking.getCustomerId(), e.getStatusCode());
            } else {
                LOGGER.warn("Failed to find flight with id={}. Error status code={}", booking.getFlightId(), e.getStatusCode());
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Integer id) {

        LOGGER.info("Processing booking search request for booking id={}", id);
        Booking booking = this.bookingsService.getBookingById(id);

        if (booking != null) {
            LOGGER.trace("Found booking");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }

        LOGGER.trace("Booking not found");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("search")
    public ResponseEntity<?> getBookingByCustomerId(@RequestBody Integer id) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("search")
    public ResponseEntity<?> getBookingByReferenceNumber(@RequestBody Integer id) {

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    private HttpHeaders setHttpHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String plainCreds = "admin:pass";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);

        return headers;
    }

    private void updateLoyaltyPoints(Customer customer) {

        BigDecimal balance = rewardsService.getBalance(customer.getPassportNumber());
        balance = balance.add(BigDecimal.valueOf(100));
        rewardsService.updateBalance(customer.getPassportNumber(), balance);
    }

    private void enqueueNotification(Customer customer, Flight flight) {
        moloCellSmsClient.enqueueMessage(customer.getPhoneNumber() +  ",Molo Air: Confirming flight " + flight.getFlightNumber() +
                " booked for " + customer.getFirstName() + " " + customer.getFirstName() + " on " + flight.getDepartureTime() + ".");
    }
}