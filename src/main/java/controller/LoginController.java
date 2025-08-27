package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.MySQLConfig;
import entity.Users;
import services.UserService;

@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet{
	
	UserService userService = new UserService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Cookie[] listCookies = req.getCookies();
		String email = "";
		String password = "";
		
		for (Cookie cookie : listCookies) {
			//getName() : Tra ra ten cookie
			String name = cookie.getName();
			//getValue(): Tra ra gia tri luu tru trong cookie
			String value =  cookie.getValue();
			
			// so sanh equals la so sanh tham chieu(so sanh doi tuong) chu cai dau viet hoa con lai la tham tri ==
			if(name.equals("email")) {
				email = value;
			}
			
			if(name.equals("password")) {
				password = value;
			}
		}		
		
		req.setAttribute("email", email);
		req.setAttribute("password", password);
		
		resp.sendRedirect(req.getContextPath() + "/index");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String remember = req.getParameter("remember");
		     
		//Chuan bi cau truy van SQL Injection
		String query = "SELECT * \n"
				+ "FROM users u\n"
				+ "WHERE u.email = ? AND u.password = ?";
		
		//Mo ket noi CSDL
		Connection connection = MySQLConfig.getConnection();
		
		//Truyen cau try van vao connection moi vua ket noi
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);	
			
			//set tham so cho dau cham hoi ben trong cau query
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			/*
			 * excuteQuery : SELECT
			 * excuteUpdate: khong phai la cau select
			 * 
			 * */			
			ResultSet resultSet = preparedStatement.executeQuery();
			List<Users> listUsers = new ArrayList<Users>();
			
			while(resultSet.next()) {
				Users users = new Users();
				users.setId(resultSet.getInt("id"));
				users.setFullName(resultSet.getString("fullName"));
				users.setRoleId(resultSet.getInt("role_id"));

				listUsers.add(users);
			}
			
			if(listUsers.isEmpty()) {
				System.out.println("Đăng nhập thất bai ");
			}else {
				System.out.println("Đăng nhập thành công");
				
				Cookie cRole = new Cookie("role", listUsers.get(0).getRoleId() + "");
				cRole.setMaxAge(8 * 60 * 60);
				resp.addCookie(cRole); 
				
				Users userId = userService.getUserByEmail(email);
				Cookie cidUser = new Cookie("idUser", userId.getId() + "");
				cidUser.setMaxAge(8 * 60 * 60);
				resp.addCookie(cidUser); 
				
				if(remember != null) {
					//tạo cookie
					Cookie cEmail = new Cookie("email", email);
					cEmail.setMaxAge(1 * 60); // 1 * 60 * 60 * 1000
					
					Cookie cPassword = new Cookie("password", password);
					cPassword.setMaxAge(1 * 60);
					
					//Bat client tao ra cookie
					resp.addCookie(cEmail);
					resp.addCookie(cPassword);
				}
			}
			
			
		} catch (Exception e) {
			System.out.println("Lỗi thực thi câu truy vấn : " + e.getMessage());
		}
		
		resp.sendRedirect(req.getContextPath() + "/index");
	}
}
