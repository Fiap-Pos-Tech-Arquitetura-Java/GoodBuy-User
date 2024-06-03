package br.com.fiap.postech.goodbuy.user.security;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Token(
        String accessToken,

        String erro
) {
}
