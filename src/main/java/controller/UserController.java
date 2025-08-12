package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Users;
import services.UserService;

@WebServlet(name ="userController", urlPatterns = "/user") 
public class UserController extends HttpServlet {
	
	private UserService userService = new UserService();
	
	 @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		 List<Users> listUsers = userService.getAllUser();
		 
		 //System.out.println("Chieu dai list" + liseUsers.size());
		 
		 req.setAttribute("listUsers", listUsers);
		 
		 req.getRequestDispatcher("user-table.jsp").forward(req, resp);
	}
}
