package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest
{
    Page page;
    Browser browser;
    Playwright playwright;
    // Invoke Browser -> Invoke Tab -> Type Url
    // Head Mode and Headless Mode
    // By Default Playwright run in Headless Mode.

    @BeforeMethod
    public void setUp()
    {
       playwright = Playwright.create();
       browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
       page = browser.newPage();
       page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); // Global Assertion time out is 7 seconds

    }
    @Test(description = "Create Event - Book that event and verify if it is Booked")
    public void DemoTest()
    {
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel("Email").fill("nikkikumar2017@gmail.com");
        page.getByLabel("Password").fill("23MN@ak1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        Locator eventButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(eventButton).isVisible();
        // Step 1 : Create Event from Admin Page.
        // in Action - Playwright by default time out is 10 Sec.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.NAVIGATION).getByRole(AriaRole.LINK,new Locator.GetByRoleOptions().setName("Manage Events")).click();
        page.locator("#event-title-input").fill("QA Summit");
        page.locator("#admin-event-form textarea").fill("This Event is created for Automation Purpose");
        page.getByLabel("Category").selectOption("Concert");
        page.getByLabel("City").fill("Ludhiana");
        page.getByLabel("Venue").fill("Model Town");
        page.getByLabel("Event Date & Time").fill("2026-09-30T07:08");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("300");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click();
        //Event Created
        assertThat(page.getByText("Event created!")).isVisible(); // By Default Playwright assertion wait for 5 sec

        // Step 2 - Find Newly Created Event in the Events Page.
        page.locator("#nav-events").click();
        Locator eventCards = page.getByTestId("event-card");
        //Visibility of Card which we have added
        Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("QA Summit"));
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); // This on Particular Assertion level
        String seatsText = targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        int seatsNumberBeforeBooking = Integer.parseInt(seatsText.split(" ")[0]);
        targetCard.getByTestId("book-now-btn").click();

        // Book the Ticket for Event
        Locator noOfSeat = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+"));
        for (int i = 1; i <10 ; i++)
        {
            noOfSeat.click();
        }
        page.getByPlaceholder("Your full name").fill("Nikki Kumar");
        page.getByTestId("customer-email").fill("nikkikumar2017@gmail.com");
        page.locator("#phone").fill("9891125012");
        page.locator("#confirm-booking").click();
        assertThat(page.getByText("Booking Confirmed!")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

         // Verify Booking History
        Locator bookingCard = page.locator("#booking-card");
        Locator targetBookingCard = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetBookingCard).isVisible();

        //Seat Count reduction Check
        page.locator("#nav-events").click();
        page.waitForTimeout(2000);
        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        //Visibility of Card which we have added
        Locator targetCardAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("QA Summit"));
        String seatsTextAfterBooking = targetCardAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);

        // AfterBooking < BeforeBooking
         int seatNumberAfterBooking = Integer.parseInt(seatsTextAfterBooking.split(" ")[0]);
         Assert.assertTrue(seatsNumberBeforeBooking >  seatNumberAfterBooking);


    }


}
