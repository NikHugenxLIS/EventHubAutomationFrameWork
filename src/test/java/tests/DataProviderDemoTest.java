package tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;


public class DataProviderDemoTest
{
    @DataProvider(name = "basicData") // MultiDimensional Array of Objects
    public Object[][] basicData()
    {
        return new Object[][] {{"user1@yopmail.com","password1"},{"user2@yopmail.com","password2"}};
    }

    @Test(dataProvider = "basicData")
    public void testFillForm(String email, String password)
    {
        System.out.println(email);
        System.out.println(password);
    }

    @DataProvider(name = "hashMapData") // MultiDimensional Array of Objects
    public Object[][] hashMapData()
    {
        HashMap<String,String> user1 = new HashMap<>();
        user1.put("email", "user1@yopmail.com");
        user1.put("password", "password1");

        HashMap<String,String> user2 = new HashMap<>();
        user2.put("email","user2@yopmail.com");
        user2.put("password", "password2");

        return new Object[][] {{user1},{user2}};
    }

    @Test(dataProvider = "hashMapData")
    public void testWithHashMap(HashMap<String,String> data)
    {
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));

    }
    @DataProvider(name = "jsonData")
    public Object[][] jsonData() throws IOException
    {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testData_TC1.json");
        String jsonContent = new String(Files.readAllBytes(path));

        Type type = new TypeToken<List<HashMap<String, String>>>(){}.getType();
        List<HashMap<String, String>> list = new Gson().fromJson(jsonContent, type);

        Object[][] table = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++)
        {
            table[i][0] = list.get(i);
        }
        return table;
    }
    @Test(dataProvider = "jsonData")
    public void testJsonData(HashMap<String,String> data)
    {
        System.out.println(data.get("email"));
        System.out.println(data.get("password"));
    }
}
