<%-- 
    Document   : video
    Created on : May 28, 2010, 9:22:53 PM
    Author     : tuandom
--%>

<%@page import="org.apache.solr.client.solrj.util.ClientUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apache.solr.client.solrj.SolrQuery"%>
<%@page import="org.apache.solr.client.solrj.SolrServer"%>
<%@page import="org.apache.solr.client.solrj.impl.CommonsHttpSolrServer"%>
<%@page import="org.apache.solr.common.SolrDocument"%>
<%@page import="org.apache.solr.common.SolrDocumentList"%>
<%@page import="org.apache.solr.common.SolrInputDocument"%>
<%@page import="org.apache.solr.client.solrj.response.QueryResponse"%>
<%@page import="java.util.*, java.net.*,java.util.Map, org.apache.commons.httpclient.util.*"%>
<%@page import="org.apache.solr.client.solrj.response.FacetField, org.apache.commons.httpclient.util.URIUtil"%>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>ViSearch - Video</title>
        <link href="style.css"rel="stylesheet" type="text/css" />
        <script src="Scripts/AC_RunActiveContent.js" type="text/javascript"></script>
        <link href="style.css"rel="stylesheet" type="text/css" />
        <link type="text/css" href="css/ui-lightness/jquery-ui-1.8.2.custom.css" rel="stylesheet" />
        <script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="js/clock.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.2.custom.min.js"></script>
        <link type="text/css" href="css/visearchStyle.css" rel="stylesheet"/>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#tbTopSearch").load("TopSearch?SearchType=5");
            });
        </script>
        <script language="javascript">
            function setText()
            {
                var keysearch = document.getElementById('txtSearch').value;
                if(keysearch=="")
                    document.getElementById('txtSearch').focus();
            }
            function CheckInput()
            {
                var keysearch = document.getElementById('txtSearch').value;
                var sortedtype = document.getElementById('slSortedType').value;
                if(keysearch == "")
                    return;
                else
                {
                    var url = "SearchVideoController?type=0&sp=1&KeySearch=";
                    url += encodeURIComponent(keysearch);
                    url += "&SortedType=" + sortedtype;
                    //    alert(url);
                    window.location = url;
                }
            }
            function showVideo(id)
            {
                // showVideo and Close all other Videos
                count = document.getElementsByTagName('OBJECT').length;
                for(var i=0; i < count; i++){
                    MDid = 'MediaPlayer'+i;
                    if(i!=id) // Close others
                    {
                        hideVideo(i);
                    }
                    else // and open new
                    {
                        document.getElementById(MDid).className="display";
                    }
                }
                // Show button CloseVideo and Close bt View
                var  btDong = "BTCloseMediaId" + id;
                document.getElementById(btDong).className="display";
                var btxem = 'BTViewMediaId'+id;
                document.getElementById(btxem).className="hidden";
            }
            function hideVideo(id)
            {
                // Hide media
                MDid = 'MediaPlayer'+id;
                document.getElementById(MDid).className="hidden";

                // Button XEM hide, button DONG show
                var btxem = 'BTViewMediaId'+id;
                document.getElementById(btxem).className="display";
                var  btDong = "BTCloseMediaId" + id;
                document.getElementById(btDong).className="hidden";
            }
            function Sort(type){
                var sortedtype = document.getElementById('slSortedType').value;
                var keysearch = document.getElementById('hfKeySearch').value;
                if(keysearch == "")
                    return;
                else
                {
                    var url = "SearchVideoController?sp=1&KeySearch=";
                    url += encodeURIComponent(keysearch);
                    url += "&SortedType=" + sortedtype;

                    if(document.getElementById('hdqf')!=null)
                    {
                        url+="&qf="+document.getElementById('hdqf').value;
                        type = 2; //facet
                    }
                    if(document.getElementById('hdqv')!=null)
                        url+="&qv="+encodeURIComponent(document.getElementById('hdqv').value);
                    //if(document.getElementById('hdsorttype')!=null)
                    //    url+="&SortedType="+document.getElementById('hdsorttype').value;

                    url += "&type=" + type;
                    window.location = url;
                }
            }
            $.ajax({
                type: "POST",
                url: "TopSearch",
                cache: false,
                data: "SearchType=5",
                success: function(html){
                    $("#tbTopSearch").append(html);
                }
            });
        </script>
    </head>

    <body onLoad="setText();">
        <%
                    String currentPage = "/video.jsp";
                    if (request.getQueryString() != null) {
                        currentPage = "/SearchVideoController?";
                        currentPage += request.getQueryString().toString();
                    }
                    session.setAttribute("CurrentPage", currentPage);
                    // Get strQuery
                    String strQuery = "";
                    if (request.getAttribute("KeySearch") != null) {
                        strQuery = (String) request.getAttribute("KeySearch");
                        //strQuery = URLDecoder.decode(strQuery, "UTF-8");
                        strQuery = strQuery.replaceAll("\"", "&quot;");
                    }
                    int sortedType = 0;
                    if (request.getAttribute("SortedType") != null) {
                        sortedType = Integer.parseInt(request.getAttribute("SortedType").toString());
                    }
                    // End Get strQuery
%>
        <%
                    // Get SolrDocumentList
                    SolrDocumentList listdocs = new SolrDocumentList();
                    Map<String, Map<String, List<String>>> highLight = null;
                    int numrow = 0;
                    int numpage = 0;
                    String strpaging = "";
                    String search_stats = "";
                    StringBuffer result = new StringBuffer();
                    String addBM = "";
                    String QTime;
                    if (request.getAttribute("QTime") != null) {
                        QTime = request.getAttribute("QTime").toString();
                        if (request.getAttribute("Docs") != null) {
                            listdocs = (SolrDocumentList) request.getAttribute("Docs");

                            search_stats = String.format("Có %d kết quả (%s giây)", listdocs.getNumFound(), QTime);
                            if (request.getAttribute("Collation") != null) {
                                String sCollation = (String) request.getAttribute("Collation");
                                result.append("<p><font color=\"#CC3333\" size=\"+2\">Có phải bạn muốn tìm: <b><a href=\"SearchVideoController?type=0&KeySearch=" + sCollation + "\">" + sCollation + "</a></b></font></p>");
                            }

                            for (int i = 0; i < listdocs.size(); i++) {
                                result.append("<table style=\"font-size:13px\">");

                                // Lay noi dung cua moi field
                                String title = (listdocs.get(i).getFirstValue("title")).toString();
                                String url = (listdocs.get(i).getFieldValue("url")).toString();
                                String id = (listdocs.get(i).getFieldValue("id")).toString();
                                String category = (listdocs.get(i).getFieldValue("category")).toString();

                                String title_hl = title;

                                if (request.getAttribute("HighLight") != null) {
                                    highLight = (Map<String, Map<String, List<String>>>) request.getAttribute("HighLight");
                                    List<String> highlightTitle = highLight.get(id).get("title");
                                    if (highlightTitle != null && !highlightTitle.isEmpty()) {
                                        title_hl = highlightTitle.get(0);
                                    }
                                }

                                result.append("<tr><td><b>" + title_hl + "</b></td></tr>");
                                result.append("<tr><td>Thể Loại: " + "<a href = 'SearchVideoController?type=3&KeySearch=category:\"" + URIUtil.encodePath(category) + "\"'>" + category + "</a></td></tr>");

                                String mediaId = "MediaPlayer" + i;
                                String BTViewMediaId = "BTViewMediaId" + i;
                                String BTCloseMediaId = "BTCloseMediaId" + i;
                                String btBookmark = "btBookmark" + i;
                                String spanBookmark = "spanBookmark" + i;
                                String addBookmark = "addBookmark" + i;
                                String nameBookmark = "nameBookmark" + i;
                                String hdIdValue = "hdIdValue" + i;

                                // START Tracking

                                result.append("<tr><td>");
                                result.append("<input type=\"button\" ID=\"" + BTViewMediaId + "\" value=\"Xem video\" onclick=\"showVideo('" + i + "');\" />");
                                result.append("<input type=\"button\" ID=\"" + BTCloseMediaId + "\" class=\"hidden\" value=\"Đóng video\" onclick=\"hideVideo('" + i + "');\" /></td>");
                                result.append("</tr>");

                                result.append("<tr><td>");
                                result.append("<span id='Tracking'></span>");
                                result.append("<tr><td><div class=\"hidden\" ID=\"" + mediaId + "\">" + url + "</div></td></tr>");

        %>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#<%=BTViewMediaId%>").click(function(){
                    var docID = <%=id%>
                    var keySearch = $("#hfKeySearch").attr("value");
                    var Url = "TrackingController?KeySearch=" + keySearch;
                    Url += "&DocID=" + docID;
                    Url += "&searchType=5";
                    $("#Tracking").load(encodeURI(Url));
                });
            });
        </script>
        <%
// END Tracking


                                        // START Bookmark
        %>
        <script type="text/javascript">
            $(function() {
                $("#datepicker").datepicker({dateFormat: 'dd-mm-yy'});

                $("#dialog").dialog("destroy");
                var tips = $(".validateTips");
                var name = $("#<%=nameBookmark%>");

                function updateTips(t) {
                    tips
                    .text(t)
                    .addClass('ui-state-highlight');
                    setTimeout(function() {
                        tips.removeClass('ui-state-highlight', 1500);
                    }, 500);
                }

                function checkLength(o) {

                    if ( o.val().length == 0) {
                        o.addClass('ui-state-error');
                        updateTips("Bạn chưa nhập tên bookmark");
                        return false;
                    } else {
                        return true;
                    }
                }

                $('#<%=addBookmark%>').dialog({
                    autoOpen: false,
                    height: 300,
                    width: 350,
                    modal: true,
                    buttons: {
                        'Đóng': function() {
                            $(this).dialog('close');
                        },
                        'Thêm': function() {
                            var bValid = true;
                            tips.removeClass('ui-state-error');
                            bValid = checkLength(name);
                            if(bValid)
                            {
                                var docID = $("#<%=hdIdValue%>").attr("value");
                                var keySearch = $("#hfKeySearch").attr("value");
                                var nameBookmark = $("#<%=nameBookmark%>").attr("value");
                                var Url = "BookmarkController?NameBookmark=" + nameBookmark;
                                var priority = $("#<%=addBookmark%> input:radio:checked").val();
                                Url += "&DocID=" + docID;
                                Url += "&SearchType=5";
                                Url += "&Priority=" + priority;
                                alert("Đã thêm vào Bookmark");
                                $("#<%=spanBookmark%>").load(encodeURI(Url));
                                $(this).dialog('close');
                            }
                        }
                    },close: function() {
                        name.val('').removeClass('ui-state-error');
                    }
                });
                $('#<%=btBookmark%>')
                .button()
                .click(function() {
                    $('#<%=addBookmark%>').dialog('open');
                });
            });

            $(document).ready(function(){
                $("#tbTopSearch").load("TopSearch?SearchType=5");
            });
        </script>
        <%



                                if (session.getAttribute("Member") != null) {
                                    result.append("<tr><td><span id=\"" + spanBookmark + "\"><input id=\"" + hdIdValue + "\" type='hidden' value='" + id + "'/>");
                                    result.append("<input id=\"" + btBookmark + "\" type='button' value='Thêm vào bookmark'/></span></td></tr>");
                                }

                                addBM += "<div id=\"" + addBookmark + "\" title=\"Thêm bookmark\">";
                                addBM += "<p class=\"validateTips\"/>";
                                addBM += "<form name=\"frmBm" + i + "\">";
                                addBM += " <fieldset>";
                                addBM += "  <label for=\"name\">Tên bookmark</label>";
                                addBM += "  <input type=\"text\" name=\"name\"  ID=\"" + nameBookmark + "\" class=\"text ui-widget-content ui-corner-all\" />";
                                addBM += "<input type=\"radio\" name=\"priority\" id=\"private\" value=\"0\"/>";
                                addBM += "<label for=\"private\">Riêng tư</label>";
                                addBM += "<input type=\"radio\" name=\"priority\" id=\"public\" value=\"1\" checked/>";
                                addBM += "<label for=\"public\">Chia sẻ</label>";
                                addBM += " </fieldset>";
                                addBM += " </form>";
                                addBM += "</div>";
                                // END Bookmark

                                result.append("</td></tr>");
                                result.append("<tr><td colspan='2'>");
                                result.append("<a href=\"SearchVideoController?type=1&KeySearch=" + URIUtil.encodeAll(title) + "\">Trang tương tự...</a>");
                                result.append("</td>");

                                result.append("</tr><tr><td>&nbsp;</td></tr></table>");

                            }


                            // Phan trang
                            numrow = Integer.parseInt(request.getAttribute("NumRow").toString());
                            numpage = Integer.parseInt(request.getAttribute("NumPage").toString());
                            strpaging = (String) request.getAttribute("Pagging");
                        }
                        // result += "Số kết quả tìm được là: " + numrow + "<br/>";
                        result.append("Tổng số trang là: " + numpage + "<br/>");
                        if (numpage > 1) {
                            result.append(strpaging + "<br/><br/>");
                        }
                    }

                    // End get SolrDocumentList
%>

        <%
// Get Facet
                    String facet = "";


                    List<FacetField> listFacet = (List<FacetField>) request.getAttribute("ListFacet");
                    if (listFacet != null) {
                        facet += "<div class=\"mnu\">Bộ lọc</div>";
                        for (int i = 0; i < listFacet.size(); i++) {
                            facet += "<table id=\"table_left\" width=\"100%\" border=\"0\">";
                            facet += "<tr>";
                            facet += "<td>";
                            facet += "<td>";
                            String fieldName = listFacet.get(i).getName();
                            facet += "<b>Thể loại</b>"; // vi chi facet 1 field category
                            facet += "<br>";
                            List<FacetField.Count> listCount = listFacet.get(i).getValues();
                            if (listCount != null) {
                                for (int j = 0; j < listCount.size(); j++) {
                                    String fieldText = listCount.get(j).getName();
                                    facet += "<a href = 'SearchVideoController?type=2&KeySearch=" + strQuery + "&qf=" + fieldName + "&qv=" + URIUtil.encodePath(fieldText) + "&SortedType=" + sortedType + "'>" + fieldText + "</a>";
                                    facet += " (" + listCount.get(j).getCount() + ")";
                                    facet += "<br>";
                                }
                            } else {
                                facet += "Không tìm ra dữ liệu<br>";
                            }
                            facet += "</td></tr>";
                            facet += "</table>";
                        }
                    }

                    // End Get Facet
%>


        <div id="wrap_left" align="center">
            <div id="wrap_right">
                <table id="wrap" width="974" border="0" cellpadding="0" cellspacing="0">

                    <tr><td height="8" colspan="2" align="center" valign="middle"></td></tr>
                    <tr>
                        <td height="130" colspan="2" valign="top">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                    <td style="text-align:right; margin-bottom:8px; font-size:11px">
                                        <%@include file="template/frm_login.jsp" %>
                                    </td></tr>
                                <tr>
                                    <td width="974" valign="top">
                                        <!-- banner here !-->
                                        <%@ include file="template/banner_Video.jsp"%>
                                        <!-- end banner      !-->
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td style="font-size:12px;" width="30%" align="middle">
                            <script type="" language="javascript">goforit();</script>
                            <span id="clock"/></td>
                        <td width="70%" align="middle"><%@include file="template/sortedtype1.jsp"%></td>
                    </tr>
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <tr>
                        <td width="200" height="33" valign="top">

                            <div class="subtable">

                                <% out.print(facet);%>
                                <div class="mnu">Tìm kiếm nhiều</div>
                                <table id="tbTopSearch">

                                </table>

                            </div>
                        </td>
                        <td width="627" rowspan="2" valign="top">
                            <% out.print(addBM);%>
                            <table>

                                <tr><td id="result_search"><% out.print(search_stats);%></td></tr><tr></tr>
                           <%  if (request.getParameter("qf") != null) {
                                                out.print("<tr><td id=\"top-header\">");
                                                if (request.getParameter("qf").toString().equals("category")) {
                                                    out.print(">> Thể loại: " + request.getParameter("qv"));
                                                } else {
                                                    out.print(">> " + request.getParameter("qf") + ": " + request.getParameter("qv"));
                                                }
                                                out.print("</td></tr>");
                                                out.print("<input type='hidden' id='hdqf' value='" + request.getParameter("qf") + "'>");
                                                out.print("<input type='hidden' id='hdqv' value='" + request.getParameter("qv") + "'>");
                                                out.print("<input type='hidden' id='hdsorttype' value='" + request.getAttribute("SortedType") + "'>");
                                            }
                                %>
                            </table>
                            <table id="table_right" width="100%" cellpadding="0" cellspacing="0">

                                <tr>

                                    <td valign="top" id="content">
                                        <% out.print(result);%>

                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <tr>

                    </tr>
                    <tr><td height="30"></td></tr>
                    <tr><td height="20" colspan="2" align="center" valign="bottom"><div align="center" class="nav"></div></td></tr>
                    <tr>
                        <td width="100"></td>
                        <td colspan="2" valign="top">
                            <%@include file="template/footer.jsp"%>
                        </td>
                    </tr>
                </table>
            </div>
        </div>

    </body>
</html>



