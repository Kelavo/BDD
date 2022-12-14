package ru.netology.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.*;

public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldBeTransferredFromTheFirstCardToTheSecondCard() {
        var dashboardPage = new DashboardPage();

        int expected2 = dashboardPage.getCardBalance("2") + 5000;
        int expected1 = dashboardPage.getCardBalance("1") - 5000;

        dashboardPage.getMoneyTransferFromFirstToSecond();
        var moneyTransferPage = new TransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("1"), "5000");

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldBeTransferredFromTheSecondCardToTheFirstCard() {
        var dashboardPage = new DashboardPage();

        int expected1 = dashboardPage.getCardBalance("1") + 1000;
        int expected2 = dashboardPage.getCardBalance("2") - 1000;

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new TransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("2"), "1000");

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldNotBePassedINoAccountIsSpecified() {
        var dashboardPage = new DashboardPage();

        dashboardPage.getMoneyTransferFromSecondToFirst();
        var moneyTransferPage = new TransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo(""), "1000");

        moneyTransferPage.getError();
    }

    @Test
    void shouldNotTransferMoreThanTheBalance() {
        var dashboardPage = new DashboardPage();
        String balance = String.valueOf(dashboardPage.getCardBalance("1") + 100);

        int expected1 = dashboardPage.getCardBalance("1");
        int expected2 = dashboardPage.getCardBalance("2");

        dashboardPage.getMoneyTransferFromFirstToSecond();
        var moneyTransferPage = new TransferPage();
        moneyTransferPage.moneyTransfer(DataHelper.getCardInfo("1"), balance);
        moneyTransferPage.getError();

        int actual1 = dashboardPage.getCardBalance("1");
        int actual2 = dashboardPage.getCardBalance("2");

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

}