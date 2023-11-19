package br.com.fiap.climei.models;

public record Token(
    String token,
    String type,
    String prefix
) {}
