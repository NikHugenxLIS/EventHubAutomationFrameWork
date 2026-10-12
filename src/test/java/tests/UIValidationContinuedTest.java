package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UIValidationContinuedTest
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
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }
    @AfterMethod
    public void  tearDown()
    {
        // Stop tracing and export it into a zip archive.
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
    @Test(groups = {"smoke"})
    public void popupValidationTest()
    {
        Locator hiddenInputField = page.getByPlaceholder("Hide/Show Example");
        assertThat(hiddenInputField).isVisible();
        Locator hideTheField = page.locator("#hide-textbox");
        hideTheField.click();
        assertThat(hiddenInputField).isHidden();

    }
    @Test
    public void dialogValidationTest()
    {
        page.onceDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Alert")).click();
        page.waitForTimeout(5000);
    }
    @Test
    public void mouseHoverTest()
    {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.waitForTimeout(5000);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();

    }
    @Test
    public void iFrameValidationTest()
    {
        FrameLocator framepage = page.frameLocator("#courses-iframe");
        framepage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
       String textCheck = framepage.locator(".inner-box h1").textContent();
        System.out.println(textCheck);

    }
    /*
     How to Take Screenshots in Playwright --
     There are Two Level where we can Take
     1. Page Level
     2. On Locator level
     */
    @Test
    public void screenShotTest()
    {
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        Locator displayedEditBox = page.getByPlaceholder("Hide/Show Example");
        displayedEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("Editboxscreenshots.png")));
        Locator hideTheField = page.locator("#hide-textbox");
        hideTheField.click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagePostscreenshot.png")));

    }

}
