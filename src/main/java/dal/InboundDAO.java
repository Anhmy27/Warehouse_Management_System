package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderDetail;
import model.Orders;
import model.User;
import model.Voucher;
import model.VoucherDetail;

/**
 * DAO for inbound order workflow.
 */
public class InboundDAO extends DBContext {

    public List<Orders> listPendingOrders() throws SQLException {
        List<Orders> list = new ArrayList<>();
    // Use SQL Server bracket quoting for reserved names like [user]
    String sql = "SELECT o.order_id, o.type, o.created_by, o.assigned_to, o.created_at, o.status, o.note, "
        + "u1.fullname as created_name, u2.fullname as assigned_name "
        + "FROM orders o LEFT JOIN [user] u1 ON o.created_by = u1.uid LEFT JOIN [user] u2 ON o.assigned_to = u2.uid "
        + "WHERE o.type = 'inbound' ORDER BY o.created_at DESC";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Orders o = new Orders();
                o.setOrderId(rs.getString("order_id"));
                o.setType(rs.getString("type"));
                o.setCreatedBy(rs.getString("created_by"));
                o.setAssignedTo(rs.getString("assigned_to"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                o.setStatus(rs.getString("status"));
                o.setNote(rs.getString("note"));
                o.setCreatedByName(rs.getString("created_name"));
                o.setAssignedToName(rs.getString("assigned_name"));
                list.add(o);
            }
        }
        return list;
    }

    public Orders getOrderById(String orderId) throws SQLException {
    String sql = "SELECT o.order_id, o.type, o.created_by, o.assigned_to, o.created_at, o.scheduled_date, "
            + "o.status, o.note, u1.fullname AS created_name, u2.fullname AS assigned_name "
            + "FROM [orders] o "
            + "LEFT JOIN [user] u1 ON o.created_by = u1.uid "
            + "LEFT JOIN [user] u2 ON o.assigned_to = u2.uid "
            + "WHERE o.order_id = ?";
    
    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, orderId);
        try (ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                Orders o = new Orders();
                o.setOrderId(rs.getString("order_id"));
                o.setType(rs.getString("type"));
                o.setCreatedBy(rs.getString("created_by"));
                o.setAssignedTo(rs.getString("assigned_to"));
                o.setCreatedAt(rs.getTimestamp("created_at"));
                o.setScheduledDate(rs.getDate("scheduled_date"));
                o.setStatus(rs.getString("status"));
                o.setNote(rs.getString("note"));
                o.setCreatedByName(rs.getString("created_name"));
                o.setAssignedToName(rs.getString("assigned_name"));
                return o;
            }
        }
    }
    return null;
}


    public List<OrderDetail> getOrderDetailsByOrderId(String orderId) throws SQLException {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.orderdetail_id, od.order_id, od.product_id, p.name as product_name, p.img as product_img, "
                + "od.quantity_expected, od.quantity_actual, od.price "
                + "FROM orderdetail od LEFT JOIN product p ON od.product_id = p.productid WHERE od.order_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setOrderDetailId(rs.getString("orderdetail_id"));
                    od.setOrderId(rs.getString("order_id"));
                    od.setProductId(rs.getString("product_id"));
                    od.setProductName(rs.getString("product_name"));
                    od.setProductImg(rs.getString("product_img"));
                    od.setQuantityExpected(rs.getInt("quantity_expected"));
                    od.setQuantityActual(rs.getInt("quantity_actual"));
                    od.setPrice(rs.getDouble("price"));
                    list.add(od);
                }
            }
        }
        return list;
    }

    public boolean updateOrderStatusToProcessing(String orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'processing' WHERE order_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            int rows = st.executeUpdate();
            return rows > 0;
        }
    }

    // Voucher related operations (use GETDATE() for SQL Server)
    public String createVoucher(String voucherId, String type, String orderId, String createdBy, String note) throws SQLException {
        String sql = "INSERT INTO voucher(voucher_id, order_id, voucher_code, type, created_date, note, created_by) "
                + "VALUES(?, ?, ?, ?, GETDATE(), ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, voucherId);
            st.setString(2, orderId);
            st.setString(3, voucherId); // use id as code for simplicity
            st.setString(4, type);
            st.setString(5, note);
            st.setString(6, createdBy);
            st.executeUpdate();
            return voucherId;
        }
    }

    public void markOrderDone(String orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'done' WHERE order_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, orderId);
            st.executeUpdate();
        }
    }

    public void createVoucherDetail(String voucherDetailId, String voucherId, String productId, int qtyExpected, int qtyActual, double price, double total) throws SQLException {
        String sql = "INSERT INTO voucher_detail(voucher_detail_id, voucher_id, product_id, quantity_expected, quantity_actual, price, total) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, voucherDetailId);
            st.setString(2, voucherId);
            st.setString(3, productId);
            st.setInt(4, qtyExpected);
            st.setInt(5, qtyActual);
            st.setDouble(6, price);
            st.setDouble(7, total);
            st.executeUpdate();
        }
    }

    // helper ID generator
    public String nextId(String prefix) {
        return prefix + System.currentTimeMillis();
    }

    public List<VoucherDetail> getVoucherDetailsByVoucherId(String voucherId) throws SQLException {
        List<VoucherDetail> list = new ArrayList<>();
        String sql = "SELECT vd.voucher_detail_id, vd.voucher_id, vd.product_id, vd.quantity_expected, vd.quantity_actual, vd.price, vd.total, "
                + "p.name as product_name, p.img as product_img "
                + "FROM voucher_detail vd LEFT JOIN product p ON vd.product_id = p.productid WHERE vd.voucher_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, voucherId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    VoucherDetail vd = new VoucherDetail();
                    vd.setVoucherDetailId(rs.getString("voucher_detail_id"));
                    vd.setVoucherId(rs.getString("voucher_id"));
                    vd.setProductId(rs.getString("product_id"));
                    vd.setQuantityExpected(rs.getInt("quantity_expected"));
                    vd.setQuantityActual(rs.getInt("quantity_actual"));
                    vd.setPrice(rs.getDouble("price"));
                    vd.setTotal(rs.getDouble("total"));
                    vd.setProductName(rs.getString("product_name"));
                    vd.setProductImg(rs.getString("product_img"));
                    list.add(vd);
                }
            }
        }
        return list;
    }

    public List<Voucher> listVouchers() throws SQLException {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT voucher_id, type, order_id, created_by, created_date, note FROM voucher ORDER BY created_date DESC";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherId(rs.getString("voucher_id"));
                v.setType(rs.getString("type"));
                v.setOrderId(rs.getString("order_id"));
                v.setCreatedBy(rs.getString("created_by"));
                v.setCreatedAt(rs.getTimestamp("created_date"));
                v.setNote(rs.getString("note"));
                list.add(v);
            }
        }
        return list;
    }

    public Voucher getVoucherById(String voucherId) throws SQLException {
        String sql = "SELECT voucher_id, type, order_id, created_by, created_date, note FROM voucher WHERE voucher_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, voucherId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setVoucherId(rs.getString("voucher_id"));
                    v.setType(rs.getString("type"));
                    v.setOrderId(rs.getString("order_id"));
                    v.setCreatedBy(rs.getString("created_by"));
                    v.setCreatedAt(rs.getTimestamp("created_date"));
                    v.setNote(rs.getString("note"));
                    return v;
                }
            }
        }
        return null;
    }

    public void createRackLot(String racklotId, String rackId, String lotdetailId, int quantity) throws SQLException {
        String sql = "INSERT INTO racklot(racklot_id, rack_id, lotdetail_id, quantity) VALUES(?, ?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, racklotId);
            st.setString(2, rackId);
            st.setString(3, lotdetailId);
            st.setInt(4, quantity);
            st.executeUpdate();
        }
    }

    public void updateLotDetailRemaining(String lotdetailId, int delta) throws SQLException {
        // use COALESCE for MySQL compatibility
        String sql = "UPDATE lotdetail SET quantity_remaining = COALESCE(quantity_remaining,0) - ? WHERE lotdetail_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, delta);
            st.setString(2, lotdetailId);
            st.executeUpdate();
        }
    }

    public void updateProductQuantities(String productId, int deltaSystem, int deltaReal) throws SQLException {
        String sql = "UPDATE product SET quantityatsystem = COALESCE(quantityatsystem,0) + ?, quantityatreal = COALESCE(quantityatreal,0) + ? WHERE productid = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, deltaSystem);
            st.setInt(2, deltaReal);
            st.setString(3, productId);
            st.executeUpdate();
        }
    }

    public String getProductIdByLotDetailId(String lotdetailId) throws SQLException {
        String sql = "SELECT product_id FROM lotdetail WHERE lotdetail_id = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, lotdetailId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("product_id");
                }
            }
        }
        return null;
    }

    public void updateRackLoad(String rackId, int delta) throws SQLException {
        // update current load and set status accordingly (simple logic)
        String sql = "UPDATE rack SET current_load = COALESCE(current_load,0) + ?, "
                + "status = CASE WHEN COALESCE(current_load,0) + ? >= COALESCE(capacity,0) THEN 'full' ELSE 'available' END "
                + "WHERE rackid = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, delta);
            st.setInt(2, delta);
            st.setString(3, rackId);
            st.executeUpdate();
        }
    }
}
