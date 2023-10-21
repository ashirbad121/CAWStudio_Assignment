package Assign;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CAWStudioAssignment {
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

        // Create a list of expected data
        List<Map<String, String>> expectedData = new ArrayList<>();
        expectedData.add(createPerson("Bob", "20", "male"));
        expectedData.add(createPerson("George", "42", "male"));
        expectedData.add(createPerson("Sara", "42", "female"));
        expectedData.add(createPerson("Conor", "40", "male"));
        expectedData.add(createPerson("Jennifer", "42", "female"));

        // Find the table and its rows
        WebElement table = driver.findElement(By.id("dynamictable"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));

        // Iterate through rows and cells to compare with expected data
        for (int i = 1; i < rows.size(); i++) { // Skip the header row
            List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));

            for (int j = 0; j < cells.size(); j++) {
                String cellText = cells.get(j).getText();
                String expectedValue = expectedData.get(i - 1).get(getHeader(j));
                Assert.assertEquals(cellText, expectedValue, "Data mismatch in row " + i + " and column " + j);
            }
        }
    }

    @AfterTest
    public void teardown() {
        driver.quit();
    }

    private Map<String, String> createPerson(String name, String age, String gender) {
        Map<String, String> person = new HashMap<>();
        person.put("name", name);
        person.put("age", age);
        person.put("gender", gender);
        return person;
    }

    private String getHeader(int columnIndex) {
        // Map column indices to headers
        switch (columnIndex) {
            case 0:
                return "name";
            case 1:
                return "age";
            case 2:
                return "gender";
            default:
                return "";
        }
    }
}
