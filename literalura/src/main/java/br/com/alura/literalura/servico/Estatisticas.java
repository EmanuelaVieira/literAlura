package br.com.alura.literalura.servico;

import br.com.alura.literalura.repositorio.LivroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Estatisticas {

        private final LivroRepositorio livroRepositorio;

        @Autowired
        public Estatisticas(LivroRepositorio livroRepositorio) {
            this.livroRepositorio = livroRepositorio;
        }

        public long contarLivrosPorIdioma(List<String> idiomas) {
            return livroRepositorio.countByListaDeIdiomas(idiomas);
        }
}

