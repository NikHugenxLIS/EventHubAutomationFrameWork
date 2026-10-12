package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage
{
   Page page;

   public BookingFormPage(Page page)
   {
       this.page = page;
   }
         private static final String FULL_NAME_PLACEHOLDER = "Your full name";
         private static final String CUSTOMER_EMAIL_ID = "customer-email";
         private static final String CUSTOMER_PHONE_ID = "#phone";
        public void fillAndConfirm(String fullName, String email, String phoneNumber)
        {
            Locator noOfSeat = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+"));
            for (int i = 1; i <10 ; i++)
            {
                noOfSeat.click();
            }
            page.getByPlaceholder(FULL_NAME_PLACEHOLDER).fill(fullName);
            page.getByTestId(CUSTOMER_EMAIL_ID).fill(email);
            page.locator(CUSTOMER_PHONE_ID).fill(phoneNumber);
            page.locator("#confirm-booking").click();
            assertThat(page.getByText("Booking Confirmed!")).isVisible();
        }
   public String bookingRefNumber()
   {
       return page.locator(".booking-ref").innerText();

   }
   public void goToMyBookingPage()
   {
       page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();
   }
   public void verifyBookingRefHistory(String bookingRef)
   {
       Locator bookingCard = page.locator("#booking-card");
       Locator targetBookingCard = bookingCard.filter(new Locator.FilterOptions().setHasText(bookingRef));
       assertThat(targetBookingCard).isVisible();
   }
}
