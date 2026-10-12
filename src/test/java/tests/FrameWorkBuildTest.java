package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameWorkBuildTest extends TestBase
{

    @Test( description = "Create Event - Book that event and verify if it is Booked")
    public void DemoTest()
    {
        String eventTitle = "Playwright FrameworkTest";
        // Login
        LoginPage loginPage = new LoginPage(page,base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        // Step 1 : Create Event from Admin Page.
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(
                eventTitle,
                "Playwright test Events",
                "Test City",
                "Test Venue",
                "2027-12-31T10:00",
                "100",
                "50"
        );
        // Step 2 - Find Newly Created Event in the Events Page.
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goToEventsPage();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard(eventTitle);
        int seatsNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage  = eventsPage.proceedToBookingEvent(targetCard);
        // Book the Ticket for Event
        bookingFormPage.fillAndConfirm("Test Student","test.student@example.com","9892345678");
        // Capture booking reference
        String bookingRef = bookingFormPage.bookingRefNumber();
        // Verify booking history
        bookingFormPage.goToMyBookingPage();
        bookingFormPage.verifyBookingRefHistory(bookingRef);

        // Go back to Events
        eventsPage.goToEventsPage();
        int expectedSeatsAfterBooking = seatsNumberBeforeBooking - 10;

        // Get updated seat count
        int seatAfterBooking = eventsPage.waitForSeatsCount(eventTitle, expectedSeatsAfterBooking);
        // Verify reduction
        Assert.assertTrue(seatsNumberBeforeBooking > seatAfterBooking,"Seat count was not reduced after booking"
        + "Before: " + seatsNumberBeforeBooking +
                ", After: " + seatAfterBooking);

    }

}
