<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.OrderDetail" %>
<%
    String orderId = (String) request.getAttribute("orderId");
    List<OrderDetail> details = (List<OrderDetail>) request.getAttribute("details");
%>
<html>
<head>
    <title>Create Inbound Voucher</title>
</head>
<body>
<h2>Create Inbound Voucher for Order: <%= orderId %></h2>
<form action="<%= request.getContextPath() %>/voucher" method="post">
    <input type="hidden" name="action" value="saveInboundVoucher" />
    <input type="hidden" name="orderId" value="<%= orderId %>" />
    <%
        String pre = (String) request.getAttribute("prefillCreatedBy");
    %>
    <label>Created By (user id): <input type="text" name="createdBy" value="<%= pre!=null?pre:"" %>" required/></label><br/>
    <label>Note: <input type="text" name="note"/></label>
    <hr/>
    <table border="1" cellpadding="6" cellspacing="0">
        <tr>
            <th>Product</th>
            <th>Expected</th>
            <th>Actual</th>
            <th>Price</th>
        </tr>
        <%
            if (details != null) {
                for (OrderDetail d : details) {
        %>
        <tr>
            <td>
                <input type="hidden" name="productId" value="<%= d.getProductId() %>" />
                <%= d.getProductName() %>
            </td>
            <td><input type="number" name="qtyExpected" value="<%= d.getQuantityExpected() %>"/></td>
            <td><input type="number" name="qtyActual" value="<%= d.getQuantityExpected() %>"/></td>
            <td><input type="text" name="price" value="<%= d.getPrice() %>"/></td>
        </tr>
        <%      }
            }
        %>
    </table>
    <button type="submit">Save Voucher</button>
</form>

</body>
</html>