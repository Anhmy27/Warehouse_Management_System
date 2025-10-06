package dal;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "RackController", urlPatterns = {"/rack"})
public class RackController extends HttpServlet {

    private InboundDAO dao = new InboundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // show rack assignment form - expects voucherId
        String voucherId = req.getParameter("voucherId");
        req.setAttribute("voucherId", voucherId);
        try {
            req.setAttribute("details", dao.getVoucherDetailsByVoucherId(voucherId));
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        req.getRequestDispatcher("/Inboundpages/rack_assignment.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // process rack assignment
        String voucherId = req.getParameter("voucherId");
        String[] voucherDetailIds = req.getParameterValues("voucherDetailId");
        String[] rackIds = req.getParameterValues("rackId");
        String[] lotdetailIds = req.getParameterValues("lotdetailId");
        String[] quantities = req.getParameterValues("quantity");

        if (voucherDetailIds != null) {
            for (int i = 0; i < voucherDetailIds.length; i++) {
                String vd = voucherDetailIds[i];
                String rack = rackIds[i];
                String lot = lotdetailIds[i];
                int qty = Integer.parseInt(quantities[i]);
                try {
                    String racklotId = dao.nextId("RKL");
                    dao.createRackLot(racklotId, rack, lot, qty);
                    // TODO: look up product id from voucher detail - simplified approach: assume lotdetail links product
                    dao.updateLotDetailRemaining(lot, qty);
                    // For demo, we won't compute product id; in production lookup and update product quantities
                } catch (SQLException ex) {
                    throw new ServletException(ex);
                }
            }
        }

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
