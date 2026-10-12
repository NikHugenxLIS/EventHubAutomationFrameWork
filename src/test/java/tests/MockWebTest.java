package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest
{
    Page page;
    Browser browser;
    Playwright playwright;
    @BeforeMethod
    public void setUp()
    {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); // Global Assertion time out is 7 seconds

    }
    @Test(description = "Sand Box banner is shown when 6 events are returned")
    public void DemoTest()
    {
        page.getByLabel("Email").fill("nikkikumar2017@gmail.com");
        page.getByLabel("Password").fill("23MN@ak1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        Locator eventButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(eventButton).isVisible();
        // Step 1 : Create Event from Admin Page.
        // in Action - Playwright by default time out is 10 Sec.

        page.route("**/api/events**",route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_6.json"))

        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();
        Assert.assertEquals(eventCards.count(),6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        page.route("**/api/events**",route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_4.json"))
        ));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        Locator eventCards1 = page.getByTestId("event-card");
        assertThat(eventCards1.first()).isVisible();
        Assert.assertEquals(eventCards1.count(),4);
        assertThat(page.locator(".mx-1").first()).isHidden();

        /*
        Normal Flow :-
        Browser calls API with Request -> API sends Response -> Response Renders on Front End
        --------------------------------------------------------
        route.fulfill();
        Browser calls API with Request -> API Sends the Response -> Modify response with (route.fulfill()) ->
        Modifies response renders on Front end
        ---------------------------------------------------------------
        route.resume()
        Browser calls API with Request -> Request is modified with route.resume() -> API Sends Response-> Modifies response
        render on Front end.
         */

    }
    @Test
    public void routeResumeTest()
    {
        page.getByLabel("Email").fill("nikkikumar@yopmail.com");
        page.getByLabel("Password").fill("23MN@ak1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        Locator eventButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"));
        assertThat(eventButton).isVisible();
        page.getByTestId("nav-bookings").click();
        page.route("**/api/events**", route -> route.resume(
                new Route.ResumeOptions().setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/17218")
        ));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();
        assertThat(page.getByText("Access Denied")).isVisible();
    }
}
