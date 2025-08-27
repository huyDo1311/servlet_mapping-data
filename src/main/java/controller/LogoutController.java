package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "logoutController", urlPatterns = {"/logout"})
public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        // 1. Hủy session nếu có
        HttpSession session = req.getSession(false); // false: không tạo session mới
        if (session != null) {
            session.invalidate(); // xóa toàn bộ session
        }

        // 2. Xóa cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("email".equals(cookie.getName()) || 
                    "password".equals(cookie.getName()) || 
                    "role".equals(cookie.getName())) {
                    
                    cookie.setValue("");
                    cookie.setMaxAge(0); // hết hạn ngay lập tức
                    resp.addCookie(cookie);
                }
            }
        }

        // 3. Redirect về trang login
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
