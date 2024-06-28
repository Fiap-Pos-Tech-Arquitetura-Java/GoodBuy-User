package br.com.fiap.postech.goodbuy.user.helper;

import br.com.fiap.postech.goodbuy.security.JwtService;
import br.com.fiap.postech.goodbuy.security.UserDetailsImpl;
import br.com.fiap.postech.goodbuy.security.enums.UserRole;
import br.com.fiap.postech.goodbuy.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
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

    public static String getToken(User user) {
        br.com.fiap.postech.goodbuy.security.User userSecurity = getUserForSecurity(user);
        return "Bearer " + new JwtService().generateToken(userSecurity);
    }

    public static UserDetails getUserDetails(User user) {
        return new UserDetailsImpl(getUserForSecurity(user));
    }

    private static br.com.fiap.postech.goodbuy.security.User getUserForSecurity(User user) {
        br.com.fiap.postech.goodbuy.security.User userSecurity =
                new br.com.fiap.postech.goodbuy.security.User(user.getLogin(), user.getPassword(), user.getRole());
        return userSecurity;
    }
}
