<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Orders" %>
<html>
<head>
    <title>Inbound Orders</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/rack-detail.css" />
</head>
<body>
<nav>
    <a href="<%= request.getContextPath() %>/orders">Orders</a> |
    <a href="<%= request.getContextPath() %>/voucher?action=list">Vouchers</a> |
    <a href="<%= request.getContextPath() %>/report?action=inbound">Reports</a>
</nav>
<h2>Inbound Orders</h2>
<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>Order ID</th>
        <th>Type</th>
        <th>Created At</th>
    <th>Created By</th>
    <th>Assigned To</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    <%
        List<Orders> orders = (List<Orders>) request.getAttribute("ordersList");
        if (orders != null) {
            for (Orders o : orders) {
    %>
    <tr>
        <td><%= o.getOrderId() %></td>
        <td><%= o.getType() %></td>
        <td><%= o.getCreatedAt() %></td>
        <td><%= o.getCreatedByName()!=null?o.getCreatedByName():o.getCreatedBy() %></td>
        <td><%= o.getAssignedToName()!=null?o.getAssignedToName():o.getAssignedTo() %></td>
        <td><%= o.getStatus() %></td>
        <td>
            <a href="<%= request.getContextPath() %>/orders?action=detail&id=<%= o.getOrderId() %>">View</a>
            <% if ("pending".equalsIgnoreCase(o.getStatus())) { %>
                &nbsp;|&nbsp;
                <form action="<%= request.getContextPath() %>/orders" method="post" style="display:inline">
                    <input type="hidden" name="action" value="confirm" />
                    <input type="hidden" name="id" value="<%= o.getOrderId() %>" />
                    <button type="submit">Confirm</button>
                </form>
            <% } else if ("processing".equalsIgnoreCase(o.getStatus())) { %>
                &nbsp;|&nbsp;
                <a href="<%= request.getContextPath() %>/voucher?action=createInbound&orderId=<%= o.getOrderId() %>">Create Inbound Voucher</a>
            <% } %>
        </td>
    </tr>
    <%      }
        }
    %>
</table>
</body>
</html>