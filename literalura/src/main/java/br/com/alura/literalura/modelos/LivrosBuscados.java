package br.com.alura.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class LivrosBuscados {
    @JsonAlias("results")
    private List<Livros> resultados;

    public LivrosBuscados() {
    }

    public List<Livros> getResultados() {
        return resultados;
    }

    public void setResultados(List<Livros> resultados) {
        this.resultados = resultados;
    }
}
