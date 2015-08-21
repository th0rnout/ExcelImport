<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Spring MVC Form Handling</title>
    </head>

    <body>

        <h2>${type}</h2>

        <form:form method="POST" action="/" enctype="multipart/form-data">
            <input type="file" name="excel" accept=".xlsx" />
            <input type="submit" value="Submit"/>
        </form:form>

        <c:forEach items="${contracts}" var="contract">
            ${contract.id} |
            ${contract.active} |
            ${contract.amount} |
            ${contract.amountPeriod} |
            ${contract.amountType} |
            ${contract.authPercent} |
            ${contract.fromDate} |
            ${contract.orderNumber} |
            ${contract.request} |
            ${contract.toDate} |
            ${contract.systemId}<br />
        </c:forEach>
    </body>
</html>