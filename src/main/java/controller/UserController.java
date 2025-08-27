package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Roles;
import entity.Tasks;
import entity.Users;
import services.RoleService;
import services.TaskService;
import services.UserService;

@WebServlet(name = "userController", urlPatterns = { "/user", "/user-add", "/user-edit", "/user-update", "/user-delete", "/user-details" })
public class UserController extends HttpServlet {

    private final UserService userService = new UserService();
    private final RoleService roleService = new RoleService();
    private TaskService taskService = new TaskService();
  

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        
		

        switch (path) {
            case "/user-add": {
                List<Roles> listRoles = roleService.getAllRole();
                req.setAttribute("listRoles", listRoles);
                req.getRequestDispatcher("user-add.jsp").forward(req, resp);
                break;
            }

            case "/user-edit": {
                int id = Integer.parseInt(req.getParameter("id"));
                Users user = userService.getUserById(id);
                List<Roles> listRoles = roleService.getAllRole();
                req.setAttribute("user", user);
                req.setAttribute("listRoles", listRoles);
                req.getRequestDispatcher("user-add.jsp").forward(req, resp);
                break;
            }

            case "/user-delete": {
                int id = Integer.parseInt(req.getParameter("id"));
                userService.deleteUser(id);
                resp.sendRedirect(req.getContextPath() + "/user");
                break;
            }
            
            case "/user-details": {
            	Users user = userService.getUserById(Integer.parseInt(req.getParameter("idUser")));
        		
            	List<Tasks> taskList = taskService.getTaskByIdUser(Integer.parseInt(req.getParameter("idUser")));
				
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
			    req.setAttribute("user", user);
			    req.setAttribute("taskList", taskList);
			    req.getRequestDispatcher("user-details.jsp").forward(req, resp);
			    break;
            }

            default: {
                List<Users> listUsers = userService.getAllUser();
                req.setAttribute("listUsers", listUsers);
                req.getRequestDispatcher("user-table.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        String fullName = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String avatar = req.getParameter("avatar");
        int roleId = Integer.parseInt(req.getParameter("role_id"));

        Users user = new Users();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setRoleId(roleId);

        switch (path) {
            case "/user-add":
                userService.addUser(user);
                break;

            case "/user-update":
                int id = Integer.parseInt(req.getParameter("id"));
                user.setId(id);
                userService.updateUser(user);
                break;
        }

        resp.sendRedirect(req.getContextPath() + "/user");
    }
    
    
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





