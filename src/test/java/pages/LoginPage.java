package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage
{
    Page page;
    String base_url;
    public LoginPage(Page page,String base_url)
    {
        this.page = page;
        this.base_url = base_url;
    }
    private static final String email_placeHolder = "Email";
    private static final String password_placeHolder = "Password";

    public DashboardPage loginToApplication()
    {
        return loginToApplication("nikkikumar2017@gmail.com", "23MN@ak1");
    }

    public DashboardPage loginToApplication(String email, String password)
    {
        page.navigate(base_url);
        System.out.println(page.title());
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByLabel(email_placeHolder).fill(email);
        page.getByLabel(password_placeHolder).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        DashboardPage dashboardPage = new DashboardPage(page);
        return dashboardPage ;
    }
}
