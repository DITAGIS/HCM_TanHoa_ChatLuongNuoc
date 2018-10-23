package tanhoa.hcm.ditagis.com.qlcln.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tanhoa.hcm.ditagis.com.qlcln.R;
import tanhoa.hcm.ditagis.com.qlcln.entities.DApplication;
import tanhoa.hcm.ditagis.com.qlcln.entities.entitiesDB.User;
import tanhoa.hcm.ditagis.com.qlcln.entities.entitiesDB.UserDangNhap;
import tanhoa.hcm.ditagis.com.qlcln.utities.Constant;
import tanhoa.hcm.ditagis.com.qlcln.utities.Preference;

public class NewLoginAsycn extends AsyncTask<String, Void, User> {
    private Exception exception;
    private ProgressDialog mDialog;
    private Context mContext;
    private AsyncResponse mDelegate;
    private DApplication mApplication;

    public interface AsyncResponse {
        void processFinish(User output);
    }

    public NewLoginAsycn(Activity activity, AsyncResponse delegate) {
        this.mApplication = (DApplication) activity.getApplication();
        this.mContext = activity;
        this.mDelegate = delegate;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        this.mDialog = new ProgressDialog(this.mContext, android.R.style.Theme_Material_Dialog_Alert);
        this.mDialog.setMessage(mContext.getString(R.string.connect_message));
        this.mDialog.setCancelable(false);
        this.mDialog.show();
    }

    @Override
    protected User doInBackground(String... params) {
        String userName = params[0];
        String pin = params[1];
        UserDangNhap.getInstance().setUser(null);
//        String passEncoded = (new EncodeMD5()).encode(pin + "_DITAGIS");
        // Do some validation here
        String urlParameters = String.format("Username=%s&Password=%s", userName, pin);
        String urlWithParam = String.format("%s?%s", Constant.API_URL.LOGIN, urlParameters);
        try {
//            + "&apiKey=" + API_KEY
            URL url = new URL(urlWithParam);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                break;
            }
            Preference.getInstance().savePreferences(mContext.getString(R.string.preference_login_api), stringBuilder.toString().replace("\"", ""));
            bufferedReader.close();
            if (checkAccess()) {
                UserDangNhap.getInstance().setUser(new User());
                UserDangNhap.getInstance().getUser().setDisplayName(getDisplayName());
                conn.disconnect();
                return UserDangNhap.getInstance().getUser();
            } else {
                conn.disconnect();
                return null;
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
//        if (user != null) {
        mDialog.dismiss();
        this.mDelegate.processFinish(user);
//        }
    }

    private Boolean checkAccess() {
        boolean isAccess = false;
        try {
            URL url = new URL(Constant.API_URL.IS_ACCESS);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", Preference.getInstance().loadPreference(mContext.getString(R.string.preference_login_api)));
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = bufferedReader.readLine();
                if (line.equals("true"))
                    isAccess = true;

            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            return isAccess;
        }
    }

    private String getDisplayName() {
        String displayName = "";
        try {
            URL url = new URL(Constant.API_URL.DISPLAY_NAME);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", Preference.getInstance().loadPreference(mContext.getString(R.string.preference_login_api)));
                conn.connect();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    pajsonRouteeJSon(line);
                    break;
                }

            } catch (Exception e) {
                Log.e("error", e.toString());
            } finally {
                conn.disconnect();
            }
        } catch (Exception e) {
            Log.e("error", e.toString());
        } finally {
            return displayName;
        }
    }

    private void pajsonRouteeJSon(String data) throws JSONException {
        if (data != null) {
            String myData = "{ \"account\": [".concat(data).concat("]}");
            JSONObject jsonData = new JSONObject(myData);
            JSONArray jsonRoutes = jsonData.getJSONArray("account");
//        jsonData.getJSONArray("account");
            for (int i = 0; i < jsonRoutes.length(); i++) {
                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                String displayName = jsonRoute.getString(mContext.getString(R.string.sql_coloumn_login_displayname));
                String username = jsonRoute.getString(mContext.getString(R.string.sql_coloumn_login_username));
                UserDangNhap.getInstance().setUser(new User());
                UserDangNhap.getInstance().getUser().setDisplayName(displayName);
                UserDangNhap.getInstance().getUser().setUserName(username);
            }
        }
    }
}