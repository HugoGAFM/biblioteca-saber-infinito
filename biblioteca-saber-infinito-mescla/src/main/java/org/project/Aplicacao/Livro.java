package org.project.Aplicacao;

public class Livro {
    String ISBN;
    String titulo;
    String autor;
    int copias;

    public Livro(String ISBN, String titulo, String autor, int copias){
        this.titulo=titulo;
        this.autor=autor;
        this.copias=copias;
        this.ISBN=ISBN;
    }

    public String getAutor() {
        return autor;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getCopias() {
        return copias;
    }
}
