package br.com.fiap.postech.goodbuy.user.repository;

import br.com.fiap.postech.goodbuy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByCpf(String cpf);

    Optional<User> findByLogin(String login);
}
