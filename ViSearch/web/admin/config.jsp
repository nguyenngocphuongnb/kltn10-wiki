<%-- 
    Document   : admin
    Created on : Jul 6, 2010, 9:24:25 AM
    Author     : VinhPham
--%>

<%@page import="org.me.dto.ParameterDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" errorPage="" %>
<%@ page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Trang cấu hình</title>
        <link href="style.css" rel="stylesheet" type="text/css" />
        <link href="css/ui-lightness/jquery-ui-1.8.2.custom.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript">
            function OnSubmit(){
                // chua kiem tra loi
               
            }
            function Back(){
                window.location = "admin/admin.jsp";
            }
            function Reset(){
                window.location = "LoadSettingDefault";
            }
        </script>
    </head>

    <body>
        <div id="wrap_left" align="center">
            <div id="wrap_right">
                <table id="wrap" width="974" border="0" cellpadding="0" cellspacing="0">

                    <tr><td height="20" colspan="2" align="center" valign="middle"></td></tr>
                    <tr>
                        <td height="130" colspan="2" valign="top">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="974" valign="top">
                                        <!-- banner here !-->
                                        <table id="Table_01" width="975" height="130" border="0" cellpadding="0" cellspacing="0">
                                            <tr><td><img src="images/BannerAdmin.png" /></td></tr>
                                        </table>
                                        <!-- end banner      !-->
                                    </td>
                                </tr>
                            </table>
                        </td></tr>
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <!-- !-->
                    <tr>
                        <td colspan="2" height="33" valign="top" align="center">
                            <%
                                        if (request.getAttribute("ListParameter") != null) {
                                            ArrayList<ParameterDTO> list = new ArrayList<ParameterDTO>();
                                            list = (ArrayList<ParameterDTO>) request.getAttribute("ListParameter");
                            %>
                            <h1>Cấu hình cho hệ thống</h1>
                            <%
                                                                        if (request.getAttribute("Msg") != null) {
                                                                            String msg = (String) request.getAttribute("Msg");
                                                                            out.print("<h3><font color=red>" + msg + "</font></h3>");
                                                                        }
                            %>
                            <%
                                                                        if (request.getAttribute("Msg2") != null) {
                                                                            String msg = (String) request.getAttribute("Msg2");
                                                                            out.print("<h3><font color=red>" + msg + "</font></h3>");
                                                                        }
                            %>
                            <form action="SaveConfigController">
                                <table style="font-size:12px;" border="1px" cellpadding="0" cellspacing="0" width="60%" class="ui-widget-content">
                                    <tr>
                                        <th>Têm tham số</th>
                                        <th>Giá trị</th>
                                        <th>Mô tả</th>
                                    </tr>
                                    <% int i = 1;
                                                                                for (ParameterDTO par : list) {
                                    %>
                                    <tr>
                                        <td>
                                            <%=par.getName()%>
                                            <input type="hidden" id="hf<%out.print(par.getName());%>" value="<%=par.getId()%>"/>
                                        </td>
                                        <td>
                                            <%if (par.getName().equals("Value"))%>
                                            <input type="text" class="textForm" onfocus="this.className='textForm_Hover';" onblur="this.className='textForm';"  size="35" name="<%out.print(par.getName());%>" value="<%=par.getValue()%>"/>
                                        </td>

                                        <td>
                                            <%=par.getDecription()%>
                                        </td>
                                    </tr>
                                    <%
                                                                                    i++;
                                                                                }
                                    %>
                                </table>
                                <table>
                                    <tr>
                                        <td  align="center"><input type="submit" value="Lưu"/></td>
                                        <td  align="center"><input type="button" onclick="Back()" value="Hủy"/></td>
                                        <td  align="center"><input type="button" onclick="Reset()" value="Khôi phục cài đặt mặc định"/></td>
                                    </tr>

                                </table>

                            </form>
                            <%
                                        }
                            %>

                        </td>
                    </tr>
                            <tr><td align="middle" colspan="2"><p>
                                <a href="admin/admin.jsp">Trang chủ admin</a>
                            </p></td>
                    </tr>
                    <tr height="50"><td></td><td width="743"></td>
                    </tr>
                    <!-- -->
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <tr>
                        <td width="200"></td>
                        <td colspan="2" valign="top">
                            <%@include file="../template/footer.jsp"%>
                        </td>
                    </tr>
                </table>

            </div>
        </div>
    </body>
</html>


