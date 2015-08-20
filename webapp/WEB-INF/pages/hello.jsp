<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
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

    </body>
</html>