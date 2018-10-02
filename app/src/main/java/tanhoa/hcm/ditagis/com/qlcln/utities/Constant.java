package tanhoa.hcm.ditagis.com.qlcln.utities;

import java.text.SimpleDateFormat;

import tanhoa.hcm.ditagis.com.qlcln.adapter.SettingsAdapter;

/**
 * Created by ThanLe on 3/1/2018.
 */

public class Constant {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:SS");
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DDMMYYYY = new SimpleDateFormat("ddMMyyyy");
    private SettingsAdapter.Item[] mSettingsItems;
    public static final String OBJECTID = "OBJECTID";
    public static final String IDDIEM_DANH_GIA = "IDDiemDanhGia";
    public static final String IDMAUKIEMNGHIEM = "IDMauKiemNghiem";
    public static final String DIACHI = "DiaChi";
    public static final String NGAY_CAP_NHAT = "NgayCapNhat";
    //    public static final String SERVER_API = "http://gis.capnuoccholon.com.vn/cholon/api";
    private final String SERVER_API = "http://sawagis.vn/tanhoa1/api";
    public String API_LOGIN;

    {
        API_LOGIN = SERVER_API + "/Login";
    }

    public String DISPLAY_NAME;

    {
        DISPLAY_NAME = SERVER_API + "/Account/Profile";
    }

    public String LAYER_INFO;

    {
        LAYER_INFO = SERVER_API + "/Account/LayerInfo";
    }


    public String IS_ACCESS;

    {
        IS_ACCESS = SERVER_API + "/Account/IsAccess/m_qlcln";
    }

    private static Constant mInstance = null;

    public static Constant getInstance() {
        if (mInstance == null)
            mInstance = new Constant();
        return mInstance;
    }

    private Constant() {
        mSettingsItems = new SettingsAdapter.Item[]{
                new SettingsAdapter.Item("Phương thức thêm điểm sự cố", ""),
                new SettingsAdapter.Item("Tùy chọn tìm kiếm", ""),
                new SettingsAdapter.Item("Bố cục giao diện", ""),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
                new SettingsAdapter.Item("Tiêu đề cài đặt", "Tiêu đề con cài đặt"),
        };
    }

    public SettingsAdapter.Item[] getSettingsItems() {
        return mSettingsItems;
    }

    public static class FIELD_DIEM_DANH_GIA {
        public static final String CANH_BAO_VUOT_NGUONG = "CanhBaoVuotNguong";
    }

    public static class VALUE_CANH_BAO_VUOT_NGUONG {
        public static final short VUOT = 1;
        public static final short KHONG_VUOT = 2;
    }
}
