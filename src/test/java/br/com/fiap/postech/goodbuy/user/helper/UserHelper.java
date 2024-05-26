package br.com.fiap.postech.goodbuy.user.helper;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.entity.enums.UserRole;

import java.util.UUID;

public class UserHelper {
    public static User getUser(boolean geraId) {
        var user = new User(
                "anderson.Wagner",
                "Anderson Wagner",
                "25310413030",
                "123456",
                UserRole.ADMIN
        );
        if (geraId) {
            user.setId(UUID.randomUUID());
        }
        return user;
    }
}
