/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.warehouse.login;

import dal.UserDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author LEGION
 */
@WebServlet(name = "LoginSystemServlet", urlPatterns = {"/login"})
public class LoginSystemServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginSystemServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginSystemServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/access/page-login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String remember = request.getParameter("rememberMe");

        UserDAO dao = new UserDAO();
        User user = dao.loginByEmail(email, password);

        if (user != null) {
            // Lưu thông tin user vào session
            HttpSession session = request.getSession();
            session.setAttribute("account", user);

            // Nếu chọn Remember Me => lưu email và password vào cookie
            if (remember != null && remember.equals("true")) {
                Cookie cEmail = new Cookie("email", email);
                Cookie cPass = new Cookie("password", password);
                Cookie cRem = new Cookie("rememberMe", "true");

                // Set thời gian sống cho cookie (7 ngày)
                cEmail.setMaxAge(7 * 24 * 60 * 60);
                cPass.setMaxAge(7 * 24 * 60 * 60);
                cRem.setMaxAge(7 * 24 * 60 * 60);

                response.addCookie(cEmail);
                response.addCookie(cPass);
                response.addCookie(cRem);
            } else {
                // Nếu không chọn => xóa cookie cũ
                Cookie cEmail = new Cookie("email", "");
                Cookie cPass = new Cookie("password", "");
                Cookie cRem = new Cookie("rememberMe", "false");

                cEmail.setMaxAge(0);
                cPass.setMaxAge(0);
                cRem.setMaxAge(0);

                response.addCookie(cEmail);
                response.addCookie(cPass);
                response.addCookie(cRem);
            }

            // Chuyển hướng về trang home
            response.sendRedirect("index.jsp");
        } else {
            // Sai email hoặc password
            request.setAttribute("error", "Email hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("views/access/page-login.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
