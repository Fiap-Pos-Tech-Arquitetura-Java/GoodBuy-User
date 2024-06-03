package br.com.fiap.postech.goodbuy.user.security;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class SecurityHelper {
    private final UserService userService;
    @Autowired
    public SecurityHelper(UserService userService) {
        this.userService = userService;
    }

    public User getLoggedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(userDetails.getUsername());
    }
}
