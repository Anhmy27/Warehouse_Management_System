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

@WebServlet(name = "VoucherController", urlPatterns = {"/voucher"})
public class VoucherController extends HttpServlet {

    private InboundDAO dao = new InboundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("createInbound".equals(action)) {
                String orderId = req.getParameter("orderId");
                List<OrderDetail> details = dao.getOrderDetailsByOrderId(orderId);
                model.Orders order = dao.getOrderById(orderId);
                req.setAttribute("orderId", orderId);
                req.setAttribute("details", details);
                // prefill createdBy with assignedTo (staff) if available, else createdBy
                if (order != null) {
                    String pre = order.getAssignedTo()!=null?order.getAssignedTo():order.getCreatedBy();
                    req.setAttribute("prefillCreatedBy", pre);
                }
                req.getRequestDispatcher("/Inboundpages/voucher_inbound_form.jsp").forward(req, resp);
                return;
            }
            if ("list".equals(action)) {
                req.setAttribute("vouchers", dao.listVouchers());
                req.getRequestDispatcher("/Inboundpages/voucher_list.jsp").forward(req, resp);
                return;
            }
            if ("inventoryForm".equals(action)) {
                String voucherId = req.getParameter("voucherId");
                model.Voucher v = dao.getVoucherById(voucherId);
                req.setAttribute("voucher", v);
                req.setAttribute("details", dao.getVoucherDetailsByVoucherId(voucherId));
                req.getRequestDispatcher("/Inboundpages/voucher_inventory_form.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("saveInboundVoucher".equals(action)) {
            try {
                saveInboundVoucher(req, resp);
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
            return;
        }
        if ("saveInventoryVoucher".equals(action)) {
            try {
                saveInventoryVoucher(req, resp);
            } catch (SQLException ex) {
                throw new ServletException(ex);
            }
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/orders");
    }

    private void saveInventoryVoucher(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        // create a new inventory voucher based on posted data
        String sourceVoucherId = req.getParameter("sourceVoucherId");
        String createdBy = req.getParameter("createdBy");
        String note = req.getParameter("note");
        // Instead of creating a new voucher, save inventory results into the existing voucher
        if (sourceVoucherId == null || sourceVoucherId.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sourceVoucherId");
            return;
        }
        String voucherId = sourceVoucherId;

        String[] productIds = req.getParameterValues("productId");
        String[] qtyExpected = req.getParameterValues("qtyExpected");
        String[] qtyActual = req.getParameterValues("qtyActual");
        String[] price = req.getParameterValues("price");

        if (productIds != null) {
            for (int i = 0; i < productIds.length; i++) {
                String pd = productIds[i];
                int qExp = Integer.parseInt(qtyExpected[i]);
                int qAct = Integer.parseInt(qtyActual[i]);
                double pr = Double.parseDouble(price[i]);
                double total = qAct * pr;
                String vdi = dao.nextId("VCD");
                dao.createVoucherDetail(vdi, voucherId, pd, qExp, qAct, pr, total);
            }
        }

        // Optionally update voucher metadata (note/createdBy) if desired - skipped here
        resp.sendRedirect(req.getContextPath() + "/voucher?action=list");
    }

    private void saveInboundVoucher(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String orderId = req.getParameter("orderId");
        String createdBy = req.getParameter("createdBy");
        String note = req.getParameter("note");
        String voucherId = dao.nextId("VCH");
        dao.createVoucher(voucherId, "inbound", orderId, createdBy, note);

        String[] productIds = req.getParameterValues("productId");
        String[] qtyExpected = req.getParameterValues("qtyExpected");
        String[] qtyActual = req.getParameterValues("qtyActual");
        String[] price = req.getParameterValues("price");

        if (productIds != null) {
            for (int i = 0; i < productIds.length; i++) {
                String pd = productIds[i];
                int qExp = Integer.parseInt(qtyExpected[i]);
                int qAct = Integer.parseInt(qtyActual[i]);
                double pr = Double.parseDouble(price[i]);
                double total = qAct * pr;
                String vdi = dao.nextId("VCD");
                dao.createVoucherDetail(vdi, voucherId, pd, qExp, qAct, pr, total);
            }
        }

        // after saving voucher, mark order done and redirect to orders list
        dao.markOrderDone(orderId);
        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
