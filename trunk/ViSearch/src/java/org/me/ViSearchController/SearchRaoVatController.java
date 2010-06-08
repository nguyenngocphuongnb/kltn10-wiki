/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.ViSearchController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.DisMaxParams;
import org.apache.solr.common.params.HighlightParams;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.MoreLikeThisParams;
import org.me.SolrConnection.SolrJConnection;
import org.me.Utils.MyString;
import org.me.Utils.Paging;
import org.me.dto.FacetDateDTO;

/**
 *
 * @author VinhPham
 */
public class SearchRaoVatController extends HttpServlet {

    SolrServer server;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        SolrDocumentList docs = new SolrDocumentList();
        String keySearch = "";
        int pagesize = 10;
        int currentpage = 1;
        long numRow = 0;
        int type = -1;
        int QTime = 0;
        int sortedType = 0;
        String sPaging = "/ViSearch/SearchRaoVatController?";
        List<FacetField> listFacet = null;
        ArrayList<FacetDateDTO> listFacetDate = null;

        try {

            if (request.getParameter("currentpage") != null) {
                currentpage = Integer.parseInt(request.getParameter("currentpage"));
            }

            server = SolrJConnection.getSolrServer("raovat");
            int start = (currentpage - 1) * pagesize;

            if (request.getParameter("type") != null) {
                type = Integer.parseInt(request.getParameter("type"));
                sPaging += "type=" + type;
            }

            if (request.getParameter("SortedType") != null) {
                sortedType = Integer.parseInt(request.getParameter("SortedType"));
                sPaging += "&SortedType=" + sortedType;
            }

            if (request.getParameter("KeySearch") != null) {
                keySearch = request.getParameter("KeySearch");
                sPaging += "&KeySearch=" + keySearch;
                QueryResponse rsp;
                Map<String, Map<String, List<String>>> highLight;

                switch (type) {
                    case 0:
                        if (request.getParameter("sp") != null) {
                            String sCollation = OnCheckSpelling(keySearch);
                            if (!sCollation.equals("")) {
                                request.setAttribute("Collation", sCollation);
                            }
                        }

                        //NewestDocument22(keySearch, "25");
                        rsp = OnSearchSubmit(keySearch, start, pagesize, sortedType);
                        docs = rsp.getResults();
                        highLight = rsp.getHighlighting();
                        request.setAttribute("HighLight", highLight);
                        QTime = rsp.getQTime();
                        // Get Facet
                        listFacet = rsp.getFacetFields();
                        //listFacetDate = NewestUpdateDocument(keySearch, "25");
                        break;
                    case 1:
                        rsp = OnMLT(keySearch, pagesize, sortedType);
                        docs = rsp.getResults();
                        highLight = rsp.getHighlighting();
                        if (highLight != null) {
                            request.setAttribute("HighLight", highLight);
                        }
                        QTime = rsp.getQTime();

                        for (int i = 0; i < docs.size() - 1; i++) {
                            for (int j = i + 1; j < docs.size(); j++) {
                                String title1 = docs.get(i).getFirstValue("rv_title").toString();
                                String title2 = docs.get(j).getFirstValue("rv_title").toString();
                                if (title1.trim().equals(title2.trim())) {
                                    Date date1 = (Date) docs.get(i).getFieldValue("last_update");
                                    Date date2 = (Date) docs.get(j).getFieldValue("last_update");
                                    if (date1.compareTo(date2) >= 0) {
                                        docs.remove(j);
                                        j--;
                                    } else {
                                        docs.remove(i);
                                        i--;
                                        break;
                                    }
                                }
                            }
                        }

                        int idem = Math.min(20, docs.size());
                        while (docs.size() > idem) {
                            docs.remove(idem);
                        }

                        // Get Facet
                        listFacet = rsp.getFacetFields();
                        //listFacetDate = NewestUpdateDocument(keySearch, "25");
                        break;
                    case 2:
                        String facetName = "";
                        String facetValue = "";
                        if (request.getParameter("FacetName") != null) {
                            facetName = request.getParameter("FacetName");
                            facetValue = request.getParameter("FacetValue");
                            sPaging += "&FacetName=" + facetName;
                            sPaging += "&FacetValue=" + facetValue;
                        }
                        rsp = OnSearchSubmitStandard(keySearch, facetName, facetValue, start, pagesize);
                        docs = rsp.getResults();
                        highLight = rsp.getHighlighting();
                        request.setAttribute("HighLight", highLight);
                        QTime = rsp.getQTime();
                        // Get Facet
                        listFacet = rsp.getFacetFields();
                        //listFacetDate = NewestUpdateDocument(keySearch, "25");
                        break;
                    default:
                        break;
                }
            }

            request.setAttribute("QTime", String.valueOf(1.0 * QTime / 1000));
            request.setAttribute("KeySearch", keySearch);
            request.setAttribute("SortedType", sortedType);
            if (docs != null) {
                numRow = docs.getNumFound();
                int numpage = (int) (numRow / pagesize);

                if (numRow % pagesize > 0) {
                    numpage++;
                }
                request.setAttribute("Docs", docs);
                //request.setAttribute("ListFacetDate", listFacetDate);
                if (type != 1) {
                    sPaging = Paging.getPaging(numpage, pagesize, currentpage, sPaging);
                    request.setAttribute("NumPage", numpage);
                }
                else
                {
                    sPaging = "20 kết quả tốt nhất trong " + numRow + " kết quả tìm được";
                }
                request.setAttribute("Pagging", sPaging);
                request.setAttribute("NumRow", numRow);
                request.setAttribute("ListFacet", listFacet);
            }
            String url = "/raovat.jsp";
            ServletContext sc = getServletContext();
            RequestDispatcher rd = sc.getRequestDispatcher(url);
            rd.forward(request, response);
        } catch (Exception e) {
            out.print(e.getMessage());
            //String url = "/ViSearch/index.jsp";
            //response.sendRedirect(url);
        } finally {
            out.close();
        }
    }

    QueryResponse OnSearchSubmit(String keySearch, int start, int pagesize, int sortedType) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();

        if (MyString.CheckSigned(keySearch)) {
            solrQuery.setQueryType("dismax");
        } else {
            solrQuery.setQueryType("dismax2");
        }

        if (sortedType != 0) {
            solrQuery.setParam(DisMaxParams.BF, "recip(rord(last_update),1,1000,1000)");
        }

        solrQuery.setQuery(keySearch);

        // Facet
        solrQuery.setFacet(true);
        solrQuery.addFacetField("category");
        solrQuery.addFacetField("site");
        //solrQuery.addFacetField("location");
        solrQuery.setFacetLimit(10);
        solrQuery.setFacetMinCount(1);
        // End Facet


        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("rv_title");
        solrQuery.addHighlightField("rv_body");
        solrQuery.setHighlightSimplePre("<em style=\"background-color:#FF0\">");
        solrQuery.setHighlightSimplePost("</em>");
        solrQuery.setHighlightRequireFieldMatch(true);
        solrQuery.setStart(start);
        solrQuery.setRows(pagesize);
        QueryResponse rsp = server.query(solrQuery);
        return rsp;
    }

    void NewestDocument22(String keySearch, String numDays) throws SolrServerException {

        ModifiableSolrParams params = new ModifiableSolrParams();
        params.set("q", keySearch);
        params.set("facet", true);
        params.set("field", "last_update");
        params.set("field", "site");
        params.set("facet.date.start", "NOW/DAY-5DAYS");
        params.set("facet.date.end", "NOW/DAY%2B1DAY");
        params.set("facet.date.gap", "%2B1DAY");


        QueryResponse rsp = server.query(params);

        Map<String, Integer> sdl = rsp.getFacetQuery();
        List<FacetField> lfc1 = rsp.getFacetFields();
        List<FacetField> lfc = rsp.getFacetDates();
        FacetField fc = rsp.getFacetDate("last_update");
        SolrDocumentList dl = rsp.getResults();
        int a = 8;
    }

    // Lay nhung bai viet moi nhat --> Test OK
    ArrayList<FacetDateDTO> NewestUpdateDocument(String keySearch, String numDays) throws SolrServerException, org.apache.commons.httpclient.URIException, IOException {
        HttpClient client = new HttpClient();

        String url = "http://localhost:8983/solr/raovat/select/?q=" + keySearch + "&facet=true&facet.date=timestamp&facet.date.start=NOW/DAY-" + numDays + "DAYS&facet.date.end=NOW/DAY%2B1DAY&facet.field=last_update&facet.limit=10&wt=json";
        url = URIUtil.encodeQuery(url);
        GetMethod get = new GetMethod(url);

        get.setRequestHeader(new Header("User-Agent", "localhost bot admin@localhost.com"));

        int status = client.executeMethod(get);
        String charSet = get.getResponseCharSet();
        if (charSet == null) {
            charSet = "UTF-8";
        }
        String body = convertStreamToString(get.getResponseBodyAsStream(), charSet);


        try {
            JSONObject json = (JSONObject) JSONSerializer.toJSON(body);
            JSONObject ob = json.getJSONObject("facet_counts");
            JSONObject location = ob.getJSONObject("facet_fields");

            // Vi moi chi xai 1 field Location nen lay luon
            JSONArray last_update = location.getJSONArray("last_update");
            ArrayList<FacetDateDTO> myArr = new ArrayList<FacetDateDTO>();

            if (last_update.size() > 0) {
                FacetDateDTO fD = null;
                for (int i = 0; i < last_update.size(); i++) {
                    if (i % 2 == 0)// Phan tu chan la Ngay (Value)
                    {
                        fD = new FacetDateDTO();
                        fD.setDateTime(last_update.get(i).toString());
                    } else //  Phan tu le là số (Count)
                    {
                        fD.setCount(last_update.get(i).toString());
                        myArr.add(fD);
                    }
                }
            }
            get.releaseConnection();
            return myArr;
        } catch (Exception x) {
            return null;
        }

    }

    QueryResponse OnSearchSubmitStandard(String keySearch, String facetName, String facetValue, int start, int pagesize) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery();
        if (facetValue.equals("l")) {
            //   facetValue="["
        }
        if (!facetName.equals("") && facetName != null) {
            keySearch = "+(rv_title:(" + keySearch + ") rv_body:(" + keySearch + ") category_index:(" + keySearch + ")) + " + facetName + ":\"" + facetValue + "\"";
        }
        solrQuery.setQuery(keySearch);

        // Facet
        solrQuery.setFacet(true);
        solrQuery.addFacetField("category");
        solrQuery.addFacetField("site");
        //solrQuery.addFacetField("location");
        solrQuery.setFacetLimit(10);
        solrQuery.setFacetMinCount(1);
        // End Facet

        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("rv_title");
        //solrQuery.addHighlightField("body");
        solrQuery.setHighlightSimplePre("<em style=\"background-color:#FF0\">");
        solrQuery.setHighlightSimplePost("</em>");
        solrQuery.setHighlightRequireFieldMatch(true);
        solrQuery.setStart(start);
        solrQuery.setRows(pagesize);
        QueryResponse rsp = server.query(solrQuery);
        return rsp;
    }

    QueryResponse OnMLT(String q, int pagesize, int sortedType) throws SolrServerException, MalformedURLException, UnsupportedEncodingException {
        //q = URLDecoder.decode(q, "UTF-8");
        SolrQuery query = new SolrQuery();

        // Facet
        query.setFacet(true);
        query.addFacetField("category");
        query.addFacetField("site");
        //query.addFacetField("location");
        query.setFacetLimit(10);
        query.setFacetMinCount(1);
        // End Facet

        query.setQueryType("/" + MoreLikeThisParams.MLT);
        query.set(MoreLikeThisParams.MATCH_INCLUDE, false);
        query.set(MoreLikeThisParams.MIN_DOC_FREQ, 1);
        query.set(MoreLikeThisParams.MIN_TERM_FREQ, 1);
        query.set(MoreLikeThisParams.SIMILARITY_FIELDS, "rv_title");
        if (sortedType == 1) {
            query.set(MoreLikeThisParams.BOOST, true);
            query.set(MoreLikeThisParams.QF, "{!boost b= recip(rord(timestamp),1,1000,1000)}");
        }

        query.setQuery("rv_title:" + MyString.cleanQueryTerm(q));
        //query.setQuery(ClientUtils.escapeQueryChars(q));
        query.setStart(0);
        query.setRows(100);
        query.setHighlight(true);
        query.addHighlightField("rv_title");
        query.addHighlightField("rv_body");
        query.setHighlightSimplePre("<em style=\"background-color:#FF0\">");
        query.setHighlightSimplePost("</em>");
        query.set(HighlightParams.ALTERNATE_FIELD, "wk_title");
        query.set(HighlightParams.FRAGMENTER, "regex");
        query.setHighlightFragsize(70);
        query.set(HighlightParams.SLOP, "0.5");
        query.set(HighlightParams.REGEX, "[-,/\n\"']{20,200}");
        QueryResponse rsp = server.query(query);
        return rsp;
    }

    String OnCheckSpelling(String q) throws org.apache.commons.httpclient.URIException, IOException {
        String result = "";
        HttpClient client = new HttpClient();
        //&spellcheck.build=true
        String url = "http://localhost:8983/solr/raovat/spell?q=" + q + "&spellcheck=true&spellcheck.collate=true&spellcheck.dictionary=jarowinkler&wt=json";
        url = URIUtil.encodeQuery(url);
        GetMethod get = new GetMethod(url);

        get.setRequestHeader(new Header("User-Agent", "localhost bot admin@localhost.com"));

        int status = client.executeMethod(get);
        String charSet = get.getResponseCharSet();
        if (charSet == null) {
            charSet = "UTF-8";
        }
        String body = convertStreamToString(get.getResponseBodyAsStream(), charSet);

        try {
            JSONObject json = (JSONObject) JSONSerializer.toJSON(body);
            JSONObject ob = json.getJSONObject("spellcheck");
            JSONArray cluster = ob.getJSONArray("suggestions");
            if (cluster.size() > 0) {
                result = cluster.getString(cluster.size() - 1);
            }
            get.releaseConnection();
            return result;
        } catch (Exception x) {
            return null;
        }
    }

    public String convertStreamToString(InputStream is, String encode) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, encode));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
