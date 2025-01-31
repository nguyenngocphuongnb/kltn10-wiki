/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.dao;

import com.mysql.jdbc.Statement;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.me.Utils.MyHashEncryption;
import org.me.dto.MemberDTO;

/**
 *
 * @author VinhPham
 */
public class MemberDAO {

    public boolean AddNewMember(MemberDTO member, String database) {
        boolean result = true;
        Connection cn = DataProvider.getConnection(database);
        try {
            CallableStatement cs;
            cs = cn.prepareCall("{CALL Insert_Member(?, ?, ?, ?, ?)}");
            cs.setString(1, member.getUserName());
            cs.setString(2, member.getPass());
            cs.setString(3, member.getFullName());
            Calendar cl = Calendar.getInstance();
            cl = member.getBirthDay();
            String birthdate = cl.get(Calendar.YEAR) + "-" + cl.get(Calendar.MONTH) + "-" + cl.get(Calendar.DAY_OF_MONTH);
            cs.setString(4, birthdate);
            cs.setInt(5, member.getSex());
            int n = cs.executeUpdate();
            if (n == 0) {
                result = false;
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean ChangePass(int id, String oldpass, String newpass, String database) {
        boolean result = true;
        Connection cn = DataProvider.getConnection(database);
        try {
            CallableStatement cs;
            cs = cn.prepareCall("{CALL change_password(?, ?, ?)}");
            cs.setInt(1, id);
            cs.setString(2, oldpass);
            cs.setString(3, newpass);
            int n = cs.executeUpdate();
            if (n == 0) {
                result = false;
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean UpdateInfo(MemberDTO mem, String database) {
        boolean result = true;
        Connection cn = DataProvider.getConnection(database);
        try {
            CallableStatement cs;
            cs = cn.prepareCall("{CALL update_member_info(?, ?, ?, ?)}");
            cs.setInt(1, mem.getId());
            cs.setString(2, mem.getFullName());
            Calendar cl = Calendar.getInstance();
            cl = mem.getBirthDay();
            String birthdate = cl.get(Calendar.YEAR) + "-" + cl.get(Calendar.MONTH) + "-" + cl.get(Calendar.DAY_OF_MONTH);
            cs.setString(3, birthdate);
            cs.setInt(4, mem.getSex());
            int n = cs.executeUpdate();
            if (n == 0) {
                result = false;
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public boolean UpdateRole(int id, int val, String database) throws SQLException {
        boolean bresult = false;
        Connection cn = (Connection) DataProvider.getConnection(database);
        Statement st = (Statement) cn.createStatement();
        String query = String.format("Update member set role=%d where id=%d",val, id);

        int i = st.executeUpdate(query);
        if(i>0)
            bresult = true;
        cn.close();
        return bresult;
    }

    public MemberDTO Login(String username, String password, String database) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MemberDTO member = null;
        Connection cn = DataProvider.getConnection(database);
        try {
            CallableStatement cs;
            cs = cn.prepareCall("{CALL member_login(?, ?)}");
            cs.setString(1, username);
            cs.setString(2, MyHashEncryption.hashPassword(password));
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                member = new MemberDTO();
                Calendar cl = Calendar.getInstance();
                Date date = rs.getDate("birthday");
                cl.setTime(date);
                member.setBirthDay(cl);
                member.setFullName(rs.getString("fullname"));
                member.setId(rs.getInt("ID"));
                member.setPass(rs.getString("pass"));
                member.setSex(rs.getInt("sex"));
                member.setUserName(rs.getString("username"));
                member.setRole(rs.getInt("role"));
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return member;
    }

    public boolean isExist(String username, String database) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Connection cn = DataProvider.getConnection(database);
        try {
            CallableStatement cs;
            cs = cn.prepareCall("{CALL isExistUsername(?)}");
            cs.setString(1, username);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                return true;
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public ArrayList<MemberDTO> GetListMember(String database/*, int start, int pagesize*/) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ArrayList<MemberDTO> list = new ArrayList<MemberDTO>();
        Connection cn = DataProvider.getConnection(database);
        try {
            //String query = String.format("Select * from member limit %s, %s", start, pagesize);
            String query = String.format("Select * from member");
            Statement st = (Statement) cn.createStatement();
            ResultSet rs = st.executeQuery(query);
            MemberDTO member;
            while (rs.next()) {
                member = new MemberDTO();
                Calendar cl = Calendar.getInstance();
                Date date = rs.getDate("birthday");
                cl.setTime(date);
                member.setBirthDay(cl);
                member.setFullName(rs.getString("fullname"));
                member.setId(rs.getInt("ID"));
                member.setPass(rs.getString("pass"));
                member.setSex(rs.getInt("sex"));
                member.setUserName(rs.getString("username"));
                member.setRole(rs.getInt("role"));
                list.add(member);
            }
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public int Count(String database) throws SQLException {
        int iCount = 0;
        Connection cn = (Connection) DataProvider.getConnection(database);
        Statement st = (Statement) cn.createStatement();
        String query = "SELECT count(*) as NumRow FROM member";
        ResultSet rs = st.executeQuery(query);

        if (rs.next()) {
            iCount = rs.getInt("NumRow");
        }

        rs.close();
        cn.close();
        return iCount;
    }

    public boolean Delete(int id, String database) throws SQLException {
        boolean bresult = false;
        Connection cn = (Connection) DataProvider.getConnection(database);
        Statement st = (Statement) cn.createStatement();
        String query = "Delete from member where id=" + id;

        int i = st.executeUpdate(query);
        if(i>0)
            bresult = true;
        cn.close();
        return bresult;
    }
}
