package library.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/views/manage*")
public class ManageAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Boolean isAdmin = (Boolean) req.getSession().getAttribute("isAdmin");
        if (Boolean.TRUE.equals(isAdmin)) {
            // already logged in → continue to the requested manage* URL
            chain.doFilter(request, response);
        } else {
            // not yet authenticated → redirect to login
            resp.sendRedirect(req.getContextPath() + "/views/manageLogin.jsp");
        }
    }
}
