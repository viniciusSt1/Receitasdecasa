package controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import model.UserDAO;

@WebServlet(urlPatterns = { "/cadastroUsuario", "/deleteUsuario", "/atualizarUsuario", "/login", "/deslogar" })
public class ControllerUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO dao = new UserDAO();
	private User usuario = new User();
	HttpSession session;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		// Controle das requisições ao acessar as rotas desse servlet
		switch(action)
		{
		case "/cadastroUsuario" : cadastrarUsuario(request, response); break;
		case "/deleteUsuario" : deletarUsuario(request, response); break;
		case "/atualizarUsuario" :  atualizarUsuario(request, response); break; 
		case "/login" : login(request, response); break;
		case "/deslogar" : deslogar(request, response); break;
		}

	}

	protected void cadastrarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User novoUsuario = new User();
		String confsenha = request.getParameter("confsenha");
		// pegar parametros recebidos atraves do forms
		novoUsuario.setNome(request.getParameter("nome"));
		novoUsuario.setEmail(request.getParameter("email"));
		novoUsuario.setSenha(request.getParameter("senha"));
		novoUsuario.setAdmin(false);
		
		if (novoUsuario.getSenha().equals(confsenha)) {
			// mandar um usuario para classe dao fazer o cadastro no bd
			try {
				dao.cadastrarUsuario(novoUsuario);
			}catch(SQLException e) {
				String alerta = "Email ja existente";
				request.setAttribute("alerta", alerta);
				request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
			}
			response.sendRedirect("index.html"); // redirecionar para pagina inicial
		}
		else {
			String alerta = "Senhas não coincidem";
			request.setAttribute("alerta", alerta);
			request.getRequestDispatcher("/cadastro.jsp").forward(request, response);
		}
		
	}

	protected void deletarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		usuario = (User) session.getAttribute("usuario"); // pegar usuario logado atualmente

		if (dao.deletarUsuario(usuario.getId())) { // enviar id do usuario a ser deletado do bd
			System.out.println("Usuario deletado com sucesso");
			deslogar(request, response);
		} else {
			System.out.println("Falha ao deletar usuario");
			response.sendRedirect("perfil.jsp");
		}
		
	}

	protected void atualizarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		usuario = (User) session.getAttribute("usuario"); // pegar usuario logado atualmente
		User usuarioAtualizado = new User();

		try {
			usuarioAtualizado.setId(usuario.getId());
			usuarioAtualizado.setNome(request.getParameter("nome"));
			usuarioAtualizado.setEmail(request.getParameter("email"));
			usuarioAtualizado.setSenha(request.getParameter("senha"));
			usuarioAtualizado.setQntReceitas(usuario.getQntReceitas());
			usuarioAtualizado.setAdmin(usuario.getAdmin());
			
			dao.atualizarUsuario(usuarioAtualizado); // enviando usuario com novos dados e seu id para ser atualizado
			session.setAttribute("usuario", usuarioAtualizado);
			response.sendRedirect("perfil.jsp");
		} catch (Exception e) {
			System.out.println(e);// provavel erro no ParseInt
		}

	}

	protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession();
		// pegando dados de email e senha preechidos no forms
		String email = request.getParameter("email");
		String senha = request.getParameter("senha");

		usuario = dao.validarCredenciais(email, senha); // validando se existe algum usuario com esses dados
		if (usuario != null) {	//credenias validas
			session.setAttribute("usuario", usuario);
			response.sendRedirect("perfil.jsp");
		} else {
			String alerta = "Senha ou Usuario Incorreto. Tente Novamente";
			request.setAttribute("alerta", alerta);
			request.getRequestDispatcher("/login.jsp").forward(request, response);
		}

	}

	protected void deslogar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession(); // pegando sessao atual
		session.invalidate(); // encerrando sessao
		response.sendRedirect("index.html");
	}

}
