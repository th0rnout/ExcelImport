<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
    <head>
        <title>Spring MVC Form Handling</title>
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.css"/>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">

        <script type="text/javascript" src="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.js"></script>
        <style>
            input[type="text"] { width: 50px; }
        </style>
    </head>

    <body>

        <div style="text-align: center;">
            <h2>ExcelImport project for</h2>
            <img src="http://www.bluesoft.net.pl/wp-content/uploads/2013/12/logo4.png"/>

            <!------
            -- TODO: Handle file size exception
            ------->
            <p>Choose an Excel file to import:</p>
            <form:form method="POST" action="/" enctype="multipart/form-data">
                <input type="file" name="excel" accept=".xlsx" />
                <input type="submit" value="Submit"/>
            </form:form>
        </div>

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
                    <tr class="row">
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
                        <td><a href="javascript:;" onclick="deleteRow(this)" data-id="${contract.id}">x</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <script>
            $(document).ready(function() {
                $("#table").dataTable();

                function deleteRow(item)
                {
                    $.ajax({
                        method: 'POST',
                        url: 'deleteRow',
                        data: {id: $(item).data("id")},
                        success: function()
                        {
                            window.location.href = "./";
                        }
                    });
                }

                /*
                $(".row").click(function() {
                    $(this).children().each(function (index, item) {
                        $(item).html('<input type="text" value="'+$(item).html()+'"/>');
                    })
                });*/
            });
        </script>
    </body>
</html>