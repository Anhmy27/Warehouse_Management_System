<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Orders" %>
<%@ page import="java.util.List" %>
<%@ page import="model.OrderDetail" %>
<html>
<head>
    <title>Order Detail</title>
</head>
<body>
<%
    Orders order = (Orders) request.getAttribute("order");
    List<OrderDetail> details = (List<OrderDetail>) request.getAttribute("details");
%>
<h2>Order: <%= order != null ? order.getOrderId() : "-" %></h2>
<p>Status: <%= order != null ? order.getStatus() : "-" %></p>
<p>Created By: <%= order != null ? (order.getCreatedByName()!=null?order.getCreatedByName():order.getCreatedBy()) : "-" %></p>
<p>Assigned To: <%= order != null ? (order.getAssignedToName()!=null?order.getAssignedToName():order.getAssignedTo()) : "-" %></p>
<p>Note: <%= order != null ? order.getNote() : "-" %></p>
<hr/>
<table border="1" cellpadding="6" cellspacing="0">
    <tr>
        <th>Product</th>
        <th>Image</th>
        <th>Expected Qty</th>
        <th>Actual Qty</th>
        <th>Price</th>
    </tr>
    <%
        if (details != null) {
            for (OrderDetail d : details) {
    %>
    <tr>
        <td><%= d.getProductName() %></td>
        <td><img src="<%= d.getProductImg()==null?"#":(request.getContextPath()+"/images/"+d.getProductImg()) %>" width="60"/></td>
        <td><%= d.getQuantityExpected() %></td>
        <td><%= d.getQuantityActual() %></td>
        <td><%= d.getPrice() %></td>
    </tr>
    <%      }
        }
    %>
</table>
<hr/>
<% if (order != null && "processing".equalsIgnoreCase(order.getStatus())) { %>
    <a href="<%= request.getContextPath() %>/voucher?action=createInbound&orderId=<%= order.getOrderId()%>">Create Inbound Voucher</a>
<% } %>

<a href="<%= request.getContextPath() %>/orders">Back to list</a>
</body>
</html>