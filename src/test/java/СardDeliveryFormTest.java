import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import utils.DataGenerator;

import java.time.Duration;


import static com.codeborne.selenide.Condition.appear;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class СardDeliveryFormTest {

    @BeforeAll
    static void setUpAllure() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1000x900";
        open("http://localhost:9999/");
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void happyPathWithoutReplanningMeeting() throws InterruptedException {
        int daysToAddFirstMeeting = 5;
        String firstMeetingDay = DataGenerator.generateData(daysToAddFirstMeeting, "dd.MM.yyyy");
        int daysToAddSecondMeeting = 7;
        String secondMeetingDay = DataGenerator.generateData(daysToAddSecondMeeting, "dd.MM.yyyy");
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");

        $x("//input[contains (@placeholder, 'Город' )]").setValue(validUser.getCity());
        $x("//* [contains(@placeholder , 'Дата встречи')]")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//*[contains(@placeholder , 'Дата встречи')]").setValue(firstMeetingDay);
        $("[data-test-id='name'] input").setValue(validUser.getName());
        $("[data-test-id='phone'] input").setValue(validUser.getPhone());
        $x("//label[contains(@data-test-id, 'agreement')]").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $x("//*[contains(@class,'notification__content')]")
                .shouldBe(visible, Duration.ofMillis(3000))
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDay));
        $x("//* [contains(@placeholder , 'Дата встречи')]")
                .sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $x("//*[contains(@placeholder , 'Дата встречи')]").setValue(secondMeetingDay);
        $x("//*[contains(text(), 'Запланировать')]").click();
        $x("//*[contains(text(),'Необходимо подтверждение')]").shouldBe(appear, Duration.ofSeconds(3));
        $x("//div[contains(text(),'У вас уже запланирована встреча на другую дату. Перепланировать?')]")
                .shouldBe(appear, Duration.ofSeconds(3));
        $x("//span[contains(text(),'Перепланировать')]").click();
        $x("//*[contains(@class,'notification__content')]")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDay),
                        Duration.ofSeconds(5)).shouldBe(visible);
    }

}
