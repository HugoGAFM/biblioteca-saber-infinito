
package org.project.Aplicacao;

import java.time.LocalDate;

public class Livro {
    String ISBN;
    String titulo;
    String autor;
    int numCopias;
    private LocalDate dataPublicacao;

    public Livro(String ISBN, String titulo, String autor, LocalDate dataPublicacao, int numCopias){
        this.titulo=titulo;
        this.autor=autor;
        this.numCopias=numCopias;
        this.ISBN=ISBN;
        this.dataPublicacao = dataPublicacao;
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

    public LocalDate getDataPublicacao() { return dataPublicacao; }

    public int getnumCopias() {
        return numCopias;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (ISBN: " + ISBN + ")";
    }
}
