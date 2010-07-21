<%-- 
    Document   : sortedtype
    Created on : Jun 4, 2010, 3:59:49 PM
    Author     : VinhPham
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div align="right" style="font-size:13px;">
        Đánh giá kết quả:
        <%
            int type = 0;
            if(request.getAttribute("Type")!=null)
                type = Integer.parseInt(request.getAttribute("Type").toString());
        %>
        <select name="slSortedType" id="slSortedType" onChange="Sort(<%=type%>);">
            <option value="0">Ưu tiên độ liên quan</option>
            <option value="1">Ưu tiên ngày gần hơn</option>
            <option value="2">Ưu tiên lịch sử tìm kiếm</option>
            <option value="3">Sử dụng tách từ tiếng Việt</option>
        </select>
        <input id="hfSortedType" type="hidden" value="<%
                    out.print(sortedType);
               %>"/>
        </div>
        <script type="text/javascript">
            var slST = document.getElementById("slSortedType");
            var arrOpt = slST.getElementsByTagName("option");
            var vl = document.getElementById("hfSortedType").value;
            var i;
            for(i = 0; i< arrOpt.length; i++)
            {
                if(arrOpt[i].value == vl)
                    arrOpt[i].selected = true;
            }
        </script>
    </body>
</html>
