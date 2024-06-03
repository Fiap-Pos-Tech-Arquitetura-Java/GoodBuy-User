package br.com.fiap.postech.goodbuy.user.helper;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.entity.enums.UserRole;
import br.com.fiap.postech.goodbuy.user.security.JwtService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserHelper {
    public static User getUser(boolean geraId) {
        return getUser(geraId, UserRole.ADMIN);
    }

    public static User getUser(boolean geraId, UserRole userRole) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        var user = new User(
                "anderson.wagner",
                "Anderson Wagner",
                "25310413030",
                encoder.encode("123456"),
                userRole
        );
        if (geraId) {
            user.setId(UUID.randomUUID());
        }
        return user;
    }

    public static String getToken() {
        return getToken(getUser(true));
    }

    public static String getToken(UserRole userRole) {
        return getToken(getUser(true, userRole));
    }

    public static String getToken(User user) {
        return "Bearer " + new JwtService().generateToken(user);
    }
}
