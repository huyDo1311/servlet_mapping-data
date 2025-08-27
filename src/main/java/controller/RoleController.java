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

@WebServlet(name = "roleController", urlPatterns = { "/role", "/role-add", "/role-edit", "/role-update",
		"/role-delete" })
public class RoleController extends HttpServlet {

	private RoleService roleService = new RoleService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = req.getServletPath();

		switch (path) {
		case "/role-edit": {
			String role_id = req.getParameter("id");
			if (role_id != null) {
				Roles role = roleService.getRoleById(role_id);
				req.setAttribute("role", role);
				req.getRequestDispatcher("role-add.jsp").forward(req, resp);
				break;
			}

		}
		case "/role-delete": {
			String role_id = req.getParameter("id");
			if (role_id != null) {
				roleService.deleteRole(Integer.parseInt(role_id));
			}
			break;
		}
		}
		List<Roles> listRoles = roleService.getAllRole();
		req.setAttribute("listRoles", listRoles);
		req.getRequestDispatcher("role-table.jsp").forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		String path = req.getServletPath();

		String roleName = req.getParameter("roleName");
		String desc = req.getParameter("desc");

		switch (path) {
		case "/role-update": {
			String role_id = req.getParameter("id");
			if (role_id != null && roleName != null && !roleName.trim().isEmpty()) {
				Roles role = new Roles();
				role.setId(Integer.parseInt(role_id));
				role.setName(roleName);
				role.setDescription(desc);

				roleService.updateRole(role);
			}
			break;
		}

		case "/role-add": {
			if (roleName != null) {
				roleService.insertRole(roleName, desc);
			}
			break;
		}
		}

		List<Roles> listRoles = roleService.getAllRole();
		req.setAttribute("listRoles", listRoles);
		req.getRequestDispatcher("role-table.jsp").forward(req, resp);

	}
}
