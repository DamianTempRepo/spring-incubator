package entelect.training.incubator.spring.booking.controller;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.service.BookingsService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("bookings")
public class BookingsController {

    private final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);

    private final BookingsService bookingsService;

    private final RestTemplate restTemplate;

    public BookingsController(BookingsService bookingsService, RestTemplate restTemplate) {
        this.bookingsService = bookingsService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Booking booking) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        String plainCreds = "admin:is_a_lie";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            restTemplate.exchange("http://localhost:8201/customers/" + booking.getCustomerId(), HttpMethod.GET, entity, String.class);
            restTemplate.exchange("http://localhost:8202/flights/" + booking.getFlightId(), HttpMethod.GET, entity, String.class);

            final Booking savedBooking  = bookingsService.createBooking(booking);
            return new ResponseEntity<>(savedBooking, HttpStatus.OK);
        }
        catch (HttpClientErrorException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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