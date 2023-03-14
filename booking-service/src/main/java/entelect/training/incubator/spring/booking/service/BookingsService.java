package entelect.training.incubator.spring.booking.service;

import entelect.training.incubator.spring.booking.model.Booking;
import entelect.training.incubator.spring.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingsService {

    private final BookingRepository bookingRepository;

    public BookingsService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Integer id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        return bookingOptional.orElse(null);
    }
}
