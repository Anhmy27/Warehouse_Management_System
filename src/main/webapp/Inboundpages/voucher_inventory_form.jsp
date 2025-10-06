<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Voucher" %>
<%@ page import="model.VoucherDetail" %>
<%
    Voucher v = (Voucher) request.getAttribute("voucher");
    List<VoucherDetail> details = (List<VoucherDetail>) request.getAttribute("details");
%>
<html>
<head>
    <title>Inventory Check for Voucher <%= v!=null? v.getVoucherId():"" %></title>
</head>
<body>
<h2>Inventory Check - Voucher: <%= v!=null? v.getVoucherId():"" %></h2>
<form action="<%= request.getContextPath()%>/voucher" method="post">
    <input type="hidden" name="action" value="saveInventoryVoucher" />
    <input type="hidden" name="sourceVoucherId" value="<%= v!=null? v.getVoucherId():"" %>" />
    <label>Created By (user id): <input type="text" name="createdBy" value="<%= v!=null? v.getCreatedBy():"" %>" required/></label><br/>
    <label>Note: <input type="text" name="note"/></label>
    <hr/>
    <table border="1">
        <tr>
            <th>Product</th>
            <th>Expected</th>
            <th>Actual</th>
            <th>Price</th>
        </tr>
        <%
            if (details!=null) for (VoucherDetail d: details) {
        %>
        <tr>
            <td><input type="hidden" name="productId" value="<%= d.getProductId()%>"/> <%= d.getProductId()%></td>
            <td><input type="number" name="qtyExpected" value="<%= d.getQuantityExpected()%>"/></td>
            <td><input type="number" name="qtyActual" value="<%= d.getQuantityActual()%>"/></td>
            <td><input type="text" name="price" value="<%= d.getPrice()%>"/></td>
        </tr>
        <% } %>
    </table>
    <button type="submit">Save Inventory Voucher</button>
</form>
</body>
</html>