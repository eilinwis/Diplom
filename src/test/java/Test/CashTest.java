package Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;


import Data.DataGenerator;
import Page.Cash;
import SQL.SqlHelperCash;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;


public class CashTest {
    Cash cash = new Cash();
    DataGenerator dataGenerator = new DataGenerator();


    @BeforeAll
    static void SetUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:8080");
        cash.cashButton.click();
    }


    @Test
    @DisplayName("Появление формы оплаты картой")
    public void fieldShouldAppear() {
        cash.form.should(appear);
        cash.CashHeader.should(appear);
    }

    @Test
    @DisplayName("Валидные данные")
    public void validInfo() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.succeedNotification.should(appear, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Невалидная карта")
    public void invalidCardNumber() {
        cash.fieldNumber.setValue(dataGenerator.getInValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.failedNotification.should(appear, Duration.ofSeconds(15));
    }

    @Test
    @DisplayName("Невалидный месяц")
    public void invalidMonth() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue("17");
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Неверно указан срок действия карты")).should(appear);

    }

    @Test
    @DisplayName("Невалидный год(больше)")
    public void invalidYearAbove() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue("63");
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Неверно указан срок действия карты")).should(appear);
    }

    @Test
    @DisplayName("Невалидный год(истёк)")
    public void invalidYearExpired() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue("11");
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Истёк срок действия карты")).should(appear);
    }

    @Test
    @DisplayName("Невалидный год(истёк в текущем году)")
    public void invalidYearExpire() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue("06");
        cash.fieldYear.setValue("21");
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Неверно указан срок действия карты")).should(appear);

    }

    @Test
    @DisplayName("Невалидное имя(Кириллица)")
    public void invalidNameCyrill() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue("Петр Иванов");
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Неверный формат имени")).should(appear);
    }

    @Test
    @DisplayName("Невалидное имя(Цифры)")
    public void invalidNameNumber() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue("123 456");
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        $(withText("Неверный формат имени")).should(appear);
    }

    @Test
    @DisplayName("Невалидный CVV")
    public void invalidCVV() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue("000");
        cash.proceedButton.click();
        $(withText("Неверный CVV")).should(appear);
    }

    @Test
    @DisplayName("Отправка пустой формы")
    public void emptyFields() {
        cash.proceedButton.click();
        cash.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
        cash.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Поле номера пустое")
    public void numberFieldIsEmpty() {
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Поле месяца пустое")
    public void monthFieldIsEmpty() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }


    @Test
    @DisplayName("Поле года пустое")
    public void yearFieldIsEmpty() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Поле имени пустое")
    public void nameFieldIsEmpty() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    @DisplayName("Поле CVV пустое")
    public void cvvFieldIsEmpty() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.proceedButton.click();
        cash.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Закрытие неуспешного уведомления")
    public void closeFailedNotification() {
        cash.fieldNumber.setValue(dataGenerator.getInValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.failedNotification.should(appear, Duration.ofSeconds(15));
        cash.closeFailedNotification.click();
        cash.failedNotification.should(disappear);
    }

    @Test
    @DisplayName("Закрытие успешного уведомления")
    public void closeSuccedNotification() {
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.succeedNotification.should(appear, Duration.ofSeconds(20));
        cash.closeSucceedNotification.click();
        cash.succeedNotification.should(disappear);
    }

    @Test
    @DisplayName("Отправка пустой формы, кроме CVV")
    public void emptyFieldsExceptCVV() {
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    @DisplayName("Отправка пустой формы, кроме CVV, имени")
    public void emptyFieldsExceptCVVAndName() {
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Отправка пустой формы, кроме CVV, имени, года")
    public void emptyFieldsExceptCVVNameYear() {
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Номер карты")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Отправка пустых полей имени и CVV")
    public void emptyFieldsNameAndCVV() {
        cash.fieldNumber.setValue(dataGenerator.getInValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
        cash.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Отправка пустых полей года,имени и CVV")
    public void emptyFieldsYearNameAndCVV() {
        cash.fieldNumber.setValue(dataGenerator.getInValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
        cash.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Ввод только валидного номера карты")
    public void onlyCardNumber() {
        cash.fieldNumber.setValue(dataGenerator.getInValidCardNumber());
        cash.proceedButton.click();
        cash.form.shouldHave(text("Месяц")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Год")).shouldHave(text("Неверный формат")).shouldBe(visible);
        cash.form.shouldHave(text("Владелец")).shouldHave(text("Поле обязательно для заполнения")).shouldBe(visible);
        cash.form.shouldHave(text("CVC/CVV")).shouldHave(text("Неверный формат")).shouldBe(visible);
    }

    @Test
    @DisplayName("Создание записи в таблице")
    void shouldCreateItem() {
        SqlHelperCash.cleanDefaultData();
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.succeedNotification.should(appear, Duration.ofSeconds(15));
        Assertions.assertEquals(SqlHelperCash.getCardIdOrder(), SqlHelperCash.getCardIdPayment());
    }

    @Test
    @DisplayName("Проверка статуса записи в таблице(Approved карта)")
    void shouldCheckStatusApproved() {
        SqlHelperCash.cleanDefaultData();
        cash.fieldNumber.setValue(dataGenerator.getValidCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.succeedNotification.should(appear, Duration.ofSeconds(15));
        Assertions.assertEquals("APPROVED", SqlHelperCash.getCardStatusApproved());
    }

    @Test
    @DisplayName("Проверка статуса записи в таблице(Declined карта)")
    void shouldCheckStatusDeclined() {
        SqlHelperCash.cleanDefaultData();
        cash.fieldNumber.setValue(dataGenerator.getDeclinedCardNumber());
        cash.fieldMonth.setValue(dataGenerator.getRandomMonth());
        cash.fieldYear.setValue(dataGenerator.getRandomYear());
        cash.fieldCardHolder.setValue(dataGenerator.generateCardHolderName());
        cash.fieldCardCode.setValue(dataGenerator.getCVV());
        cash.proceedButton.click();
        cash.succeedNotification.should(appear, Duration.ofSeconds(15));
        Assertions.assertEquals("DECLINED", SqlHelperCash.getCardStatusDeclined());
    }
}

