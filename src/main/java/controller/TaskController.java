package controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Jobs;
import entity.Status;
import entity.Tasks;
import entity.Users;
import services.GroupWorkService;
import services.StatusService;
import services.TaskService;
import services.UserService;

@WebServlet(urlPatterns = { "/task", "/task-add-mapping", "/task-add", "/task-edit", "/task-update", "/task-delete" })
public class TaskController extends HttpServlet {
	private final TaskService taskService = new TaskService();
	private UserService userService = new UserService();
	private GroupWorkService groupWorkService = new GroupWorkService();
	private StatusService statusService = new StatusService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		String path = req.getServletPath();

		switch (path) {
		case "/task": {
			List<Tasks> list = taskService.getAllTasks();
			req.setAttribute("listTasks", list);
			req.getRequestDispatcher("task.jsp").forward(req, resp);
			break;
		}

		case "/task-add-mapping": {
			List<Users> listUsers = userService.getAllUser();
			List<Jobs> listJobs = groupWorkService.getAllJob();
			List<Status> listStatus = statusService.getAllStatus();

			req.setAttribute("listUsers", listUsers);
			req.setAttribute("listJobs", listJobs);
			req.setAttribute("listStatus", listStatus);

			req.getRequestDispatcher("task-add.jsp").forward(req, resp);
			break;
		}

		case "/task-edit": { 
			String idStr = req.getParameter("id");
			if (idStr == null || idStr.isEmpty()) {
				resp.sendRedirect(req.getContextPath() + "/task");
				return;
			}
			try {
				int id = Integer.parseInt(idStr);
				Tasks task = taskService.getTaskById(id);
				if (task == null) {
					resp.sendRedirect(req.getContextPath() + "/task");
					return;
				}
				
				List<Users> listUsers = userService.getAllUser();
				List<Jobs> listJobs = groupWorkService.getAllJob();
				List<Status> listStatus = statusService.getAllStatus();

				req.setAttribute("listUsers", listUsers);
				req.setAttribute("listJobs", listJobs);
				req.setAttribute("listStatus", listStatus);
				
				req.setAttribute("task", task);
				req.getRequestDispatcher("task-add.jsp").forward(req, resp);
			} catch (NumberFormatException e) {
				resp.sendRedirect(req.getContextPath() + "/task");
			}
			break;
		}

		case "/task-delete": {
			String idStr = req.getParameter("id");
			if (idStr != null) {
				try {
					int id = Integer.parseInt(idStr);
					taskService.deleteTask(id);
				} catch (NumberFormatException ignored) {
				}
			}
			resp.sendRedirect(req.getContextPath() + "/task");
			break;
		}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html; charset=UTF-8");

		String path = req.getServletPath();

		switch (path) {
		case "/task-add": {
			String name = req.getParameter("name");
			String startDateStr = req.getParameter("start_date"); // dd/MM/yyyy
			String endDateStr = req.getParameter("end_date"); // dd/MM/yyyy
			int userId = Integer.parseInt(req.getParameter("user_id"));
			int jobId = Integer.parseInt(req.getParameter("job_id"));
			int statusId = Integer.parseInt(req.getParameter("status_id"));

			Date startDate = null;
			Date endDate = null;

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				sdf.setLenient(false);

				if (startDateStr != null && !startDateStr.isEmpty()) {
					java.util.Date utilStart = sdf.parse(startDateStr);
					startDate = new Date(utilStart.getTime());
				}

				if (endDateStr != null && !endDateStr.isEmpty()) {
					java.util.Date utilEnd = sdf.parse(endDateStr);
					endDate = new Date(utilEnd.getTime());
				}

				// Tạo đối tượng task
				Tasks t = new Tasks();
				t.setName(name);
				t.setStart_date(startDate);
				t.setEnd_date(endDate);
				t.setUser_id(userId);
				t.setJob_id(jobId);
				t.setStatus_id(statusId); 

				taskService.addTask(t);
				resp.sendRedirect(req.getContextPath() + "/task");

			} catch (Exception e) {
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Ngày không hợp lệ, vui lòng nhập theo định dạng dd/MM/yyyy");
			}
			break;
		}

		case "/task-update": {
			int id = Integer.parseInt(req.getParameter("id"));
			String name = req.getParameter("name");
			Date startDate = Date.valueOf(req.getParameter("start_date"));
			Date endDate = Date.valueOf(req.getParameter("end_date"));
			int userId = Integer.parseInt(req.getParameter("user_id"));
			int jobId = Integer.parseInt(req.getParameter("job_id"));
			int statusId = Integer.parseInt(req.getParameter("status_id"));

			Tasks t = new Tasks();
			t.setId(id);
			t.setName(name);
			t.setStart_date(startDate);
			t.setEnd_date(endDate);
			t.setUser_id(userId);
			t.setJob_id(jobId);
			t.setStatus_id(statusId);

			taskService.updateTask(t);
			resp.sendRedirect(req.getContextPath() + "/task");
			break;
		}
		}
	}
}
