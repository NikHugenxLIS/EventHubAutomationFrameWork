package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage
{
    private final Page page;

    public MyBookingsPage(Page page)
    {
        this.page = page;
    }

    public void open()
    {
        page.getByTestId("nav-bookings").click();
        assertThat(page).hasURL(Pattern.compile(".*/bookings/?"));
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

    public String openFirstBookingDetails()
    {
        Locator firstBooking = page.getByTestId("booking-card").first();
        assertThat(firstBooking).isVisible();
        String bookingId = firstBooking.getByTestId("booking-id").innerText();
        firstBooking.getByRole(AriaRole.LINK,
                new Locator.GetByRoleOptions().setName("View Details")).click();
        assertThat(page).hasURL(Pattern.compile(".*/bookings/[^/]+"));
        return bookingId;
    }

    public void verifyBookingWasRemoved(String bookingId)
    {
        Locator cancelledBooking = page.getByTestId("booking-card")
                .filter(new Locator.FilterOptions().setHasText(bookingId));
        assertThat(cancelledBooking).isHidden();
    }
}
