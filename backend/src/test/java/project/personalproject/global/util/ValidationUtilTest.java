package project.personalproject.global.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void 이메일_확인_정상_값() {
        // given
        String email = "test@gmail.com";

        // when
        boolean result = ValidationUtil.isEmail(email);

        // then
        assertTrue(result);
    }

    @Test
    void 이메일_확인_비정상_값() {
        // given
        String email = "testgmail.com";

        // when
        boolean result = ValidationUtil.isEmail(email);

        // then
        assertFalse(result);
    }

}