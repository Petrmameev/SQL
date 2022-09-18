package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static ru.netology.data.SQLHelper.clearDataBase;
import static com.codeborne.selenide.Selenide.open;

public class LoginTest {

    @AfterAll
    static void tearDown() {
        clearDataBase();
    }

    @Test
    void shouldSuccessLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());

    }

    @Test
    void shouldBeErrorWhenFailedLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo(DataHelper.getRandomUser().getLogin(), DataHelper.getAuthInfoWithTestData().getPassword());
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    void shouldBeErrorWhenFailedPassword() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfo);
        loginPage.getError();
    }

    @Test
    void shouldBlockWhenFailedPasswordThreeTime() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfo);
        loginPage.getError();
        loginPage.cleanField();
        var authInfo1 = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfo1);
        loginPage.getError();
        loginPage.cleanField();
        var authInfo2 = new DataHelper.AuthInfo(DataHelper.getAuthInfoWithTestData().getLogin(), DataHelper.getRandomUser().getPassword());
        loginPage.validLogin(authInfo2);
        loginPage.getBlockError();
    }

    @Test
    void shouldBeErrorWhenFailedVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfoWithTestData();
        var verificationPage = loginPage.validLogin(authInfo);
        verificationPage.verifyVerificationPageVisibility();
        var verificationCode = DataHelper.getRandomVerificationCode().getCode();
        verificationPage.validVerify(verificationCode);
        verificationPage.getError();
    }

}
