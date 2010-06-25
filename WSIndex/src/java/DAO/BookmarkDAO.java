/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DAO;

import DTO.BookmarkDTO;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
/**
 *
 * @author tuandom
 */
public class BookmarkDAO {

    public ArrayList<BookmarkDTO> getDataList(int start, int end) throws SQLException, ParseException, java.text.ParseException {
        ArrayList<BookmarkDTO> list = new ArrayList<BookmarkDTO>();
        Connection cn = (Connection) DataProvider.getConnection("visearch");
        Statement st = (Statement) cn.createStatement();
        String query = String.format("SELECT * FROM bookmark where priority=1 LIMIT %d, %d", start, end);
        ResultSet rs = st.executeQuery(query);

        BookmarkDTO page;

        while (rs.next()) {
            page = new BookmarkDTO();
            page.setId(rs.getInt("Id"));
            page.setMemberId(rs.getInt("member_ID"));
            page.setDocId(rs.getString("docid"));
            page.setSearchType(rs.getInt("searchtype"));
            page.setBookmarkName(rs.getString("bookmark_name"));
            list.add(page);
        }

        rs.close();
        cn.close();
        return list;
    }

    public int CountRecord() throws SQLException {
        int iCount = 0;
        Connection cn = (Connection) DataProvider.getConnection("visearch");
        Statement st = (Statement) cn.createStatement();
        String query = "SELECT count(*) as NumRow FROM bookmark where priority=1"; // la public
        ResultSet rs = st.executeQuery(query);

        if (rs.next()) {
            iCount = rs.getInt("NumRow");
        }

        rs.close();
        cn.close();
        return iCount;
    }

}
