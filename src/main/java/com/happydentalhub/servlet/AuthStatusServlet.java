package com.happydentalhub.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.happydentalhub.model.User;

@WebServlet("/authStatus")
public class AuthStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");

        HttpSession session = request.getSession(false);
        User u = (session == null) ? null : (User) session.getAttribute("user");

        if (u == null) {
            response.getWriter().write("{\"loggedIn\":false}");
        } else {
            response.getWriter().write("{\"loggedIn\":true,"
                    + "\"name\":\"" + esc(u.getFullName()) + "\","
                    + "\"email\":\"" + esc(u.getEmail()) + "\","
                    + "\"phone\":\"" + esc(u.getPhone()) + "\"}");
        }
    }

    private static String esc(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}