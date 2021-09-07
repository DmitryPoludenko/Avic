package avic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.*;


public class AvicSmokeTests {

    private WebDriver driver;

    @BeforeTest
    public void profileSetUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    }

    @BeforeMethod
    public void testsSetUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://avic.ua/");
    }

    @Test(priority = 1)
    public void checkThatUrlContainsSearchWord() {
        driver.findElement(By.xpath("//input[@id='input_search']"))
                .sendKeys("iPhone 11");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']"))
                .click();
        driver.manage()
                .timeouts()
                .implicitlyWait(30, TimeUnit.SECONDS);
        assertTrue(driver.getCurrentUrl().contains("query=iPhone+11"));
    }

    @Test(priority = 2)
    public void checkElementsAmountOnSearchPage() {

        driver.findElement(By.xpath("//input[@id='input_search']"))
                .sendKeys("iPhone 11");
        driver.findElement(By.xpath("//button[@class='button-reset search-btn']"))
                .click();
        driver.manage()
                .timeouts()
                .implicitlyWait(30, TimeUnit.SECONDS);

        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));

        assertEquals(elements.size(), 12);

    }

    @Test(priority = 3)
    public void checkThatCatalogueIsWorking() {

        Actions action = new Actions(driver);
        driver.manage()
                .timeouts()
                .implicitlyWait(30, TimeUnit.SECONDS);
        WebElement webElement = driver.findElement(By.xpath("//a[@href='https://avic.ua/noutbuki-i-aksessuaryi']//span[@class='sidebar-item-title']"));
        action.moveToElement(webElement).pause(500)
                .moveToElement(driver.findElement(By.xpath("//ul[@class='sidebar-list']//a[@href='https://avic.ua/noutbuki']"))).pause(500)
                .moveToElement(driver.findElement(By.xpath("//ul[@class='sidebar-list']//a[@href='/noutbuki/proizvoditel--lenovo']"))).pause(500)
                .click().build().perform();
        assertTrue(driver.getCurrentUrl().contains("proizvoditel--lenovo"));
    }


    @Test(priority = 4)
    public void checkIfSearchResultByFilterContainsSearchWord() {
        Actions action = new Actions(driver);

        driver.manage()
                .timeouts()
                .implicitlyWait(30, TimeUnit.SECONDS);
        WebElement webElement = driver.findElement(By.xpath("//a[@href='https://avic.ua/girobordyi-i-giroskuteryi']//span[@class='sidebar-item-title']"));

        action.moveToElement(webElement).pause(500)
                .moveToElement(driver.findElement(By.xpath("//ul[@class='sidebar-list']//a[contains(text(), 'самокат')]"))).pause(500)
                .moveToElement(driver.findElement(By.xpath("//a[@href='/girobordyi-i-giroskuteryi/vid--elektrosamokat']//img"))).pause(500)
                .click().build().perform();

        driver.findElement(By.xpath("//label[@for='fltr-proizvoditel-xiaomi']")).click();

        List<WebElement> elements = driver.findElements(By.xpath("//div[@class='prod-cart__descr']"));

        for (WebElement element : elements) {
            assertTrue(element.getText().contains("Mi"));
        }

    }

    @Test(priority = 5)
    public void checkCorrectlyWorkingFilter() {

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//a[@href='https://avic.ua/apple-store']//span[@class='sidebar-item-title']")).click();
        driver.findElement(By.xpath("//div[@class='brand-box__info']//a[@href='https://avic.ua/iphone']")).click();

        driver.findElement(By.xpath("//label[@for='fltr-1']")).click();
        assertTrue(driver.getCurrentUrl().contains("available--on"));

        driver.findElement(By.xpath("//label[@for='fltr-seriya-iphone-11']")).click();
        assertTrue(driver.getCurrentUrl().contains("available--on_seriya--iphone-11"));
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

}
