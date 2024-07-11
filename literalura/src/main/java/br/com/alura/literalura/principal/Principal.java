package br.com.alura.literalura.principal;

import br.com.alura.literalura.modelos.Autores;
import br.com.alura.literalura.modelos.Livros;
import br.com.alura.literalura.modelos.LivrosBuscados;
import br.com.alura.literalura.repositorio.AutorRepositorio;
import br.com.alura.literalura.repositorio.LivroRepositorio;
import br.com.alura.literalura.servico.ConsumoAPI;
import br.com.alura.literalura.servico.ConversorAPI;
import br.com.alura.literalura.servico.Estatisticas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal {

    private Scanner acao = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private int opcao = -1;

    private final ConsumoAPI consumo;
    private final ConversorAPI conversor;
    private final LivroRepositorio livroRepositorio;
    private final AutorRepositorio autorRepositorio;
    private final Estatisticas estatisticas;
    private List<Livros> livrosBuscados = new ArrayList<>();

    @Autowired
    public Principal(LivroRepositorio livroRepositorio, AutorRepositorio autorRepositorio,
                     Estatisticas estatisticas, ConsumoAPI consumo, ConversorAPI conversor) {
        this.livroRepositorio = livroRepositorio;
        this.autorRepositorio = autorRepositorio;
        this.estatisticas = estatisticas;
        this.consumo = consumo;
        this.conversor = conversor;
    }

    public void Inicio() {

        while (opcao != 0) {
            System.out.println("""
                    \n********************************
                    >>>MENU DE OPÇÕES<<<
                                        
                    >>> 1 - BUSCAR LIVRO
                    >>> 2 - LISTAR AUTORES
                    >>> 3 - LISTAR POR IDIOMAS
                    >>> 4 - LISTAR AUTORES POR ANO
                    >>> 5 - LISTAR TODOS OS LIVROS PESQUISADOS
                                        
                    >>> 0 - SAIR

                    DIGITE A OPÇÃO DESEJADA:
                    ********************************
                    """);
            opcao = acao.nextInt();
            acao.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivros();
                    break;
                case 2:
                    listarAutores();
                    break;
                case 3:
                    listarPorIdioma();
                    break;
                case 4:
                    listarPorAno();
                    break;
                case 5:
                    listarLivros();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void buscarLivros() {
        Livros resultadosLivros = getPrimeiroLivroBuscado();
        if (resultadosLivros != null) {
            Livros livros = new Livros(resultadosLivros);
            livroRepositorio.save(livros);
            livrosBuscados.add(livros);
        } else {
            System.out.println("Nenhum livro encontrado para a pesquisa realizada.");
        }
    }

    private Livros getPrimeiroLivroBuscado() {
        System.out.println("\nDIGITE O LIVRO PARA BUSCA:");
        var buscaLivro = acao.nextLine();
        var json = consumo.obterDados(URL_BASE + "?search=" + buscaLivro.replace(" ", "+"));
        LivrosBuscados livrosBuscados = conversor.obterDados(json, LivrosBuscados.class);

        if (livrosBuscados.getResultados().isEmpty()) {
            return null;
        }

        Livros primeiroLivro = livrosBuscados.getResultados().get(0);
        System.out.println(primeiroLivro);
        return primeiroLivro;
    }

    private void listarAutores() {
        List<Autores> autoresListados = autorRepositorio.findAll();
        autoresListados.forEach(autor -> System.out.println("Nome: " + autor.getNomeAutor() +
                " [" + autor.getAnoNascimento() + "-" + autor.getAnoFalecimento() + "]"));
    }

    private void listarPorIdioma() {
        boolean idiomaInvalido = true;
        List<String> idiomaDigitado;

        do {
            System.out.println("""
                    \nESCOLHA O IDIOMA PARA BUSCA:
                    >>> PT - Português
                    >>> ES - Espanhol
                    >>> EN - Inglês
                    """);

            var idiomaPesquisado = acao.nextLine().toUpperCase();

            if (!idiomaPesquisado.equals("ES") && !idiomaPesquisado.equals("EN") && !idiomaPesquisado.equals("PT")) {
                System.out.println("Idioma inválido. Tente novamente.");
                continue;
            }

            idiomaDigitado = Collections.singletonList(idiomaPesquisado.toLowerCase());

            long quantidadeDeLivros = livroRepositorio.countByListaDeIdiomas(idiomaDigitado);

            System.out.println("Quantidade de livros encontrados em " + idiomaPesquisado + ": " + quantidadeDeLivros);

            idiomaInvalido = false;
        } while (idiomaInvalido);
    }

    private void listarPorAno() {
        int anoPesquisa = -1;
        while (anoPesquisa < 0) {
            try {
                System.out.println("DIGITE O ANO PARA PESQUISA:");
                anoPesquisa = Integer.parseInt(acao.nextLine());
                if (anoPesquisa < 0) {
                    System.out.println("Ano inválido. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
            }
        }

        List<Autores> autoresVivos =
                autorRepositorio.findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(anoPesquisa, anoPesquisa);

        if (autoresVivos.isEmpty()) {
            System.out.println("Nenhum autor encontrado vivo no ano " + anoPesquisa);
        } else {
            System.out.println("Autores encontrados:");
            autoresVivos.forEach(autor ->
                    System.out.println("Nome: " + autor.getNomeAutor() +
                            " [" + autor.getAnoNascimento() + "-" + autor.getAnoFalecimento() + "]"));
        }
    }

    public void listarLivros() {
        List<Livros> todosLivros = livroRepositorio.findAll();

        todosLivros.forEach(livros -> System.out.println("Livro título: " + livros.getTitulo() + " " + livros.getAutores()));
    }
}