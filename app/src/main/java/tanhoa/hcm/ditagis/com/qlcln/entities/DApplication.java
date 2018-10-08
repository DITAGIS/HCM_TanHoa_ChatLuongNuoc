package tanhoa.hcm.ditagis.com.qlcln.entities;

import android.app.Application;
import android.location.Location;

import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.geometry.Geometry;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import tanhoa.hcm.ditagis.com.qlcln.entities.entitiesDB.User;
import tanhoa.hcm.ditagis.com.qlcln.utities.Constant;

public class DApplication extends Application {
    public Constant getConstant;

    {
        getConstant = new Constant();
    }


    private User userDangNhap;

    public User getUserDangNhap() {
        return userDangNhap;
    }

    public void setUserDangNhap(User userDangNhap) {
        this.userDangNhap = userDangNhap;
    }

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket(Constant.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private short loaiVatTu;

    public short getLoaiVatTu() {
        return loaiVatTu;
    }

    public void setLoaiVatTu(short loaiVatTu) {
        this.loaiVatTu = loaiVatTu;
    }


    private Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    private ArcGISFeature arcGISFeature;

    public ArcGISFeature getArcGISFeature() {
        return arcGISFeature;
    }

    public void setArcGISFeature(ArcGISFeature arcGISFeature) {
        this.arcGISFeature = arcGISFeature;
    }

    public Socket getSocket() {
        return mSocket;
    }

    private Location mLocation;

    public Location getmLocation() {
        return mLocation;
    }

    public void setmLocation(Location mLocation) {
        this.mLocation = mLocation;
    }
}
