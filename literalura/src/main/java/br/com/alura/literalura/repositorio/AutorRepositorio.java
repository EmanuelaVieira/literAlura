package br.com.alura.literalura.repositorio;

import br.com.alura.literalura.modelos.Autores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepositorio extends JpaRepository<Autores, Long> {
    List<Autores> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(int anoNascimento, int anoFalecimento);
}