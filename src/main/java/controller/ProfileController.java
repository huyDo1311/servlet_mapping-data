package controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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

@WebServlet(name ="profileController", urlPatterns = {"/profile", "/profile-edit", "/profile-update"})
public class ProfileController extends HttpServlet {
	
	private UserService userService = new UserService();
	private TaskService taskService = new TaskService();
	private StatusService statusService = new StatusService();
	private GroupWorkService groupWorkService = new GroupWorkService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();
		String idUser = getUserIdFromCookie(req);

		// Lấy thông tin user
		Users user = userService.getUserById(Integer.parseInt(idUser));
		req.setAttribute("user", user);

		switch (path) {
			case "/profile": {
				List<Tasks> taskList = taskService.getTaskByIdUser(Integer.parseInt(idUser));
				
				// Tính tổng số task
			    int total = taskList.size();
			    int countNotStart = 0;
			    int countInProgress = 0;
			    int countDone = 0;

			    for (Tasks t : taskList) {
			        if (t.getStatus_id() == 1) {
			            countNotStart++;
			        } else if (t.getStatus_id() == 2) {
			            countInProgress++;
			        } else if (t.getStatus_id() == 3) {
			            countDone++;
			        }
			    }

			    // Tính %
			    double perNotStart = total > 0 ? (countNotStart * 100.0 / total) : 0;
			    double perInProgress = total > 0 ? (countInProgress * 100.0 / total) : 0;
			    double perDone = total > 0 ? (countDone * 100.0 / total) : 0;

			    // Gửi xuống JSP
			    req.setAttribute("perNotStart", perNotStart);
			    req.setAttribute("perInProgress", perInProgress);
			    req.setAttribute("perDone", perDone);
				
				req.setAttribute("task", taskList);
				req.setAttribute("user", user);
				req.getRequestDispatcher("profile.jsp").forward(req, resp);
				break;
			}
			case "/profile-edit": {
				String id = req.getParameter("id");
				if (id != null) {
					Tasks taskEdit = taskService.getTaskById(Integer.parseInt(id));
					Jobs  job =  groupWorkService.getJobById(taskEdit.getJob_id()) ;
					List<Status> status = statusService.getAllStatus();
					req.setAttribute("task", taskEdit);
					req.setAttribute("status", status);
					req.setAttribute("job", job);
					req.getRequestDispatcher("profile-edit.jsp").forward(req, resp);
				} else {
					resp.sendRedirect("profile"); // không có id thì quay lại profile
				}
				break;
			}
			default:
				resp.sendRedirect("profile");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		if ("/profile-update".equals(path)) {
			// Lấy data từ form update
			String id = req.getParameter("id");
			String statusId = req.getParameter("status_id");
			
			System.out.println(statusId);
			
//			Tasks task = new Tasks();
//			task.setId(Integer.parseInt(id));
//			task.setStatus_id(Integer.parseInt(statusId));

			taskService.updateStatus(Integer.parseInt(id), Integer.parseInt(statusId));;

			resp.sendRedirect("profile");
			
		}
	}

	// Hàm lấy idUser từ cookie
	private String getUserIdFromCookie(HttpServletRequest req) {
		Cookie[] listCookies = req.getCookies();
		if (listCookies != null) {
			for (Cookie cookie : listCookies) {
				if ("idUser".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "0";
	}
}
