package ru.netology.test;

import com.codeborne.selenide.Condition;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.sqlUtils.SqlUtils;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    SqlUtils mySql = new SqlUtils();

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldCheckLogin() throws SQLException {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getValidAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = SqlUtils.getVerificationCode();
        verificationPage.verify(verificationCode);
        $("h2[data-test-id='dashboard']").shouldBe(Condition.visible);
    }

    @Test
    void shouldCheckIfBlockedAfterLoginWithInvalidPassword() throws SQLException {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfoInvalidPassword();
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        loginPage.validLogin(authInfo);
        val statusSQL = mySql.getStatusFromDb();
        assertEquals("blocked", statusSQL);
    }

    @AfterAll
    static void close() throws SQLException {
        SqlUtils.cleanDb();
    }
}
