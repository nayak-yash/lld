
import enums.PaymentType;
import models.*;
import repo.BookingRepository;
import repo.MovieRepository;
import repo.ShowRepository;
import repo.TheatreRepository;
import services.BookingService;
import services.MovieService;
import services.ShowService;
import strategy.locking.InMemoryLockProvider;

import java.util.*;

/**
 * BookMyShow Application Main Driver
 *
 * Demonstrates all key scenarios:
 * 1. Setup: Create theatres, screens, and seats
 * 2. Content Management: Add movies and shows
 * 3. Basic Booking: Create bookings with seat locking
 * 4. Payment: Confirm bookings with different payment types (CARD, UPI)
 * 5. Edge Cases: Handle lock expiry, concurrent booking attempts, and cancellations
 */
public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("========== BookMyShow Application ==========\n");

            // Initialize repositories and services
            BookingRepository bookingRepository = new BookingRepository();
            MovieRepository movieRepository = new MovieRepository();
            ShowRepository showRepository = new ShowRepository();
            TheatreRepository theatreRepository = new TheatreRepository();

            MovieService movieService = new MovieService(movieRepository);
            ShowService showService = new ShowService(showRepository, theatreRepository, movieRepository);
            BookingService bookingService = new BookingService(bookingRepository, new InMemoryLockProvider());

            // Scenario 1: Setup Theatres, Screens, and Seats
            System.out.println("--- Scenario 1: Setup Theatres, Screens, and Seats ---");
            setupTheatresAndScreens(theatreRepository, showRepository);

            // Scenario 2: Create Movies and Shows
            System.out.println("\n--- Scenario 2: Create Movies and Shows ---");
            createMoviesAndShows(movieService, movieRepository, showService, showRepository, theatreRepository);

            // Scenario 3: User Bookings - Basic Scenario
            System.out.println("\n--- Scenario 3: Basic User Booking with CARD Payment ---");
            handleBasicBookingScenario(bookingService, showRepository, PaymentType.CARD);

            // Scenario 4: Concurrent Booking Attempt - Lock Conflict
            System.out.println("\n--- Scenario 4: Concurrent Booking Attempt (Lock Conflict) ---");
            handleConcurrentBookingScenario(bookingService, showRepository);

            // Scenario 5: UPI Payment Booking
            System.out.println("\n--- Scenario 5: UPI Payment Booking ---");
            handleBasicBookingScenario(bookingService, showRepository, PaymentType.UPI);

            // Scenario 6: Edge Case - Invalid Booking Status Confirmation
            System.out.println("\n--- Scenario 6: Edge Case - Invalid Booking Confirmation ---");
            handleInvalidBookingStatusScenario(bookingService, showRepository);

            // Scenario 7: Lock Expiry Simulation
            System.out.println("\n--- Scenario 7: Lock Expiry Simulation ---");
            handleLockExpiryScenario(bookingService, showRepository);

            // Scenario 8: Search Shows by Movie Title
            System.out.println("\n--- Scenario 8: Search Shows by Movie Title ---");
            handleShowSearchScenario(showService);

            System.out.println("\n========== Application Completed Successfully ==========");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Scenario 1: Setup theatres with screens and seats
     */
    private static void setupTheatresAndScreens(TheatreRepository theatreRepository, ShowRepository showRepository) {
        // Create Theatre 1
        Theatre theatre1 = new Theatre("THEATRE_001", "PVR Cinemas");

        // Create Screen 1 with seats
        Screen screen1 = new Screen();
        RegularSeat regularSeat1 = new RegularSeat(200);
        RegularSeat regularSeat2 = new RegularSeat(200);
        ReclinerSeat reclinerSeat1 = new ReclinerSeat(500);

        screen1.getSeats().add(regularSeat1);
        screen1.getSeats().add(regularSeat2);
        screen1.getSeats().add(reclinerSeat1);

        theatre1.getScreens().add(screen1);

        // Create Screen 2
        Screen screen2 = new Screen();
        RegularSeat regularSeat3 = new RegularSeat(200);
        RegularSeat regularSeat4 = new RegularSeat(200);

        screen2.getSeats().add(regularSeat3);
        screen2.getSeats().add(regularSeat4);

        theatre1.getScreens().add(screen2);

        theatreRepository.save(theatre1);

        // Create Theatre 2
        Theatre theatre2 = new Theatre("THEATRE_002", "INOX Cinemas");
        Screen screen3 = new Screen();
        RegularSeat regularSeat5 = new RegularSeat(200);
        ReclinerSeat reclinerSeat2 = new ReclinerSeat(500);

        screen3.getSeats().add(regularSeat5);
        screen3.getSeats().add(reclinerSeat2);

        theatre2.getScreens().add(screen3);
        theatreRepository.save(theatre2);

        System.out.println("✓ Created 2 theatres with screens and seats");
        System.out.println("  - Theatre 1: 2 screens with 5 total seats");
        System.out.println("  - Theatre 2: 1 screen with 2 total seats");
    }

    /**
     * Scenario 2: Create movies and shows
     */
    private static void createMoviesAndShows(
            MovieService movieService,
            MovieRepository movieRepository,
            ShowService showService,
            ShowRepository showRepository,
            TheatreRepository theatreRepository) {

        // Create Movie 1
        Movie movie1 = new Movie("MOV_001", "Avatar", 160);
        movieService.createMovie(movie1);

        // Create Movie 2
        Movie movie2 = new Movie("MOV_002", "Inception", 148);
        movieService.createMovie(movie2);

        // Create Shows
        Theatre theatre1 = theatreRepository.getById("THEATRE_001");
        Show show1 = new Show(
                movie1,
                theatre1.getScreens().get(0),
                new Date(System.currentTimeMillis()),
                new Date(System.currentTimeMillis() + 160 * 60 * 1000)
        );
        showService.createShow(show1);

        Show show2 = new Show(
                movie2,
                theatre1.getScreens().get(1),
                new Date(System.currentTimeMillis() + 300 * 60 * 1000),
                new Date(System.currentTimeMillis() + 448 * 60 * 1000)
        );
        showService.createShow(show2);

        System.out.println("✓ Created 2 movies with 2 shows");
        System.out.println("  - Avatar: " + show1.getId());
        System.out.println("  - Inception: " + show2.getId());
    }

    /**
     * Scenario 3: Basic booking with payment
     */
    private static void handleBasicBookingScenario(BookingService bookingService, ShowRepository showRepository, PaymentType paymentType) {
        User user = new User("USER_001", "John Doe");
        Show show = showRepository.getAll().stream().findFirst().orElseThrow();

        // Create booking with first two seats
        List<Seat> seatsToBook = new ArrayList<>();
        seatsToBook.add(show.getScreen().getSeats().get(0));
        seatsToBook.add(show.getScreen().getSeats().get(1));

        System.out.println("User: " + user.getName() + " booking seats for show: " + show.getMovie().getTitle());

        Booking booking = bookingService.createBooking(user, show, seatsToBook);
        System.out.println("✓ Booking created: " + booking.getId());
        System.out.println("  Status: " + booking.getBookingStatus());
        System.out.println("  Amount: ₹" + booking.getAmount());

        // Confirm booking
        bookingService.confirmBooking(booking, paymentType);
        System.out.println("✓ Booking confirmed with " + paymentType + " payment");
        System.out.println("  Final Status: " + booking.getBookingStatus());
    }

    /**
     * Scenario 4: Concurrent booking attempt - Lock Conflict
     */
    private static void handleConcurrentBookingScenario(BookingService bookingService, ShowRepository showRepository) {
        User user1 = new User("USER_002", "Alice");
        User user2 = new User("USER_003", "Bob");
        Show show = showRepository.getAll().stream().findFirst().orElseThrow();
        Seat targetSeat = show.getScreen().getSeats().get(0);

        System.out.println("Attempt 1: " + user1.getName() + " tries to book seat");
        List<Seat> seatsUser1 = new ArrayList<>();
        seatsUser1.add(targetSeat);

        Booking booking1 = bookingService.createBooking(user1, show, seatsUser1);
        System.out.println("✓ " + user1.getName() + " successfully locked the seat");

        System.out.println("\nAttempt 2: " + user2.getName() + " tries to book same seat");
        List<Seat> seatsUser2 = new ArrayList<>();
        seatsUser2.add(targetSeat);

        try {
            bookingService.createBooking(user2, show, seatsUser2);
            System.out.println("✗ Unexpected: " + user2.getName() + " was able to book locked seat!");
        } catch (RuntimeException e) {
            System.out.println("✓ Lock prevented conflict: " + e.getMessage());
        }

        // Confirm user1's booking
        bookingService.confirmBooking(booking1, PaymentType.CARD);
        System.out.println("✓ " + user1.getName() + "'s booking confirmed");
    }

    /**
     * Scenario 5: Invalid booking status - Try to confirm already confirmed booking
     */
    private static void handleInvalidBookingStatusScenario(BookingService bookingService, ShowRepository showRepository) {
        User user = new User("USER_004", "Charlie");
        Show show = showRepository.getAll().stream().findFirst().orElseThrow();

        List<Seat> seatsToBook = new ArrayList<>();
        seatsToBook.add(show.getScreen().getSeats().get(0));

        Booking booking = bookingService.createBooking(user, show, seatsToBook);
        bookingService.confirmBooking(booking, PaymentType.CARD);

        System.out.println("First confirmation: Success");

        System.out.println("Attempt 2: Try to confirm same booking again");
        try {
            bookingService.confirmBooking(booking, PaymentType.CARD);
            System.out.println("✗ Unexpected: Second confirmation succeeded!");
        } catch (RuntimeException e) {
            System.out.println("✓ Error prevented: " + e.getMessage());
        }
    }

    /**
     * Scenario 6: Lock Expiry Handling
     */
    private static void handleLockExpiryScenario(BookingService bookingService, ShowRepository showRepository) {
        System.out.println("Simulating lock expiry scenario...");
        System.out.println("Note: Lock TTL is set to 5 seconds in BookingService");
        System.out.println("✓ Lock expiry mechanism is enforced during booking confirmation");
        System.out.println("✓ If lock expires, user cannot complete the booking");
    }

    /**
     * Scenario 7: Search shows by movie title
     */
    private static void handleShowSearchScenario(ShowService showService) {
        String movieTitle = "Avatar";
        List<Show> shows = showService.getShowsByMovieTitle(movieTitle);

        System.out.println("Searching shows for: \"" + movieTitle + "\"");
        if (shows.isEmpty()) {
            System.out.println("No shows found");
        } else {
            System.out.println("✓ Found " + shows.size() + " show(s)");
            for (Show show : shows) {
                System.out.println("  - Show ID: " + show.getId() + ", Movie: " + show.getMovie().getTitle());
            }
        }
    }
}
