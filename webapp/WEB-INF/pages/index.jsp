<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
    <head>
        <title>ExcelImport for Bluesoft</title>
        <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.css"/>

        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="http://netdna.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet" />

        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script type="text/javascript" src="https://cdn.datatables.net/r/dt/dt-1.10.8/datatables.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    </head>

    <body>

        <div class="container">
            <div class="row" style="text-align: center;">
                <div class="col-lg-2 vcenter">
                    <!--<h2>ExcelImport project for</h2>-->
                    <img src="http://www.bluesoft.net.pl/wp-content/uploads/2013/12/logo4.png"/>
                </div><!--
                <!------
                -- TODO: Handle file size exception
                -------
                --><div class="col-lg-4 vcenter excel-input">
                    <p>Choose an Excel file to import:</p>
                    <form:form method="POST" action="/" enctype="multipart/form-data">
                        <span class="btn btn-default btn-file">
                            Browse <input type="file" name="excel" accept=".xlsx" style="display: inline;"/>
                        </span>
                        <input type="text" class="form-control filename" disabled/>
                        <input type="submit" class="btn btn-primary " value="Submit">
                    </form:form>
                </div>

                <div class="error">${error}</div>
            </div>

            <div class="row">
                <table id="table" class="table table-hover table-striped">
                    <thead>
                        <tr>
                            <th>System</th>
                            <th>Request</th>
                            <th>Order number</th>
                            <th>From date</th>
                            <th>To date</th>
                            <th>Amount</th>
                            <th>Amount type</th>
                            <th>Amount period</th>
                            <th>Authorization (%)</th>
                            <th>Active</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${rows}" var="row">
                            <tr class="table-row">
                                <td>${row.system}</td>
                                <td>${row.request}</td>
                                <td>${row.orderNumber}</td>
                                <td>${row.fromDate}</td>
                                <td>${row.toDate}</td>
                                <td>${row.amount}</td>
                                <td>${row.amountType}</td>
                                <td>${row.amountPeriod}</td>
                                <td>${row.authPercent}</td>
                                <td>${row.active}</td>
                                <td><a href="javascript:;" onclick="deleteRow(this)" data-id="${row.contractId}">x</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

        <div class="row">
            <form:form id="form" commandName="row">
                <form:input path="system" type="text" placeholder="System"/>
                <form:input path="request" type="text" placeholder="Request"/>
                <form:input path="orderNumber" type="text" placeholder="Order number"/>
                <form:input path="fromDate" type="text" placeholder="From date"/>
                <form:input path="toDate" type="text" placeholder="To date"/>
                <form:input path="amount" type="text" placeholder="Amount"/>
                <form:input path="amountType" type="text" placeholder="Amount type"/>
                <form:input path="amountPeriod" type="text" placeholder="Amount period"/>
                <form:input path="authPercent" type="text" placeholder="Authorization percent"/>
                <form:input path="active" type="text" placeholder="Active"/>
                <form:input path="contractId" type="hidden" value="-1"/>
            </form:form>


                <input type="submit" value="Add" onclick="addRow(this)"/>
            </div>
        </div>

        <script>
            $(document).ready(function() {
                $("#table").dataTable();

                /*
                $(".row").click(function() {
                    $(this).children().each(function (index, item) {
                        $(item).html('<input type="text" value="'+$(item).html()+'"/>');
                    })
                });*/
            });

            $(function() {
                $("input:file").change(function (){
                    var fileName = $(this).val();
                    $(".filename").val(fileName);
                });
            });

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

            function addRow(item)
            {
                $.ajax({
                    type:"POST",
                    data: $("#form").serialize(),
                    url:"addRow",
                    success: function()
                    {
                        window.location.href = "./";
                    }
                });
            }
        </script>
    </body>
</html>