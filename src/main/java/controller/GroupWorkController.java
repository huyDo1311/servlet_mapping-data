package controller;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Jobs;
import entity.Roles;
import entity.Tasks;
import entity.Users;
import services.GroupWorkService;
import services.TaskService;
import services.UserService;

@WebServlet(name = "groupWorkController", urlPatterns = { "/groupwork", "/groupwork-add", "/groupwork-edit",
		"/groupwork-update", "/groupwork-delete", "/groupwork-details" })
public class GroupWorkController extends HttpServlet {

	private GroupWorkService groupWorkService = new GroupWorkService();
	private TaskService taskService = new TaskService();
	private UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		switch (path) {
		case "/groupwork": {
			List<Jobs> listJobs = groupWorkService.getAllJob();
			req.setAttribute("listJobs", listJobs);
			req.getRequestDispatcher("groupwork.jsp").forward(req, resp);
			break;
		}

		case "/groupwork-edit": {
			String editIdStr = req.getParameter("id");
			if (editIdStr != null) {
				int editId = Integer.parseInt(editIdStr);
				Jobs jobEdit = groupWorkService.getJobById(editId);
				req.setAttribute("job", jobEdit);
				req.getRequestDispatcher("groupwork-add.jsp").forward(req, resp);
			} else {
				resp.sendRedirect("groupwork");
			}
			break;
		}

		case "/groupwork-details": {
			int jobId = Integer.parseInt(req.getParameter("id"));
			Jobs job = groupWorkService.getJobById(jobId);
			List<Tasks> taskList = taskService.getTaskByIdJob(jobId);
			List<Users> userList = userService.getUsersByJobId(jobId);

			// Tính thống kê %
			int total = taskList.size();
			int countNotStart = 0, countInProgress = 0, countDone = 0;
			for (Tasks t : taskList) {
				if (t.getStatus_id() == 1)
					countNotStart++;
				else if (t.getStatus_id() == 2)
					countInProgress++;
				else if (t.getStatus_id() == 3)
					countDone++;
			}
			double perNotStart = total > 0 ? (countNotStart * 100.0 / total) : 0;
			double perInProgress = total > 0 ? (countInProgress * 100.0 / total) : 0;
			double perDone = total > 0 ? (countDone * 100.0 / total) : 0;

			req.setAttribute("job", job);
			req.setAttribute("userList", userList);
			req.setAttribute("taskList", taskList);
			req.setAttribute("perNotStart", perNotStart);
			req.setAttribute("perInProgress", perInProgress);
			req.setAttribute("perDone", perDone);

			req.getRequestDispatcher("groupwork-details.jsp").forward(req, resp);
			break;
		}

		case "/groupwork-delete": {
			String deleteIdStr = req.getParameter("id");
			if (deleteIdStr != null) {
				int deleteId = Integer.parseInt(deleteIdStr);
				groupWorkService.deleteJob(deleteId);
			}
			resp.sendRedirect("groupwork");
			break;
		}

		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		switch (path) {
		case "/groupwork-add": {
			String name = req.getParameter("name");
			String startDateStr = req.getParameter("start_date"); // dd/MM/yyyy
			String endDateStr = req.getParameter("end_date"); // dd/MM/yyyy

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

				Jobs job = new Jobs();
				job.setName(name);
				job.setStart_date(startDate);
				job.setEnd_date(endDate);

				groupWorkService.addJob(job);
				resp.sendRedirect("groupwork");

			} catch (Exception e) {
				e.printStackTrace();
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Ngày không hợp lệ, vui lòng nhập theo định dạng dd/MM/yyyy");
			}
			break;

		}

		case "/groupwork-update": {
			int id = Integer.parseInt(req.getParameter("id"));
			String name = req.getParameter("name");
			Date startDate = Date.valueOf(req.getParameter("start_date"));
			Date endDate = Date.valueOf(req.getParameter("end_date"));

			Jobs job = new Jobs();
			job.setId(id);
			job.setName(name);
			job.setStart_date(startDate);
			job.setEnd_date(endDate);

			if (job != null) {
				groupWorkService.updateJob(job);
				resp.sendRedirect("groupwork");
				break;
			}
		}
		}
	}
}
