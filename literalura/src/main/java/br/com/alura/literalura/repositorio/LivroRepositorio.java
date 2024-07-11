package br.com.alura.literalura.repositorio;

import br.com.alura.literalura.modelos.Livros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LivroRepositorio extends JpaRepository<Livros, Long> {
    @Query("SELECT COUNT(l) FROM Livros l JOIN l.idioma i WHERE LOWER(i) IN :idiomas")
    long countByListaDeIdiomas(@Param("idiomas") List<String> idiomas);
}