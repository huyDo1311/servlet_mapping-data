package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Jobs;
import services.GroupWorkService;

@WebServlet(name ="groupWorkController", urlPatterns = "/groupwork") 
public class GroupWorkController extends HttpServlet {
	
	private GroupWorkService groupWorkService = new GroupWorkService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Jobs> listJobs = groupWorkService.getAllJob();
		
		req.setAttribute("listJobs", listJobs);
		
		req.getRequestDispatcher("groupwork.jsp").forward(req, resp);
	}
}
