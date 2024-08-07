package br.com.fiap.postech.goodbuy.user.controller;

import br.com.fiap.postech.goodbuy.security.SecurityHelper;
import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.security.Token;
import br.com.fiap.postech.goodbuy.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Service
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SecurityHelper securityHelper;

    @Autowired
    public UserController(UserService userService, SecurityHelper securityHelper) {
        this.userService = userService;
        this.securityHelper = securityHelper;
    }

    @Operation(summary = "registra um user")
    @PostMapping
    public ResponseEntity<User> save(@Valid @RequestBody User userDTO) {
        User savedUserDTO = userService.save(userDTO);
        return new ResponseEntity<>(savedUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "lista todos os users")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<User>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf
    ) {
        User user = new User(login, nome, cpf, null, null);
        user.setId(null);
        var pageable = PageRequest.of(page, size);
        var users = userService.findAll(pageable, user);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "lista um user por seu id")
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        try {
            User user = userService.findById(id);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "lista um user por seu login", hidden = true)
    @GetMapping("/findByLogin/{login}")
    public ResponseEntity<User> findByLogin(@PathVariable String login) {
        try {
            User user = userService.findByLogin(login);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "altera um user por seu id")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @Valid @RequestBody User userDTO) {
        try {
            User updatedUser = userService.update(id, userDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.ACCEPTED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "remove um user por seu id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException
                exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "realiza o login do user")
    @PostMapping("/login")
    public ResponseEntity<Token> login(@Valid @RequestBody User user) throws Exception {
        Token token = userService.login(user);
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }
}
