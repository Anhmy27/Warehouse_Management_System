package dal;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ReportController", urlPatterns = {"/report"})
public class ReportController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("inbound".equals(action)) {
            req.getRequestDispatcher("/Inboundpages/report_inbound.jsp").forward(req, resp);
            return;
        }
        req.getRequestDispatcher("/Inboundpages/report_stock.jsp").forward(req, resp);
    }
}
