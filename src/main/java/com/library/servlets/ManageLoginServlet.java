package com.library.servlets;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/manageLogin")
public class ManageLoginServlet extends HttpServlet {
    private static final String ADMIN_PASSWORD = "123";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // just show the login form
        req.getRequestDispatcher("/views/manageLogin.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pw = req.getParameter("password");
        if (ADMIN_PASSWORD.equals(pw)) {
            req.getSession().setAttribute("isAdmin", true);
            resp.sendRedirect(req.getContextPath() + "/manageMember");
        } else {
            req.setAttribute("error", "Invalid password, please try again.");
            req.getRequestDispatcher("/views/manageLogin.jsp")
                    .forward(req, resp);
        }
    }
}
