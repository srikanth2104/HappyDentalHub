package com.happydentalhub.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Pages and URLs anyone can open (lowercase, so file-name case does not matter)
    private static final Set<String> PUBLIC = new HashSet<>(Arrays.asList(
            "", "/", "/happy.html", "/index.html",
            "/login.html", "/register.html",
            "/login", "/register", "/logout", "/authstatus", "/auth.js"));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());
        String lower = path.toLowerCase();

        boolean isStatic = lower.matches(".*\\.(css|js|jpg|jpeg|png|gif|svg|webp|ico|woff2?|ttf)$");
        boolean isPublic = PUBLIC.contains(lower) || isStatic;

        HttpSession session = request.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("user") != null;

        if (isPublic || loggedIn) {
            response.setHeader("Cache-Control", "no-store"); // back button after logout won't show pages
            chain.doFilter(req, res);
            return;
        }

        // not logged in -> go to login, remember the page they wanted
        String next = path.startsWith("/") ? path.substring(1) : path;
        if (!next.matches("^[A-Za-z0-9_\\-]+\\.html$")) next = "";
        response.sendRedirect(request.getContextPath() + "/login.html?error="
                + URLEncoder.encode("Please log in to continue.", "UTF-8")
                + (next.isEmpty() ? "" : "&next=" + URLEncoder.encode(next, "UTF-8")));
    }
}