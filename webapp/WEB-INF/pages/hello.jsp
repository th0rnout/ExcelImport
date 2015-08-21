<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
    <head>
        <title>Spring MVC Form Handling</title>
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.css"/>

        <script type="text/javascript" src="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.js"></script>
    </head>

    <body>

        <h2>${type}</h2>

        <form:form method="POST" action="/" enctype="multipart/form-data">
            <input type="file" name="excel" accept=".xlsx" />
            <input type="submit" value="Submit"/>
        </form:form>

        <table id="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Active</th>
                    <th>Amount</th>
                    <th>Amount period</th>
                    <th>Amount type</th>
                    <th>Authentication (%)</th>
                    <th>From date</th>
                    <th>Order number</th>
                    <th>Request</th>
                    <th>To date</th>
                    <th>System ID</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${contracts}" var="contract">
                    <tr>
                        <td>${contract.id}</td>
                        <td>${contract.active}</td>
                        <td>${contract.amount}</td>
                        <td>${contract.amountPeriod}</td>
                        <td>${contract.amountType}</td>
                        <td>${contract.authPercent}</td>
                        <td>${contract.fromDate}</td>
                        <td>${contract.orderNumber}</td>
                        <td>${contract.request}</td>
                        <td>${contract.toDate}</td>
                        <td>${contract.systemId}</td>
                        <td><a href="javascript:;" class="deleteRow" data-id="${contract.id}">x</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <script>
            $(document).ready(function() {
                $("#table").dataTable();

                $(".deleteRow").click(function()
                {
                    $.ajax({
                        method: 'POST',
                        url: 'deleteRow',
                        data: {id: $(this).data("id")},
                        success: function()
                        {
                            window.location.href = "./";
                        }
                    });
                })
            });
        </script>
    </body>
</html>