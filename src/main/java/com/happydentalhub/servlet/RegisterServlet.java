package com.happydentalhub.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.happydentalhub.dao.UserDAO;
import com.happydentalhub.model.User;
import com.happydentalhub.util.PasswordUtil;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name     = val(request.getParameter("name"));
        String email    = val(request.getParameter("email")).toLowerCase();
        String phone    = val(request.getParameter("phone"));
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");
        String confirm  = request.getParameter("confirm") == null ? "" : request.getParameter("confirm");

        String error = null;
        if (!name.matches("^[A-Za-z]+(\\s[A-Za-z]+)*$") || name.length() > 50) {
            error = "Name can contain only letters and single spaces (max 50).";
        } else if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$") || email.length() > 50) {
            error = "Please enter a valid email address.";
        } else if (!phone.matches("^[6-9]\\d{9}$")) {
            error = "Phone number must be 10 digits and start with 6, 7, 8 or 9.";
        } else if (password.length() < 6) {
            error = "Password must be at least 6 characters.";
        } else if (!password.equals(confirm)) {
            error = "Passwords do not match.";
        }

        if (error != null) {
            fail(response, error);
            return;
        }

        try {
            User u = new User();
            u.setFullName(name);
            u.setEmail(email);
            u.setPhone(phone);
            u.setPasswordHash(PasswordUtil.hash(password));

            if (new UserDAO().create(u)) {
                response.sendRedirect("login.html?registered=1");
            } else {
                fail(response, "This email is already registered. Please log in.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail(response, "Server error. Please try again later.");
        }
    }

    private static String val(String s) { return s == null ? "" : s.trim(); }

    private void fail(HttpServletResponse response, String msg) throws IOException {
        response.sendRedirect("register.html?error=" + URLEncoder.encode(msg, "UTF-8"));
    }
}