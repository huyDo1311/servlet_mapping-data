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

@WebFilter(filterName = "authenFilter", urlPatterns = { "/*" })
public class AuthenticationFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String path = req.getRequestURI().substring(req.getContextPath().length());

		// Bỏ qua filter cho login.jsp và servlet xử lý login
		if (path.equals("/login.jsp") || path.equals("/login")) {
			chain.doFilter(request, response);
			return;
		}

		Cookie[] cookies = req.getCookies();
		int roleId = -1; // mặc định chưa login

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

		if (roleId == -1) {
			resp.sendRedirect(req.getContextPath() + "/login.jsp");
			return;
		}

		// đã login thì cho đi tiếp
		chain.doFilter(request, response);
	}
}
