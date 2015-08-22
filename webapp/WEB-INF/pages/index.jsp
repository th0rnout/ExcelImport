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
                -->
                <div class="col-lg-4 vcenter excel-input">
                    <p>Choose an Excel file to import:</p>
                    <form:form method="POST" action="/" enctype="multipart/form-data">
                        <span class="btn btn-default btn-file">
                            Browse <input type="file" name="excel" accept=".xlsx" style="display: inline;"/>
                        </span>
                        <input type="text" class="form-control filename" disabled/>
                        <input type="submit" class="btn btn-primary" value="Submit">
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
                            <th>Auth (%)</th>
                            <th>Active</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${rows}" var="row">
                            <tr onclick="rowClick(this)" data-id="${row.contractId}" class="table-row">
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
                                <td><a href="javascript:;" onclick="deleteRow(this)" data-id="${row.contractId}"><button class="btn btn-danger btn-xs">Delete</button></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="row new-form">
                <form:form id="form" commandName="row">
                    <form:input path="system" type="text" class="form-control" placeholder="System" style="width: 80px;"/>
                    <form:input path="request" type="text" class="form-control" placeholder="Request" style="width: 80px;"/>
                    <form:input path="orderNumber" type="text" class="form-control" placeholder="Order number" style="width: 120px;"/>
                    <form:input path="fromDate" type="date" class="form-control" placeholder="From date" style="width: 160px;"/>
                    <form:input path="toDate" type="date" class="form-control" placeholder="To date" style="width: 160px;"/>
                    <form:input path="amount" type="text" class="form-control" placeholder="Amount" style="width: 80px;"/>
                    <form:input path="amountType" type="text" class="form-control" placeholder="Amount type" style="width: 80px;"/>
                    <form:input path="amountPeriod" type="text" class="form-control" placeholder="Amount period" style="width: 80px;"/>
                    <form:input path="authPercent" type="text" class="form-control" placeholder="Auth percent" style="width: 80px;"/>
                    <form:input path="active" type="text" class="form-control" placeholder="Active" style="width: 80px;"/>
                    <form:input path="contractId" type="hidden" value="0"/>
                </form:form>

                <input id="add-button" class="btn btn-primary" type="submit" value="Add" onclick="saveOrUpdateRow(this)"/>

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

            function saveOrUpdateRow(item)
            {
                alert($("#form").serialize());

                $.ajax({
                    type:"POST",
                    data: $("#form").serialize(),
                    url:"saveOrUpdateRow",
                    success: function()
                    {
                        window.location.href = "./";
                    }
                });


                $("#contract-id").val(0);
                $("#add-button").val("Add");
                $("#form").children().each(function(item)
                {
                    $(item).val('');
                })
            }

            function rowClick(item)
            {
                $("#contract-id").val($(item).data("id"));
                $("#add-button").val("Save");

                var inputs = $("#form").children().toArray();
                var cells = $(item).children().toArray();

                for(var i = 0; i < 10; i++)
                {
                    $(inputs[i]).val($(cells[i]).html());
                }
            }
        </script>
    </body>
</html>