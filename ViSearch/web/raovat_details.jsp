<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apache.solr.client.solrj.SolrQuery"%>
<%@page import="org.apache.solr.client.solrj.SolrServer"%>
<%@page import="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer"%>
<%@page import="org.apache.solr.common.SolrDocument"%>
<%@page import="org.apache.solr.common.SolrDocumentList"%>
<%@page import="org.apache.solr.common.SolrInputDocument"%>
<%@page import="org.apache.solr.client.solrj.response.QueryResponse"%>
<%@page import="java.util.*, java.net.*,java.util.Map, org.apache.commons.httpclient.util.*, java.text.*"%>
<%@page import="org.apache.solr.client.solrj.response.FacetField"%>
<html xmlns="http://www.w3.org/1999/xhtml">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>ViSearch - Rao vặt</title>
        <link href="style.css"rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="script/jquery-1.4.2.min.js">
        </script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#btBookmark").click(function(){
                    var docID = $("#hdIdValue").attr("value");
                    var keySearch = $("#txtSearch").attr("value");
                    alert(keySearch);
                    var Url = "BookmarkController?KeySearch=" + keySearch;
                    Url += "&DocID=" + docID;
                    $("#Bookmark").load(encodeURI(Url));
                });
            });
        </script>
        <script language="javascript">
            function CheckInput()
            {
                var keysearch = document.getElementById('txtSearch').value;
                if(keysearch == "")
                    return;
                else
                {
                    var url = "SearchRaoVatController?type=0&KeySearch=";
                    //url += keysearch.value;
                    url += encodeURIComponent(keysearch);
                    //alert(url);
                    window.location = url;
                }
            }
        </script>
    </head>

    <body>

        <%
                    session.setAttribute("CurrentPage", request.getRequestURI().replaceFirst("/ViSearch", ""));
                    // get String query
                    String strQuery = "";
                    if (request.getAttribute("KeySearch") != null) {
                        strQuery = (String) request.getAttribute("KeySearch");
                        //strQuery = URLDecoder.decode(strQuery, "UTF-8");
                        strQuery = strQuery.replaceAll("\"", "&quot;");
                    }
                    // end get String query
%>
        <%
                    //get SolrDocumentList
                    SolrDocumentList listdocs = new SolrDocumentList();

                    String result = "";
                    if (request.getAttribute("Docs") != null) {
                        listdocs = (SolrDocumentList) request.getAttribute("Docs");
                        for (int i = 0; i < listdocs.size(); i++) {
                            // Lay noi dung cua moi field
                            String id = (listdocs.get(i).getFieldValue("id")).toString();
                            String title = (listdocs.get(i).getFirstValue("rv_title")).toString();
                            String body = (listdocs.get(i).getFirstValue("body")).toString();
                            String price = "";
                            String category = (listdocs.get(i).getFieldValue("category")).toString();
                            String score = (listdocs.get(i).getFieldValue("score")).toString();
                            String site = (listdocs.get(i).getFieldValue("site")).toString();
                            String location = "";
                            String contact = "";
                            Date last_update = (Date) (listdocs.get(i).getFieldValue("last_update"));
                            Calendar cl = Calendar.getInstance();
                            cl.setTime(last_update);
                            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

                            String url = title;
                            String photo = "images/Noimage.jpg";

                            if (listdocs.get(i).getFieldValue("contact") != null) {
                                contact = (listdocs.get(i).getFieldValue("contact")).toString();
                            }

                            if (listdocs.get(i).getFieldValue("photo") != null && !listdocs.get(i).getFieldValue("photo").toString().equals("")) {
                                photo = (listdocs.get(i).getFieldValue("photo")).toString();
                            }
                            if (listdocs.get(i).getFieldValue("price") != null) {
                                price = (listdocs.get(i).getFieldValue("price")).toString();
                                if (Float.parseFloat(price) == 0) {
                                    price = "Call";
                                }
                            }
                            if (listdocs.get(i).getFieldValue("location") != null) {
                                location = (listdocs.get(i).getFieldValue("location")).toString();
                            }

                            url = "<div class=\"title_content\" id='divtop'>" + title + "</div>";
                            result += url;
                            result += "<div id='divleft'>";

                            /*  if (contact != null && contact.trim() != "") {
                            result += "<ul>" + "Thể loại: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=category:\"" + category + "\"'>" + category + "</a></ul>";
                            }
                            if (location != null && location.trim() != "") {
                            result += "<ul>" + "Location: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=location:" + location + "'>" + location + "</a></ul>";
                            }
                            result += "<ul>" + "Score: " + score + "</ul>";
                            result += "<ul>" + "Nguồn: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=site:" + site + "'>" + site + "</a></ul>";
                            result += "<ul>" + "Giá: " + price + "</ul>";
                            result += "<ul>" + "Ngày mới cập nhật : " + sf.format(last_update) + "</ul>";
                             */
                             result += "<table style=\"font-size:13px\">";
                            if (contact != null && contact.trim() != "") {
                                result += "<tr><td>" + "Thể loại: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=category:\"" + category + "\"'>" + category + "</a></td></tr>";
                            }
                            if (location != null && location.trim() != "") {
                                result += "<tr><td>" + "Location: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=location:" + location + "'>" + location + "</a></td></tr>";
                            }
                            //result += "<tr><td>" + "Score: " + score + "</td></tr>";
                            result += "<tr><td>" + "Nguồn: " + "<a href = 'SearchRaoVatController?type=2&KeySearch=site:" + site + "'>" + site + "</a></td></tr>";
                            result += "<tr><td>" + "Giá: " + price + "</td></tr>";
                            result += "<tr><td>" + "Ngày mới cập nhật : " + sf.format(last_update) + "</td></tr>";
                            result += "</table>";

                            result += "</div>";
                            photo = "<div id='divright'><img src='" + photo + "' alt='No image' width='200'/><br/>";
                            if (session.getAttribute("Member") != null) {
                                photo += "<span id='Bookmark'>"
                                        + "<input id='hdIdValue' type='hidden' value='" + id + "'>"
                                        + "<input id='btBookmark' type='button' value='Thêm vào bookmark'></span>";
                            }
                            photo += "</div>";
                            result += photo;
                            result += "<div id='divbottom'>" + body + "</div>";
                        }
                    }

                    //get SolrDocumentList
%>
        <%
                    //get Cùng chuyên mục Category
                    SolrDocumentList listdocs2 = new SolrDocumentList();
                    String result2 = "";

                    if (request.getAttribute("Docs_MoreLikeThis") != null) {
                        listdocs2 = (SolrDocumentList) request.getAttribute("Docs_MoreLikeThis");

                        result2 += "<div style=\"font-size:13px\">";
                        for (int i = 0; i < listdocs2.size(); i++) {


                            // Lay noi dung cua moi field
                            String title = "";
                            if ((listdocs2.get(i).getFieldValue("rv_title")).toString() != null) {
                                title = listdocs2.get(i).getFirstValue("rv_title").toString();
                            }

                            String id = (listdocs2.get(i).getFieldValue("id")).toString();
                            String url;

                            url = "<li><b><a href=\"DetailRaoVatController?id=" + id + "\">" + title + "</a></b></li>";
                            result2 += url;

                        }
                        result2 += "</div>";
                    }
                    //end Cùng chuyên mục Category
%>
        <%
                    // Get Facet

                    // End get Facet
%>
        <div id="wrap_left" align="center">
            <div id="wrap_right">
                <table id="wrap" width="974" border="0" cellpadding="0" cellspacing="0">

                    <tr><td height="20" colspan="2" align="center" valign="middle"></td></tr>
                    <tr>
                        <td height="130" colspan="2" valign="top">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td width="974" valign="top">
                                        <%@include file="template/banner_RaoVat.jsp" %>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <tr>
                        <td width="200" height="33" valign="top">
                            <%@include file="template/login.jsp" %>
                            <%  //out.print(facet);%>
                            <table>
                                <tr><th><div class="title_content" align="left">Từ khóa được tìm kiếm nhiều nhất</div></th></tr>
                                <tr><td><a href="">aaa</a></td></tr>
                                <tr><td><a href="">bbb</a></td></tr>
                                <tr><td><a href="">ccc</a></td></tr>
                            </table>
                        </td>
                        <td width="627" rowspan="2" valign="top">

                            <!--
                            <table>

                                <tr><td id="result_search">thong ke</td></tr><tr></tr>
                            </table>
                            -->

                            <div  valign="top" id="content">
                                <%
                                            out.print(result);
                                            if (result2 != "") {
                                                out.print("<div class=\"title_content\">Bài viết liên quan</div>");
                                                out.print(result2);
                                            }
                                %>
                            </div>
                        </td>
                    </tr>

                    <tr>

                    </tr>
                    <tr><td height="30"></td></tr>
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <tr>
                        <td width="100"></td>
                        <td colspan="2" valign="top">
                            <%@include file="template/footer.jsp" %>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

    </body>
</html>



