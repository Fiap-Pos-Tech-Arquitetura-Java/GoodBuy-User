package br.com.fiap.postech.goodbuy.user.service;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.security.Token;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {
    User save(User user);

    Page<User> findAll(Pageable pageable, User user);

    User findById(UUID id);

    User update(UUID id, User user);

    void delete(UUID id);

    User findByLogin(String username);

    Token login(User user) throws Exception;
}
