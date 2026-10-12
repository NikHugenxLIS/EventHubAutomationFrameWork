package tests;

import org.testng.annotations.Test;
import pages.BookingDetailsPage;
import pages.DashboardPage;
import pages.LoginPage;
import pages.MyBookingsPage;

public class EventHubCancelBookingTest extends TestBase
{
    @Test(groups = {"framework"}, description = "Cancel the first booking and verify it is removed")
    public void cancelFirstAvailableBooking()
    {
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();

        MyBookingsPage myBookingsPage = new MyBookingsPage(page);
        myBookingsPage.open();
        String bookingId = myBookingsPage.openFirstBookingDetails();

        BookingDetailsPage bookingDetailsPage = new BookingDetailsPage(page);
        bookingDetailsPage.verifyDetailsPageLoaded();
        bookingDetailsPage.cancelBooking();

        myBookingsPage.verifyBookingWasRemoved(bookingId);
    }
}
