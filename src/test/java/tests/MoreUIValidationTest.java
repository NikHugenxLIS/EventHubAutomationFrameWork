package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class MoreUIValidationTest
{
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    @BeforeMethod(alwaysRun = true)
    public void setUp()
    {
        playwright = Playwright.create();
           browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
           // Chrome browser - incognito , browser
        context = browser.newContext();
        // Start tracing before creating / navigating a page.
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }
    @AfterMethod
    public void  tearDown()
    {
       // Stop tracing and export it into a zip archive.
            context.tracing().stop(new Tracing.StopOptions()
                    .setPath(Paths.get("trace.zip")));
    }
    @Test(groups = {"smoke"})
    public void ChildWindowHandleTest()
    {
        Locator blinkingText = page.locator(".blinkingText");
        Page newPage = context.waitForPage(() -> blinkingText.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
        //System.out.println("emailId: " + emailId);
        page.bringToFront();
        page.getByLabel("username").fill(emailId);
        System.out.println("username: " + page.getByLabel("username").inputValue());
        page.waitForTimeout(3000);
    }
    @Test
    public void radioButtonTest()
    {
        Locator userRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("user"));
        userRdBtn.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        //Verify that is Radio Button is Clicked/Selected or Not
        Assert.assertTrue(userRdBtn.isChecked());
        Locator checkBoxTerm = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the "));
        checkBoxTerm.click();
        Assert.assertTrue(userRdBtn.isChecked());
        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher"); // Dropdown with Combobox
        page.waitForTimeout(3000);

    }
}
