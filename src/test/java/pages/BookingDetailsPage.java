package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingDetailsPage
{
    private final Page page;

    public BookingDetailsPage(Page page)
    {
        this.page = page;
    }

    public void verifyDetailsPageLoaded()
    {
        assertThat(page).hasURL(Pattern.compile(".*/bookings/[^/]+"));
        assertThat(page.getByRole(AriaRole.HEADING,
                new Page.GetByRoleOptions().setName("Event Details"))).isVisible();
    }

    public void cancelBooking()
    {
        page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Cancel Booking")).click();

        Locator confirmationDialog = page.getByRole(AriaRole.DIALOG);
        assertThat(confirmationDialog).isVisible();
        confirmationDialog.getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Yes, cancel it")).click();

        assertThat(page.getByText("Booking cancelled successfully")).isVisible();
    }
}
