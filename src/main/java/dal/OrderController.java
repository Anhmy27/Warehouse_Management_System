package dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.OrderDetail;
import model.Orders;

@WebServlet(name = "OrderController", urlPatterns = {"/orders"})
public class OrderController extends HttpServlet {

    private InboundDAO dao = new InboundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("detail".equals(action)) {
                showDetail(req, resp);
            } else {
                listOrders(req, resp);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("confirm".equals(action)) {
                confirmOrder(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        List<Orders> list = dao.listPendingOrders();
        req.setAttribute("ordersList", list);
        req.getRequestDispatcher("/Inboundpages/order_list.jsp").forward(req, resp);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String id = req.getParameter("id");
        if (id == null) {
            resp.sendRedirect(req.getContextPath() + "/orders");
            return;
        }
        Orders order = dao.getOrderById(id);
        List<OrderDetail> details = dao.getOrderDetailsByOrderId(id);
        req.setAttribute("order", order);
        req.setAttribute("details", details);
        req.getRequestDispatcher("/Inboundpages/order_detail.jsp").forward(req, resp);
    }

    private void confirmOrder(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String id = req.getParameter("id");
        if (id != null) {
            boolean ok = dao.updateOrderStatusToProcessing(id);
            if (ok) {
                resp.sendRedirect(req.getContextPath() + "/orders");
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
