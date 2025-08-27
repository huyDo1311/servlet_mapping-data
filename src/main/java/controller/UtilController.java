package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Tasks;
import services.TaskService;

@WebServlet(name="utilController", urlPatterns = {"/index"})
public class UtilController extends HttpServlet {
	private TaskService taskService = new TaskService();
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		List<Tasks> taskList = taskService.getAllTasks();
		
		System.out.println("taskList:   "+ taskList.size()); 
		
		int total = taskList.size();
		int countNotStart = 0, countInProgress = 0, countDone = 0;

		for (Tasks t : taskList) {
		    if (t.getStatus_id() == 1) {
		        countNotStart++;
		    } else if (t.getStatus_id() == 2) {
		        countInProgress++;
		    } else if (t.getStatus_id() == 3) {
		        countDone++;
		    }
		}

		// In ra kết quả đếm
		System.out.println("Tổng số task: " + total);
		System.out.println("Chưa bắt đầu: " + countNotStart);
		System.out.println("Đang thực hiện: " + countInProgress);
		System.out.println("Hoàn thành: " + countDone);

		req.setAttribute("countNotStart", countNotStart);
		req.setAttribute("countInProgress", countInProgress);
		req.setAttribute("countDone", countDone);
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
}
