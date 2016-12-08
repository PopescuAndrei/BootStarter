package ro.andrei.bootstarter.web.support;

import static ro.andrei.bootstarter.support.Constants.PASSWORD_MAX_LENGTH;
import static ro.andrei.bootstarter.support.Constants.PASSWORD_MIN_LENGTH;

import org.springframework.util.StringUtils;

public final class AuthenticationUtils {

    public static boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= PASSWORD_MIN_LENGTH &&
            password.length() <= PASSWORD_MAX_LENGTH);
    }
}
