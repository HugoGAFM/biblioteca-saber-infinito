package org.project.Aplicacao;

public class Membro {
    int idMembro;
    String nome;
    String telefone;
    String email;
    boolean devendo;

    public Membro(String nome, String telefone, String email){
        this.nome=nome;
        this.telefone=telefone;
        this.email=email;
    }

    public int getIdMembro() {return idMembro;}

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDevendo() {
        return devendo;
    }


    @Override
    public String toString() {
        return nome + " - " + email;
    }
}
