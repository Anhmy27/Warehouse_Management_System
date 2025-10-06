<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP + Controller</title>
</head>
<body>
<h1>Warehouse Management</h1>
<nav>
    <a href="<%= request.getContextPath() %>/orders">Inbound Orders</a> |
    <a href="<%= request.getContextPath() %>/voucher?action=list">Vouchers</a> |
    <a href="<%= request.getContextPath() %>/report?action=inbound">Reports</a>
</nav>
<hr/>
<a href="rack-detail.jsp">Go to location</a>
</body>
</html>
