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
                </div>

                <div class="col-lg-4 vcenter excel-input">
                    <p>Choose an Excel file to import:</p>
                    <form:form method="POST" action="./" enctype="multipart/form-data">
                        <span class="btn btn-default btn-file">
                            Browse <input type="file" name="excel" accept=".xlsx" style="display: inline;"/>
                        </span>
                        <input type="text" class="form-control filename" disabled/>
                        <input type="submit" class="btn btn-primary" value="Submit">
                    </form:form>
                </div>

                <div id="upload-success" class="alert alert-success" style="display: none;">
                    <div style="font-size: 18px;"><span id="success-msg">${success}</span></div>
                </div>

                <div id="upload-error" class="alert alert-danger" style="display: none;">
                    <div style="font-size: 18px;"><strong>Error!</strong> <span id="error-msg">${error}</span></div>
                </div>

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
                                <td><a href="javascript:;" onclick="confirmDeletion(${row.contractId}, event)" data-id="${row.contractId}"><button class="btn btn-danger btn-xs">Delete</button></a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <div class="row new-form">
                <form:form class="form-inline" role="form" id="form" commandName="row">
                    <div class="form-group">
                        <form:input data-type="string" data-length="50" path="system" type="text" class="form-control" placeholder="System" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="string" data-length="12" path="request" type="text" class="form-control" placeholder="Request" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="string" data-length="12" path="orderNumber" type="text" class="form-control" placeholder="Order number" style="width: 120px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="date" path="fromDate" type="date" class="form-control" placeholder="From date" style="width: 160px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="date" path="toDate" type="date" class="form-control" placeholder="To date" style="width: 160px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="float" path="amount" type="text" class="form-control" placeholder="Amount" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="string" data-length="5" path="amountType" type="text" class="form-control" placeholder="Am. type" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="string" data-length="5" path="amountPeriod" type="text" class="form-control" placeholder="Am. period" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="float" path="authPercent" type="text" class="form-control" placeholder="Auth %" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input data-type="string" path="active" type="text" class="form-control" placeholder="Active" style="width: 80px;"/>
                    </div>
                    <div class="form-group">
                        <form:input id="contract-id" data-type="string" path="contractId" type="hidden" value="0"/>
                    </div>
                </form:form>

                <input id="add-button" class="btn btn-primary" type="submit" value="Add" onclick="saveOrUpdateRow(this)"/>

                <div id="row-error" class="alert alert-danger" style="display: none;">
                    <div><strong>Error!</strong> <span></span></div>
                </div>

            </div>
        </div>

        <!-- Modal -->
        <div id="deleteRowModal" class="modal fade" role="dialog" data-id="-1">
            <div class="modal-dialog">

                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Warning</h4>
                    </div>
                    <div class="modal-body">
                        <p>Are you sure you want to delete this row?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-warning" onclick="deleteRow($('#deleteRowModal').data('id'))">Delete</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    </div>
                </div>

            </div>
        </div>






        <script>
            $(document).ready(function() {
                $("#table").dataTable({
                    "aoColumnDefs": [
                        { 'bSortable': false, 'aTargets': [ 10 ] }
                    ]
                });

                if ($.trim( $('#error-msg').html() ).length) {
                    {
                        $("#upload-error").css("display", "block");
                    }
                }

                if ($.trim( $('#success-msg').html() ).length) {
                    {
                        $("#upload-success").css("display", "block");
                    }
                }
            });

            $(function() {
                $("input:file").change(function (){
                    var fileName = $(this).val();
                    $(".filename").val(fileName);
                });
            });

            function confirmDeletion(index, event)
            {
                event.stopPropagation();

                $("#deleteRowModal").data("id", index).modal();
            }

            function deleteRow(index)
            {
                $.ajax({
                    method: 'POST',
                    url: 'deleteRow',
                    data: {id: index},
                    success: function()
                    {
                        window.location.reload(true);
                    }
                });
            }

            function saveOrUpdateRow(item)
            {
                if(validateForm())
                {
                     $.ajax({
                         type:"POST",
                         data: $("#form").serialize(),
                         url:"saveOrUpdateRow",
                         success: function(msg)
                         {
                             if(msg == "success")
                                 window.location.reload(true);
                             if(msg == "system-error")
                                 showSystemError();
                         }
                     });
                }


            }

            function validateForm()
            {
                var array = $("#form").children().toArray();

                var valid = true;

                $("#row-error").css({display: "none"});
                $("#row-error span").html("Correct marked fields to proceed.<br />");

                for(var i = 0; i < array.length; i++)
                {
                    var type = $(array[i]).children().first().data("type");
                    var value = $(array[i]).children().first().val();
                    var name = $(array[i]).children().first().attr('placeholder');


                    if(type == "string")
                    {
                        if(value == "") {
                            $(array[i]).removeClass("has-success");
                            $(array[i]).addClass("has-error");
                            $("#row-error span").append("Field " + name + " cannot be empty.<br />");
                            valid = false;
                        }
                        else
                        {
                            if(value.length > $(array[i]).children().first().data('length'))
                            {
                                var max = $(array[i]).children().first().data('length');

                                $(array[i]).removeClass("has-success");
                                $(array[i]).addClass("has-error");
                                $("#row-error span").append("Field " + name + " cannot be longer than " + max + " characters<br />");
                            }
                            else {
                                $(array[i]).removeClass("has-error");
                                $(array[i]).addClass("has-success");
                            }
                        }

                    }
                    else if(type == "float")
                    {
                        if(isNaN(value)) {
                            $(array[i]).removeClass("has-success");
                            $(array[i]).addClass("has-error");
                            $("#row-error span").append("Field " + name + " has to be a number.<br />");
                            valid = false;
                        }
                        else
                        {
                            $(array[i]).removeClass("has-error");
                            $(array[i]).addClass("has-success");
                        }
                    }
                    else if(type == "date")
                    {
                        if(!value) {
                            $(array[i]).removeClass("has-success");
                            $(array[i]).addClass("has-error");
                            $("#row-error span").append("Field " + name + " cannot be empty.<br />");
                            valid = false;
                        }
                        else
                        {
                            $(array[i]).removeClass("has-error");
                            $(array[i]).addClass("has-success");
                        }
                    }
                }

                if(!valid)
                    $("#row-error").slideDown();

                return valid;
            }

            function showSystemError()
            {
                $("#row-error").css({display: "none"});
                $("#row-error span").html("Specified system was not found in database");
                $("#row-error").slideDown();

            }


            function rowClick(item)
            {
                $("#contract-id").val($(item).data("id"));
                $("#add-button").val("Save");

                var inputs = $("#form").children().toArray();
                var cells = $(item).children().toArray();

                for(var i = 0; i < 10; i++)
                {
                    $(inputs[i]).children().first().val($(cells[i]).html());
                }
            }
        </script>
    </body>
</html>