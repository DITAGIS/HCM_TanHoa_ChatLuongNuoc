package tanhoa.hcm.ditagis.com.qlcln.conectDB;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tanhoa.hcm.ditagis.com.qlcln.entities.ThoiGianChatLuongNuoc;


public class ThoiGianChatLuongNuocDB {
    private Connection cnn;
    private Statement statement;
    private String tableDB = "[TANHOAGIS].[dbo].[HOSO_THOIGIANCHATLUONGNUOC]";

    public ThoiGianChatLuongNuocDB() {
        cnn = ConnectionDB.getInstance().getConnection();
    }


    public List<ThoiGianChatLuongNuoc> getTable_ThoiGianChatLuongNuoc(String idDiemDanhGia) {
        List<ThoiGianChatLuongNuoc> DBs = new ArrayList<>();
        try {
            if (cnn == null) return null;
            String sql = "select top 100 * from " + tableDB + " where IDDiemDanhGia = '" + idDiemDanhGia + "'";
            statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            final ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                ThoiGianChatLuongNuoc thoiGianChatLuongNuoc = new ThoiGianChatLuongNuoc();
                HashMap<String,String> attributes = new HashMap<>();

                attributes.put("OBJECTID",rs.getString("OBJECTID"));
                attributes.put("IDDiemDanhGia",rs.getString("IDDiemDanhGia"));
                attributes.put("DienTich",rs.getString("DienTich"));
                attributes.put("TinhTrangNuoc",rs.getString("TinhTrangNuoc"));
                attributes.put("MuiNuoc",rs.getString("MuiNuoc"));
                attributes.put("MauNuoc",rs.getString("MauNuoc"));
                attributes.put("NgayCapNhat",rs.getString("NgayCapNhat"));
                thoiGianChatLuongNuoc.setString_attributes(attributes);

                DBs.add(thoiGianChatLuongNuoc);
            }
            rs.close();
            statement.close();

        } catch (SQLException e) {

        }
        return DBs;
    }


    public boolean insertThoiGianChatLuongNuoc(ThoiGianChatLuongNuoc input) {
        if (cnn == null) return false;
        else {
            try {
                statement = cnn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                String sql_getMaxObjectID = "SELECT Max([OBJECTID]) FROM " + tableDB;
                ResultSet resultSet = statement.executeQuery(sql_getMaxObjectID);
                int maxobjectid = 1;
                while (resultSet.next()) {
                    if (resultSet.getString(1) != null)
                        maxobjectid = Integer.parseInt(resultSet.getString(1)) + 1;
                }

                String sql = "INSERT INTO " + tableDB + " ([OBJECTID],[IDDiemDanhGia],[DienTich],[TinhTrangNuoc],[MuiNuoc],[MauNuoc],[NgayCapNhat])";
                String value = " VALUES('" + maxobjectid + "', ";

                Double dientich = Double.valueOf(0);
                int tinhtrangnuoc, muinuoc, maunuoc;

                String idDiemDanhGia = null, ngaycapnhat = null;
                if (input.getIdDiemDanhGia() != null) {
                    value = value + "'" + input.getIdDiemDanhGia() + "',";
                } else value = value + null + ", ";

                if (input.getDienTich() != null) {
                    dientich = Double.parseDouble(input.getDienTich());
                    value = value + dientich + ",";
                } else value = value + null + ", ";
                if (input.getTinhTrangNuoc() != null) {
                    tinhtrangnuoc = Integer.parseInt(input.getTinhTrangNuoc());
                    value = value + tinhtrangnuoc + ",";
                } else value = value + null + ", ";

                if (input.getMuiNuoc() != null) {
                    muinuoc = Integer.parseInt(input.getMuiNuoc());
                    value = value + muinuoc + ",";
                } else value = value + null + ", ";
                if (input.getMauNuoc() != null) {
                    maunuoc = Integer.parseInt(input.getMauNuoc());
                    value = value + maunuoc + ",";
                } else value = value + null + ", ";
                if (input.getNgayCapNhat() != null) {
                    ngaycapnhat = "" + input.getNgayCapNhat() + "";
                    value = value + ngaycapnhat;
                } else value = value + null;
//                String value = " VALUES('" + maxobjectid + "', " + idDiemDanhGia + ", " + dientich + ", " + null + ", " + dientich + "," + tgbdt + ", " + tgtt + "," + giaidoansinhtruong + ")";
                String sql_insert = sql + value + ")";
                int rs = statement.executeUpdate(sql_insert);
                statement.close();
                if (rs == 1) return true;
            } catch (SQLException e) {
            }
            return false;
        }
    }
}
