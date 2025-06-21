package org.project.Aplicacao;

public class Membro {
    Long idMebro;
    String nome;
    String telefone;
    String email;
    boolean devendo;

    public Membro(Long idMebro, String nome, String telefone, String email, boolean devendo){
        this.idMebro=idMebro;
        this.nome=nome;
        this.telefone=telefone;
        this.email=email;
        this.devendo=devendo;
    }


}
