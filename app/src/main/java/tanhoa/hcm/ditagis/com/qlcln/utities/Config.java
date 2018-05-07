package tanhoa.hcm.ditagis.com.qlcln.utities;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by NGUYEN HONG on 3/20/2018.
 */

public class Config {
    private String url;
    private String[] queryField;
    private String[] outField;
    private String[] updateField;
    private String alias;
    private String name;
    private int minScale;
    private Context mContext;
    private static Config instance = null;

    public static Config getInstance() {
        if (instance == null) instance = new Config();
        return instance;
    }

    private Config() {
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public String[] getUpdateField() {
        return updateField;
    }

    public void setUpdateField(String[] updateField) {
        this.updateField = updateField;
    }

    public Config(String url, String[] outField, String alias) {
        this.url = url;
        this.outField = outField;
        this.alias = alias;
    }

    public Config(String url, String[] queryField, String[] outField, String alias) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.alias = alias;
    }


    public Config(String url, String[] queryField, String[] outField, String alias, int minScale, String[] updateField) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.updateField = updateField;
        this.alias = alias;
        this.minScale = minScale;
    }

    public Config(String url, String[] queryField, String[] outField, String name, String alias, int minScale, String[] updateField) {
        this.url = url;
        this.queryField = queryField;
        this.outField = outField;
        this.updateField = updateField;
        this.alias = alias;
        this.minScale = minScale;
        this.name = name;
    }

    public int getMinScale() {
        return minScale;
    }

    public void setMinScale(int minScale) {
        this.minScale = minScale;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String[] getQueryField() {
        return queryField;
    }

    public void setQueryField(String[] queryField) {
        this.queryField = queryField;
    }

    public String[] getOutField() {
        return outField;
    }

    public void setOutField(String[] outField) {
        this.outField = outField;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class FeatureConfig {
        public static ArrayList<Config> getConfigs() {
            ArrayList<Config> configs = new ArrayList<>();
            configs.add(new Config(Url.url_diemdanhgianuoc, QueryFields.queryFields_diemdanhgianuoc, OutFields.outFields_diemdanhgianuoc, Constant.NAME_DIEMDANHGIANUOC, Alias.alias_diemdanhgianuoc, MinScale.minScale_diemdanhgianuoc, UpdateFields.updateFields_diemdanhgianuoc));
            configs.add(new Config(Url.url_thoigianchatluongnuoc, QueryFields.queryFields_diemdanhgianuoc, OutFields.outFields_diemdanhgianuoc, Constant.NAME_HOSOTHOIGIANCHATLUONGNUOC, Alias.alias_diemdanhgianuoc, MinScale.minScale_diemdanhgianuoc, UpdateFields.updateFields_thoigianchatluongnuoc));
            return configs;
        }
    }

    public static class UpdateFields {
        public static String[] updateFields_diemdanhgianuoc = {"ViTriDiemDanhGia", "DienTich", "NgayCapNhat"};
        public static String[] updateFields_thoigianchatluongnuoc = {"DienTich","TinhTrangNuoc", "MuiNuoc", "MauNuoc","NgayCapNhat"};


    }
    public static class Url {
        public static String url_diemdanhgianuoc = "http://113.161.88.180:800/arcgis/rest/services/TanHoa/ChatLuongNuoc/FeatureServer/0";
        public static String url_thoigianchatluongnuoc = "http://113.161.88.180:800/arcgis/rest/services/TanHoa/ChatLuongNuoc/FeatureServer/1";
    }

    public static class QueryFields {
        public static String[] queryFields_diemdanhgianuoc = {"OBJECTID", "IDDiemDanhGia","ViTriDiemDanhGia", "DienTich", "NgayCapNhat"};

    }

    public static class OutFields {
        public static String[] outFields_diemdanhgianuoc = {"OBJECTID", "IDDiemDanhGia","ViTriDiemDanhGia", "DienTich", "NgayCapNhat"};

    }

    public static class Alias {
        public static String alias_diemdanhgianuoc = "Điểm đánh giá nước";
    }

    public static class MinScale {
        private static int minScale_diemdanhgianuoc = 10000;
    }

}
