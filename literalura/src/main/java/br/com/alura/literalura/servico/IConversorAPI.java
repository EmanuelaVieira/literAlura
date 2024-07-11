package br.com.alura.literalura.servico;

public interface IConversorAPI {
    <T> T obterDados(String Json, Class<T> classe);
}