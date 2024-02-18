package model;

import java.util.Date;

public class Comment {
	int id;
	String conteudo;
	Date data_criacao;
	User autor;
	Recipe receita;
	
	public Comment() {
		super();
	}
	
	public Comment(int id, String conteudo, Date data_criacao, User autor, Recipe receita) {
		super();
		this.id = id;
		this.conteudo = conteudo;
		this.data_criacao = data_criacao;
		this.autor = autor;
		this.receita = receita;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getData_criacao() {
		return data_criacao;
	}
	public void setData_criacao(Date data_criacao) {
		this.data_criacao = data_criacao;
	}
	
	public User getAutor() {
		return autor;
	}
	public void setAutor(User autor) {
		this.autor = autor;
	}

	public Recipe getReceita() {
		return receita;
	}
	public void setReceita(Recipe receita) {
		this.receita = receita;
	}
	
}
