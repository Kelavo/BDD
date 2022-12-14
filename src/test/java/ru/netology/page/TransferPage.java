package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.ownText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {

    private final SelenideElement amount = $("[data-test-id=amount] input");
    private final SelenideElement fromField = $("[data-test-id=from] input");
    private final SelenideElement transfer = $("[data-test-id=action-transfer]");
    private final SelenideElement error = $("[data-test-id=error-notification]");

    public void moneyTransfer(DataHelper.CardInfo from, String amountToTransfer) {
        amount.setValue(amountToTransfer);
        fromField.setValue(from.getNumber());
        transfer.click();
        new DashboardPage();
    }

    public void getError() {
        error.shouldBe(Condition.visible);
    }

}