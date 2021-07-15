package Page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class Cash {
    public SelenideElement cashButton = $(withText("Купить"));
    public SelenideElement CashHeader = $(withText("Оплата по карте"));
    public SelenideElement creditCardButton = $(withText("Купить в кредит"));
    public SelenideElement creditHeader = $(withText("Кредит по данным карты"));
    public SelenideElement form = $("[class='form form_size_m form_theme_alfa-on-white']");
    public SelenideElement form2 = $$("[class='input-group input-group_width_available input-group_theme_alfa-on-white control-group' ]").last();
    public SelenideElement fieldNumber = form.$("[placeholder='0000 0000 0000 0000']");
    public SelenideElement fieldMonth = form.$("[placeholder='08']");
    public SelenideElement fieldYear = form.$("[placeholder='22']");
    public SelenideElement fieldCardHolder = form2.$("[class='input__control']");
    public SelenideElement fieldCardCode = form.$("[placeholder='999']");
    public SelenideElement proceedButton = $(withText("Продолжить"));
    public SelenideElement succeedNotification = $("[class='notification notification_visible notification_status_ok notification_has-closer notification_stick-to_right notification_theme_alfa-on-white']");
    public SelenideElement failedNotification = $("[class='notification notification_visible notification_status_error notification_has-closer notification_stick-to_right notification_theme_alfa-on-white']");
    public SelenideElement closeSucceedNotification = succeedNotification.$("[type='button']");
    public SelenideElement closeFailedNotification = failedNotification.$("[type='button']");

}

