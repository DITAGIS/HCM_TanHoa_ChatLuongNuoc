package tanhoa.hcm.ditagis.com.qlcln.utities;

import java.text.SimpleDateFormat;

import tanhoa.hcm.ditagis.com.qlcln.adapter.SettingsAdapter;

/**
 * Created by ThanLe on 3/1/2018.
 */

public class Constant {
    public static final String CHAT_SERVER_URL = "http://sawagis.vn:3000";
    public static final String EVENT_LOCATION = "vitrinhanvien";
    public static final String EVENT_STAFF_NAME = "tennhanvien";
    public static final String EVENT_GIAO_VIEC = "giaoviecsuco";
    public static final String APP_ID = "qlcln";
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DDMMYYYY = new SimpleDateFormat("ddMMyyyy");
    public static final String OBJECTID = "OBJECTID";
    public static final String IDDIEM_DANH_GIA = "IDDiemDanhGia";
    public static final String IDMAUKIEMNGHIEM = "IDMauKiemNghiem";
    public static final String DIACHI = "DiaChi";
    public static final String NGAY_CAP_NHAT = "NgayCapNhat";
    public static final int REQUEST_LOGIN = 0;
    //    private final String SERVER_API = "http://sawagis.vn/tanhoa1/api";
    private static final String SERVER_API = "http://113.161.88.180:798/apiv1/api";

    public static class API_URL {
        public static final String LOGIN = SERVER_API + "/Login";
        public static final String DISPLAY_NAME = SERVER_API + "/Account/Profile";
        public static final String LAYER_INFO = SERVER_API + "/Account/LayerInfo";
        public static final String IS_ACCESS = SERVER_API + "/Account/IsAccess/m_qlcln";
    }

    private static Constant mInstance = null;

    public static Constant getInstance() {
        if (mInstance == null)
            mInstance = new Constant();
        return mInstance;
    }

    private Constant() {
    }


    public static class FIELD_DIEM_DANH_GIA {
        public static final String CANH_BAO_VUOT_NGUONG = "CanhBaoVuotNguong";
    }

    public static class VALUE_CANH_BAO_VUOT_NGUONG {
        public static final short VUOT = 1;
        public static final short KHONG_VUOT = 2;
    }
}
