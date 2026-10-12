package tests;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

public class FrameWorkBuildDataDrivenTest extends TestBase
{
    @DataProvider(name = "eventBookingData")
    public Object[][] eventBookingData() throws IOException
    {
        return DataProviderUtil.getJsonDataToMap("src/test/resources/eventBookingData.json");

    }
    @Test(groups = {"framework"},dataProvider = "eventBookingData", description = "Create Event - Book that event and verify if it is Booked")
    public void DemoTest(HashMap<String, String> data)
    {
        // Login
        LoginPage loginPage = new LoginPage(page,base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        // Step 1 : Create Event from Admin Page.
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(
                data.get("titlePrefix"),
                data.get("description"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats")

        );
        // Step 2 - Find Newly Created Event in the Events Page.
        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goToEventsPage();
        eventsPage.waitForEventsToLoad();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int seatsNumberBeforeBooking = eventsPage.getSeatsCount(targetCard);
        BookingFormPage bookingFormPage  = eventsPage.proceedToBookingEvent(targetCard);
        // Book the Ticket for Event
        bookingFormPage.fillAndConfirm(data.get("fullName"),data.get("email"),data.get("phone"));
        // Capture booking reference
        String bookingRef = bookingFormPage.bookingRefNumber();
        // Verify booking history
        bookingFormPage.goToMyBookingPage();
        bookingFormPage.verifyBookingRefHistory(bookingRef);

        // Go back to Events
        eventsPage.goToEventsPage();
        int expectedSeatsAfterBooking = seatsNumberBeforeBooking - 10;

        // Get updated seat count
        int seatAfterBooking = eventsPage.waitForSeatsCount(data.get("titlePrefix"), expectedSeatsAfterBooking);
        // Verify reduction
        Assert.assertTrue(seatsNumberBeforeBooking > seatAfterBooking,"Seat count was not reduced after booking"
        + "Before: " + seatsNumberBeforeBooking +
                ", After: " + seatAfterBooking);

    }

}
