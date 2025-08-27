package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(filterName = "authorFilter", urlPatterns = { "/user/*", "/rod/*", "/groupwork/*", "/task/*" })
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Cookie[] cookies = req.getCookies();
        int roleId = -1;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("role".equals(cookie.getName())) {
                    try {
                        roleId = Integer.parseInt(cookie.getValue()); 
                    } catch (NumberFormatException e) {
                        roleId = -1;
                    }
                }
            }
        }

        String uri = req.getRequestURI();

        // ✅ Admin
        if (roleId == 1) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ Manager (Leader)
        if (roleId == 2) {
            if (uri.contains("/groupwork") || uri.contains("/task") || uri.contains("/user")) {
                chain.doFilter(request, response);
                return;
            } else {
                resp.sendRedirect(req.getContextPath() + "/403.jsp");
                return;
            }
        }

        // ✅ User
        if (roleId == 3) {
            if (uri.contains("/task") || uri.contains("/user/profile")) {
                chain.doFilter(request, response);
                return;
            } else {
                resp.sendRedirect(req.getContextPath() + "/403.jsp");
                return;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/403.jsp");
    }
}
