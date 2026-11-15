package com.happydentalhub.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.happydentalhub.dao.AppointmentDAO;
import com.happydentalhub.model.Appointment;

@WebServlet("/bookAppointment")
public class BookAppointmentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // These names must match the name="..." attributes in Contact.html
        String name    = trim(request.getParameter("name"));
        String phone   = trim(request.getParameter("phone"));
        String email   = trim(request.getParameter("email"));
        String service = trim(request.getParameter("service"));
        String doctor  = trim(request.getParameter("doctor"));
        String dateStr = trim(request.getParameter("date"));
        String timeStr = trim(request.getParameter("time"));
        String message = trim(request.getParameter("message"));

        String error = null;
        LocalDate date = null;

        if (name.isEmpty() || phone.isEmpty() || service.isEmpty()
                || dateStr.isEmpty() || timeStr.isEmpty()) {
            error = "Please fill in all required fields.";
        } else if (!phone.replaceAll("[\\s-]", "").matches("^\\+?[0-9]{10,13}$")) {
            error = "Please enter a valid phone number.";
        } else if (!email.isEmpty() && !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            error = "Please enter a valid email address.";
        } else {
            try {
                date = LocalDate.parse(dateStr);
                LocalTime time = parseTime(timeStr);

                if (date.isBefore(LocalDate.now())) {
                    error = "Preferred date cannot be in the past.";
                } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    error = "The clinic is closed on Sundays.";
                } else if (time.isBefore(LocalTime.of(9, 0))) {
                    error = "Please choose a time within working hours.";
                } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY
                        && !time.isBefore(LocalTime.of(14, 0))) {
                    error = "On Saturdays we are open from 9:00 AM to 2:00 PM.";
                } else if (!time.isBefore(LocalTime.of(17, 0))) {
                    error = "We are open until 5:00 PM on weekdays.";
                }
            } catch (Exception e) {
                error = "Invalid date or time.";
            }
        }

        if (error != null) {
            redirect(response, "error", error);
            return;
        }

        // Doctor name is saved at the start of the message column
        String fullMessage = doctor.isEmpty() ? message
                : "Doctor: " + doctor + (message.isEmpty() ? "" : " | " + message);

        Appointment a = new Appointment();
        a.setFullName(name);
        a.setPhone(phone);
        a.setEmail(email.isEmpty() ? null : email);
        a.setServiceType(service);
        a.setPreferredDate(date);
        a.setPreferredTime(timeStr);
        a.setMessage(fullMessage.isEmpty() ? null : fullMessage);

        try {
            if (new AppointmentDAO().save(a)) {
                redirect(response, "success", "Appointment saved.");
            } else {
                redirect(response, "error", "Could not save your appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // real error shows in the Eclipse Console
            redirect(response, "error", "Server error. Please try again later.");
        }
    }

    // Accepts "9:00", "14:00" or "02:00 PM"
    private static LocalTime parseTime(String t) {
        t = t.trim().toUpperCase();
        if (t.contains("AM") || t.contains("PM")) {
            return LocalTime.parse(t, DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH));
        }
        String[] p = t.split(":");
        return LocalTime.of(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private void redirect(HttpServletResponse response, String status, String msg)
            throws IOException {
        response.sendRedirect("contact.html?status=" + status + "&msg="
                + URLEncoder.encode(msg, "UTF-8"));
    }
}