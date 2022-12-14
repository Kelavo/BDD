package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final ElementsCollection depositButton = $$("[data-test-id=\"action-deposit\"]");
    private static final ElementsCollection cards = $$(".list__item div");
    private static final String balanceStart = "баланс: ";
    private static final String balanceFinish = " р.";

    public DashboardPage() {
        SelenideElement heading = $("[data-test-id=dashboard]");
        heading.shouldBe(visible);
    }

    public int getCardBalance(String id) {
        String cardBalance = cards.findBy(text(DataHelper.getCardInfo(id).getNumber().substring(16))).getText();
        return extractBalance(cardBalance);
    }

    private int extractBalance(String cardBalance) {
        val start = cardBalance.indexOf(balanceStart);
        val finish = cardBalance.indexOf(balanceFinish);
        val value = cardBalance.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public void getMoneyTransferFromFirstToSecond() {
        depositButton.last().click();
    }

    public void getMoneyTransferFromSecondToFirst() {
        depositButton.first().click();
    }
}

