package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APITest
{
    @Test
    public void e2eApiTest()
    {
        HashMap<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "nikkikumar2017@gmail.com");
        loginPayload.put("password", "23MN@ak1");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse apiResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(apiResponse.ok());
        System.out.println(apiResponse.text());

        String token = JsonPath.read(apiResponse.text(), "$.token");
        System.out.println("Login Success : " + token);

        // Create Event
        Map<String,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title", "Playwright API Testing");
        createEventPayload.put("description", "I am Testing API");
        createEventPayload.put("category", "Conference");
        createEventPayload.put("venue", "fdgfdg");
        createEventPayload.put("city", "New York");
        createEventPayload.put("eventDate", "2026-10-10T15:52:00.000Z");
        createEventPayload.put("price", 100);
        createEventPayload.put("totalSeats", 100);


        APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization", "Bearer " + token).setData(createEventPayload));
        Assert.assertTrue(apiResponse.ok(), "Create Event Success");
        Integer eventId = JsonPath.read(eventResponse.text(), "$.data.id");
        System.out.println("Event Id : " + eventId);

        // Get Event
        APIResponse retriveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit","12").setHeader("Authorization", "Bearer " + token));
        List<Integer> allEventId = JsonPath.read(retriveEvents.text(), "$.data[*].id");
        Assert.assertTrue(allEventId.contains(eventId),"Created Event Should appear in the List");

        // Delete Event
        APIResponse deleteResponse = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer " + token));
        Assert.assertTrue((deleteResponse.ok()), "Event is Not deleted Sucessfully");

        // Verify Deletion is Success. -> GetEvents and Confirms that event id does not exist anymore

        APIResponse postDeletionAllEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create()
                        .setQueryParam("page", "1")
                        .setQueryParam("limit","12")
                        .setHeader("Authorization", "Bearer " + token));
        List<Integer> allEventIdPostDeletion = JsonPath.read(postDeletionAllEvents.text(), "$.data[*].id");
        System.out.println("allEventIdPostDeletion : " + allEventIdPostDeletion);
        Assert.assertFalse(allEventIdPostDeletion.contains(eventId),"Deleted Event is appearing in the List");

    }
}
