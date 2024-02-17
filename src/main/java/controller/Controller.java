package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Category;
import model.Comment;
import model.CommentDAO;
import model.Recipe;
import model.RecipeDAO;
import model.User;

@WebServlet(urlPatterns = { "/comentar", "/cadastrarReceita" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommentDAO comentarioDAO = new CommentDAO();
	private RecipeDAO recipe_DAO = new RecipeDAO();
	private Recipe receita = new Recipe();
	private User autor = new User();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();
		System.out.println("Entrei no if de actions");

		// Controle das requisições ao acessar as rotas desse servlet
		if (action.equals("/comentar")) {
			comentar(request, response);
		}

		if (action.equals("/cadastrarReceita")) {
			cadastrarReceita(request, response);
		}

	}

	protected void comentar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Obtendo os parâmetros do formulário de comentário
		String conteudo = request.getParameter("conteudo"); // Supondo que "conteudo" seja o nome do campo de texto do comentário
		int idReceita = Integer.parseInt(request.getParameter("idReceita")); // Supondo que "idReceita" seja o ID da receita à qual o comentário será associado
		
		HttpSession session = request.getSession();
		autor = (User) session.getAttribute("usuario"); // pegar usuario logado atualmente

		// Criando um objeto Comment com os dados fornecidos
		Recipe receita = new Recipe();
		receita.setId(idReceita); // Definindo o ID da receita associada ao comentário

		Comment novoComentario = new Comment();
		novoComentario.setConteudo(conteudo);
		novoComentario.setAutor(autor);
		novoComentario.setReceita(receita);

		// Adicionando o comentário ao banco de dados usando o CommentDAO
		comentarioDAO.adicionarComentario(novoComentario);

		// Redirecionando de volta para a página da receita ou realizando outra ação desejada
		response.sendRedirect(""); // Colocar a rota
	}

	protected void cadastrarReceita(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		autor = (User) session.getAttribute("usuario");
		ArrayList<Category> vet_categorias = new ArrayList<Category>();
		Category categoria_cache = new Category();

		// Armazenando vetor de categorias
		String cache_checkbox = request.getParameter("Cafe da manha");
		if (cache_checkbox != null) {
			categoria_cache.setId(1);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		cache_checkbox = request.getParameter("Almoco");
		if (cache_checkbox != null) {
			categoria_cache.setId(2);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		cache_checkbox = request.getParameter("Cafe da tarde");
		if (cache_checkbox != null) {
			categoria_cache.setId(3);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		cache_checkbox = request.getParameter("Janta");
		if (cache_checkbox != null) {
			categoria_cache.setId(4);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		cache_checkbox = request.getParameter("Massas");
		if (cache_checkbox != null) {
			categoria_cache.setId(5);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		cache_checkbox = request.getParameter("Bebidas");
		if (cache_checkbox != null) {
			categoria_cache.setId(6);
			categoria_cache.setNome(cache_checkbox);
			vet_categorias.add(categoria_cache);
		}

		// Inserção da Receita
		receita.setTitulo(request.getParameter("titulo"));
		receita.setIngredientes(request.getParameter("ingredientes"));
		receita.setConteudo(request.getParameter("conteudo"));
		receita.setCategorias(vet_categorias);
		receita.setAutor(autor);
		// receita.setCategorias(request.getParameter("categorias[]"));

		recipe_DAO.cadastrarReceita(receita);
		response.sendRedirect("index.html"); // redirecionar para pagina inicial
	}

}
