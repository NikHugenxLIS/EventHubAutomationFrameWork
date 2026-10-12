package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage
{
    Page page;
    private static final String EVENT_TITLE_INPUT = "#event-title-input";
    private static final String EVENT_DESCRIPTION_INPUT = "#admin-event-form textarea";
    private static final String CITY_LABEL = "City";
    private static final String CATEGORY_LABLE = "Category";
    private static final String VENUE_LABEL = "Venue";
    private static final String EVENT_DATE_TIME = "Event Date & Time";
    private static final String PRICE_LABEL = "Price ($)";
    private static final String SEAT_LABEL = "Total Seats";
    public AdminEventsPage(Page page)
    {
           this.page=page;
    }

    public void goTo()
    {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK,new Locator.GetByRoleOptions().setName("Manage Events")).click();
    }

    public void createEvent(String title, String description, String city, String venue, String dateTime,
                            String price, String totalSeats)
    {
        page.locator(EVENT_TITLE_INPUT).fill(title);
        page.locator(EVENT_DESCRIPTION_INPUT).fill(description);
        page.getByLabel(CATEGORY_LABLE).selectOption("Concert");
        page.getByLabel(CITY_LABEL).fill(city);
        page.getByLabel(VENUE_LABEL).fill(venue);
        page.getByLabel(EVENT_DATE_TIME).fill(dateTime);
        page.getByLabel(PRICE_LABEL).fill(price);
        page.getByLabel(SEAT_LABEL).fill(totalSeats);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();
        //Event Created
        assertThat(page.getByText("Event created!")).isVisible(); // By Default Playwright assertion wait for 5 sec
    }
}
