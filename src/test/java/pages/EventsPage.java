package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage
{
    Page page;
    public EventsPage(Page page)
    {
        this.page = page;
    }

    public void goToEventsPage()
    {
        page.locator("#nav-events").click();
    }
    public Locator waitForEventsToLoad()
    {
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        return eventCards;
    }
    public Locator findEventCard(String titleCard)
    {
        Locator eventCards = waitForEventsToLoad();
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
        assertThat(targetCard).isVisible();
        return targetCard;
    }
    public int getSeatsCount(Locator  targetCard)
    {
        //assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); // This on Particular Assertion level
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);
    }
    public BookingFormPage proceedToBookingEvent(Locator  targetCard)
    {
        targetCard.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }
    public int waitForSeatsCount(String eventTitle, int expectedSeats)
    {
        Locator targetCard = findEventCard(eventTitle);
        Locator seats = targetCard.getByText("seats");

        assertThat(seats).hasText(expectedSeats + " seats available");

        return getSeatsCount(targetCard);
    }
}
