package com.happydentalhub.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.happydentalhub.dao.UserDAO;
import com.happydentalhub.model.User;
import com.happydentalhub.util.PasswordUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email    = val(request.getParameter("email")).toLowerCase();
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");
        String next     = val(request.getParameter("next"));

        // only allow simple page names like "contact.html" (prevents open redirects)
        if (!next.matches("^[A-Za-z0-9_\\-]+\\.html$")) {
            next = "Happy.html";
        }

        try {
            User u = new UserDAO().findByEmail(email);
            if (u == null || !PasswordUtil.verify(password, u.getPasswordHash())) {
                response.sendRedirect("login.html?error="
                        + URLEncoder.encode("Invalid email or password.", "UTF-8")
                        + "&next=" + URLEncoder.encode(next, "UTF-8"));
                return;
            }

            // start a fresh session for the logged-in user
            HttpSession old = request.getSession(false);
            if (old != null) old.invalidate();
            HttpSession session = request.getSession(true);
            u.setPasswordHash(null); // never keep the hash in the session
            session.setAttribute("user", u);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            response.sendRedirect(next);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.html?error="
                    + URLEncoder.encode("Server error. Please try again later.", "UTF-8"));
        }
    }

    private static String val(String s) { return s == null ? "" : s.trim(); }
}