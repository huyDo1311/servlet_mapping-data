package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Roles;
import services.RoleService;

@WebServlet(name = "roleController", urlPatterns = {"/role-table"})
public class RoleController extends HttpServlet {
	
	private RoleService roleService = new RoleService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Roles> listRoles = roleService.getAllRole();
		req.setAttribute("listRoles", listRoles);
		req.getRequestDispatcher("role-table.jsp").forward(req, resp);
	}
}
