
package tanhoa.hcm.ditagis.com.qlcln.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import tanhoa.hcm.ditagis.com.qlcln.R;
import tanhoa.hcm.ditagis.com.qlcln.entities.ThoiGianChatLuongNuoc;


public class ThoiGianCLNAdapter extends ArrayAdapter<ThoiGianChatLuongNuoc> {
    private Context context;
    private List<ThoiGianChatLuongNuoc> items;

    public ThoiGianCLNAdapter(Context context, List<ThoiGianChatLuongNuoc> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }


    public List<ThoiGianChatLuongNuoc> getItems() {
        return items;
    }

    public void setItems(List<ThoiGianChatLuongNuoc> items) {
        this.items = items;
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_text_text_image, null);
        }
        ThoiGianChatLuongNuoc thoiGianChatLuongNuoc = items.get(position);
        TextView textViewItem1 = (TextView) convertView.findViewById(R.id.txtItem1);
        TextView textViewItem2 = (TextView) convertView.findViewById(R.id.txtItem2);
        textViewItem1.setText(thoiGianChatLuongNuoc.getIdDiemDanhGia());
        textViewItem2.setText(thoiGianChatLuongNuoc.getDienTich());
        return convertView;
    }



}
