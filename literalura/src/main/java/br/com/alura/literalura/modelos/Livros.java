package br.com.alura.literalura.modelos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livro")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Livros {

    @Id
    @Column(name = "id_livro")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonAlias("title")
    private String titulo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id")
    @JsonAlias("authors")
    private List<Autores> autores;

    @ElementCollection
    @CollectionTable(name = "livros_idioma", joinColumns = @JoinColumn(name = "livro_id"))
    @Column(name = "idioma")
    @JsonAlias("languages")
    private List<String> idioma;

    @JsonAlias("download_count")
    private int numeroDeDownloads;

    public Livros() {
        this.autores = new ArrayList<>();
        this.idioma = new ArrayList<>();
    }

    public Livros(Livros livros) {
        this.titulo = livros.titulo;
        this.idioma = new ArrayList<>(livros.idioma);
        this.numeroDeDownloads = livros.numeroDeDownloads;
        this.autores = new ArrayList<>(livros.autores);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autores> getAutores() {
        return autores;
    }

    public void setAutores(List<Autores> autores) {
        this.autores = autores;
    }

    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public int getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    public void setNumeroDeDownloads(int numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }


    @Override
    public String toString() {
        StringBuilder autoresStr = new StringBuilder();
        for (Autores autor : autores) {
            autoresStr.append(autor.toString()).append(" ");
        }

        return "\n**********************" +
                "\nRESULTADO: " +
                "\nTítulo: '" + titulo + '\'' +
                "\n" + autoresStr.toString() +
                "\nIdioma: " + idioma.toString() +
                "\nNúmero de Downloads: " + numeroDeDownloads +
                "\n**********************";
    }
}
