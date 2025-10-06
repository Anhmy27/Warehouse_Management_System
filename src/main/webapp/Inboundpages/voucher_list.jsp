<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Voucher" %>
<html>
<head>
    <title>Voucher List</title>
</head>
<body>
<h2>Vouchers</h2>
<p><a href="<%= request.getContextPath()%>/voucher?action=list">Refresh</a></p>
<table border="1">
    <tr>
        <th>Voucher ID</th>
        <th>Type</th>
        <th>Order ID</th>
        <th>Created By</th>
        <th>Created At</th>
        <th>Actions</th>
    </tr>
    <%
        List<Voucher> vs = (List<Voucher>) request.getAttribute("vouchers");
        if (vs!=null) for (Voucher v: vs) {
    %>
    <tr>
        <td><%= v.getVoucherId() %></td>
        <td><%= v.getType() %></td>
        <td><%= v.getOrderId() %></td>
        <td><%= v.getCreatedBy() %></td>
        <td><%= v.getCreatedAt() %></td>
        <td>
            <a href="<%= request.getContextPath()%>/voucher?action=inventoryForm&voucherId=<%= v.getVoucherId()%>">Inventory Check</a>
        </td>
    </tr>
    <% } %>
</table>
</body>
</html>