package tanhoa.hcm.ditagis.com.qlcln.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import tanhoa.hcm.ditagis.com.qlcln.R;
import tanhoa.hcm.ditagis.com.qlcln.adapter.ItemTextTextImageAdapter;

/**
 * Created by ThanLe on 4/16/2018.
 */

public class NotifyTraCuuAdapterValueChangeAsync extends AsyncTask<ItemTextTextImageAdapter, Void, Void> {
    private ProgressDialog dialog;
    private Context mContext;
    private Activity mActivity;

    public NotifyTraCuuAdapterValueChangeAsync(Activity activity) {
        mActivity = activity;
        dialog = new ProgressDialog(mActivity, android.R.style.Theme_Material_Dialog_Alert);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage(mActivity.getString(R.string.async_dang_cap_nhat_giao_dien));
        dialog.setCancelable(false);

        dialog.show();

    }

    @Override
    protected Void doInBackground(ItemTextTextImageAdapter... params) {
        final ItemTextTextImageAdapter adapter = params[0];
        try {
            Thread.sleep(500);
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                }
            });
        } catch (InterruptedException e) {

        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }


    @Override
    protected void onPostExecute(Void result) {
        if (dialog != null || dialog.isShowing()) dialog.dismiss();
        super.onPostExecute(result);

    }

}