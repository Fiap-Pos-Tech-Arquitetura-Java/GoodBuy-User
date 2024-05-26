package br.com.fiap.postech.goodbuy.user.service;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.helper.UserHelper;
import br.com.fiap.postech.goodbuy.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class UserServiceIT {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceIT(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void devePermitirCriarEstrutura() {
        var totalRegistros = userRepository.count();
        assertThat(totalRegistros).isEqualTo(3);
    }

    @Test
    void devePermitirCadastrarUser() {
        // Arrange
        var user = UserHelper.getUser(true);
        // Act
        var userCadastrado = userRepository.save(user);
        // Assert
        assertThat(userCadastrado).isInstanceOf(User.class).isNotNull();
        assertThat(userCadastrado.getId()).isEqualTo(user.getId());
        assertThat(userCadastrado.getName()).isEqualTo(user.getName());
    }
    @Test
    void devePermitirBuscarUser() {
        // Arrange
        var id = UUID.fromString("7a04f6fb-c79b-4b47-af54-9bef34cbab35");
        var name = "Anderson Wagner";
        // Act
        var userOpcional = userRepository.findById(id);
        // Assert
        assertThat(userOpcional).isPresent();
        userOpcional.ifPresent(
                userRecebido -> {
                    assertThat(userRecebido).isInstanceOf(User.class).isNotNull();
                    assertThat(userRecebido.getId()).isEqualTo(id);
                    assertThat(userRecebido.getName()).isEqualTo(name);
                }
        );
    }
    @Test
    void devePermitirRemoverUser() {
        // Arrange
        var id = UUID.fromString("8855e7b2-77b6-448b-97f8-8a0b529f3976");
        // Act
        userRepository.deleteById(id);
        // Assert
        var userOpcional = userRepository.findById(id);
        assertThat(userOpcional).isEmpty();
    }
    @Test
    void devePermitirListarUsers() {
        // Arrange
        // Act
        var usersListados = userRepository.findAll();
        // Assert
        assertThat(usersListados).hasSize(3);
    }
}
