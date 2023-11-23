package Assign;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;

public class CAWStudioUpdatedAssignment {
    private WebDriver driver;

    @BeforeTest
    public void setup() {
        // Browser launch
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
    }

    @Test
    public void TC1() {
        // Click on the "Table Data" summary element
        driver.findElement(By.xpath("//summary[.='Table Data']")).click();
        WebElement textField = driver.findElement(By.id("jsondata"));
        textField.clear();

        // JSON data to be entered
        String jsonData = "[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, " +
                "{\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, " +
                "{\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, " +
                "{\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, " +
                "{\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]";
        
        textField.sendKeys(jsonData);

        // Click "Refresh Table" button
        driver.findElement(By.xpath("//button[.='Refresh Table']")).click();

        // Parse the JSON data
        
        JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
        
        
        // Find the table and its rows
        WebElement table = driver.findElement(By.id("dynamictable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        // Iterate through rows and cells to compare with JSON data
        for (int i = 1; i < rows.size(); i++) { // Skip the header row
            List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
            JsonObject rowData = jsonArray.get(i - 1).getAsJsonObject();
            String name = rowData.get("name").getAsString();
            String age = rowData.get("age").getAsString();
            String gender = rowData.get("gender").getAsString();

            Assert.assertEquals(cells.get(0).getText(), name, "Name mismatch in row " + i);
            Assert.assertEquals(cells.get(1).getText(), age, "Age mismatch in row " + i);
            Assert.assertEquals(cells.get(2).getText(), gender, "Gender mismatch in row " + i);
        }
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }
}

