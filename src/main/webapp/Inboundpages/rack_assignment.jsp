<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.VoucherDetail" %>
<%
    String voucherId = (String) request.getAttribute("voucherId");
    List<VoucherDetail> details = (List<VoucherDetail>) request.getAttribute("details");
%>
<html>
<head>
    <title>Rack Assignment</title>
</head>
<body>
<h2>Rack Assignment for Voucher: <%= voucherId %></h2>
<form action="<%= request.getContextPath() %>/rack" method="post">
    <input type="hidden" name="voucherId" value="<%= voucherId %>" />
    <table border="1">
        <tr>
            <th>Product</th>
            <th>Expected</th>
            <th>Actual</th>
            <th>Rack ID</th>
            <th>LotDetail ID</th>
            <th>Quantity to place</th>
        </tr>
        <%
            if (details != null) {
                for (VoucherDetail d : details) {
        %>
        <tr>
            <td><%= d.getProductId()%></td>
            <td><%= d.getQuantityExpected()%></td>
            <td><%= d.getQuantityActual()%></td>
            <td><input type="text" name="rackId" value=""/></td>
            <td><input type="text" name="lotdetailId" value=""/></td>
            <td>
                <input type="hidden" name="voucherDetailId" value="<%= d.getVoucherDetailId()%>"/>
                <input type="number" name="quantity" value="<%= d.getQuantityActual()%>"/>
            </td>
        </tr>
        <%      }
            }
        %>
    </table>
    <button type="submit">Save Assignment</button>
</form>
</body>
</html>