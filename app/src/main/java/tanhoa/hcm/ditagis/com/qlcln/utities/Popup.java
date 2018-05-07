package tanhoa.hcm.ditagis.com.qlcln.utities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.ArcGISFeature;
import com.esri.arcgisruntime.data.CodedValue;
import com.esri.arcgisruntime.data.CodedValueDomain;
import com.esri.arcgisruntime.data.Domain;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.FeatureTable;
import com.esri.arcgisruntime.data.FeatureType;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.LayerList;
import com.esri.arcgisruntime.mapping.view.Callout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tanhoa.hcm.ditagis.com.qlcln.Editing.EditingTGChatLuongNuoc;
import tanhoa.hcm.ditagis.com.qlcln.QuanLyChatLuongNuoc;
import tanhoa.hcm.ditagis.com.qlcln.R;
import tanhoa.hcm.ditagis.com.qlcln.adapter.ChiTietCLNAdapter;
import tanhoa.hcm.ditagis.com.qlcln.adapter.FeatureViewMoreInfoAdapter;
import tanhoa.hcm.ditagis.com.qlcln.adapter.ThoiGianCLNAdapter;
import tanhoa.hcm.ditagis.com.qlcln.async.EditAsync;
import tanhoa.hcm.ditagis.com.qlcln.async.NotifyDataSetChangeAsync;
import tanhoa.hcm.ditagis.com.qlcln.conectDB.ThoiGianChatLuongNuocDB;
import tanhoa.hcm.ditagis.com.qlcln.entities.ThoiGianChatLuongNuoc;
import tanhoa.hcm.ditagis.com.qlcln.libs.FeatureLayerDTG;

public class Popup extends AppCompatActivity implements View.OnClickListener {
    private QuanLyChatLuongNuoc mainActivity;
    private ArcGISFeature mSelectedArcGISFeature = null;
    private ServiceFeatureTable mServiceFeatureTable;
    private Callout mCallout;
    private FeatureLayerDTG mFeatureLayerDTG;
    private List<String> lstFeatureType;
    private LinearLayout linearLayout;
    private ThoiGianChatLuongNuocDB thoiGianChatLuongNuocDB;
    private FeatureTable table_thoigiancln;
    private FeatureLayerDTG featureLayerDTG_thoigiancln;
    private EditingTGChatLuongNuoc editingTGChatLuongNuoc;
    private SimpleDateFormat format_yearfirst = new SimpleDateFormat("yyyy/MM/dd ");

    public Popup(QuanLyChatLuongNuoc mainActivity,List<FeatureLayerDTG> layerDTGS, Callout callout) {
        this.mainActivity = mainActivity;
        this.mServiceFeatureTable = getServiceFeatureTable(layerDTGS,Constant.NAME_DIEMDANHGIANUOC);
        this.mCallout = callout;
        thoiGianChatLuongNuocDB = new ThoiGianChatLuongNuocDB();
        this.table_thoigiancln = getServiceFeatureTable(layerDTGS,Constant.NAME_HOSOTHOIGIANCHATLUONGNUOC);
        this.featureLayerDTG_thoigiancln = getFeatureLayerDTG(layerDTGS,Constant.NAME_HOSOTHOIGIANCHATLUONGNUOC);
        this.editingTGChatLuongNuoc = new EditingTGChatLuongNuoc(mainActivity,featureLayerDTG_thoigiancln);

    }
    public ServiceFeatureTable getServiceFeatureTable(List<FeatureLayerDTG> layerDTGS, String id){
        for(FeatureLayerDTG layerDTG: layerDTGS) {
            if (layerDTG.getFeatureLayer().getId().equals(id)) {
                return (ServiceFeatureTable) layerDTG.getFeatureLayer().getFeatureTable();
            }
        }
        return null;
    }
    public FeatureLayerDTG getFeatureLayerDTG(List<FeatureLayerDTG> layerDTGS, String id){
        for(FeatureLayerDTG layerDTG: layerDTGS) {
            if (layerDTG.getFeatureLayer().getId().equals(id)) {
                return layerDTG;
            }
        }
        return null;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setFeatureLayerDTG(FeatureLayerDTG layerDTG) {
        this.mFeatureLayerDTG = layerDTG;
    }

    private void refressPopup() {
        Map<String, Object> attributes = mSelectedArcGISFeature.getAttributes();
        for (Field field : this.mSelectedArcGISFeature.getFeatureTable().getFields()) {
            Object value = attributes.get(field.getName());
            switch (field.getName()) {
                case Constant.IDDIEM_DANH_GIA:
                    if (value != null)
                        ((TextView) linearLayout.findViewById(R.id.txt_id_su_co)).setText(value.toString());
                    break;
                case Constant.VI_TRI:
                    if (value != null)
                        ((TextView) linearLayout.findViewById(R.id.txt_vi_tri_su_co)).setText(value.toString());
                    break;
            }
        }
    }

    public LinearLayout createPopup(final ArcGISFeature mSelectedArcGISFeature) {
        this.mSelectedArcGISFeature = mSelectedArcGISFeature;
        lstFeatureType = new ArrayList<>();
        for (int i = 0; i < mSelectedArcGISFeature.getFeatureTable().getFeatureTypes().size(); i++) {
            lstFeatureType.add(mSelectedArcGISFeature.getFeatureTable().getFeatureTypes().get(i).getName());
        }
        LayoutInflater inflater = LayoutInflater.from(this.mainActivity.getApplicationContext());
        linearLayout = (LinearLayout) inflater.inflate(R.layout.popup_diemdanhgianuoc, null);
        refressPopup();
        if (mCallout != null) mCallout.dismiss();
        ((ImageButton) linearLayout.findViewById(R.id.imgBtn_viewtablethoigian)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editingTGChatLuongNuoc.showThoiGianChatLuongNuoc(mSelectedArcGISFeature);
//                showThoiGianChatLuongNuoc();
            }
        });
        ((ImageButton) linearLayout.findViewById(R.id.imgBtn_ViewMoreInfo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMoreInfo();
            }
        });

        ((ImageButton) linearLayout.findViewById(R.id.imgBtn_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedArcGISFeature.getFeatureTable().getFeatureLayer().clearSelection();
                deleteFeature();
            }
        });
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return linearLayout;
    }

    /**
     * Hiển thị bảng thời gian về thu thập chất lượng nước
     */
    private void showThoiGianChatLuongNuoc() {
        final Map<String, Object> attributes = mSelectedArcGISFeature.getAttributes();
        final String idDiemDanhGia = attributes.get("IDDiemDanhGia").toString();
        if (idDiemDanhGia != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
            builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final View layout_timetable_chatluongnuoc = mainActivity.getLayoutInflater().inflate(R.layout.layout_title_listview_button, null);
            ListView listView = (ListView) layout_timetable_chatluongnuoc.findViewById(R.id.listview);
            ((TextView) layout_timetable_chatluongnuoc.findViewById(R.id.txtTitlePopup)).setText("Thời gian trồng trọt");
            Button btnAdd = (Button) layout_timetable_chatluongnuoc.findViewById(R.id.btnAdd);
            btnAdd.setText("Thêm dữ liệu");
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attributes.get("DienTich") != null)
                        addThoiGianChatLuongNuoc(idDiemDanhGia, attributes.get("DienTich").toString());
                    else addThoiGianChatLuongNuoc(idDiemDanhGia, null);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ThoiGianChatLuongNuoc itemAtPosition = (ThoiGianChatLuongNuoc) parent.getItemAtPosition(position);
                    HashMap<String, String> attributes = itemAtPosition.getString_attributes();

                    View layout_chitiet_chatluongnuoc = mainActivity.getLayoutInflater().inflate(R.layout.layout_title_listview, null);
                    ListView listView = (ListView) layout_chitiet_chatluongnuoc.findViewById(R.id.listview);
                    List<ChiTietCLNAdapter.Item> items = new ArrayList<>();

                    List<Field> fields = table_thoigiancln.getFields();
                    for (Field field : fields) {
                        ChiTietCLNAdapter.Item item = new ChiTietCLNAdapter.Item();
                        item.setAlias(field.getAlias());
                        Object value = attributes.get(field.getName());
                        if (value != null) {
                            if (field.getDomain() != null) {
                                List<CodedValue> codedValues = ((CodedValueDomain) field.getDomain()).getCodedValues();
                                String valueDomain = getValueDomain(codedValues, value.toString()).toString();
                                if (valueDomain != null) item.setValue(valueDomain);
                            } else switch (field.getFieldType()) {
                                case DATE:
//                                    item.setValue(Constant.DATE_FORMAT.format(((Calendar) value).getTime()));
                                    break;
                                default:
                                    item.setValue(attributes.get(field.getName()));
                            }
                        }
                        items.add(item);
                    }
                    ChiTietCLNAdapter chiTietCLNAdapter = new ChiTietCLNAdapter(mainActivity, items);
                    if (items != null) listView.setAdapter(chiTietCLNAdapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                    builder.setView(layout_chitiet_chatluongnuoc);
                    AlertDialog dialog = builder.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                }
            });
            List<ThoiGianChatLuongNuoc> items = thoiGianChatLuongNuocDB.getTable_ThoiGianChatLuongNuoc(idDiemDanhGia);
            ThoiGianCLNAdapter thoiTgTrongTrotAdapter = new ThoiGianCLNAdapter(mainActivity, items);

            if (items != null) listView.setAdapter(thoiTgTrongTrotAdapter);
            builder.setView(layout_timetable_chatluongnuoc);
            AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }
    }

    /**
     * Thêm dữ liệu về thu thập chất kết quả lượng nước
     */
    private void addThoiGianChatLuongNuoc(final String idDiemDanhGia, final String dientich) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        View layout = mainActivity.getLayoutInflater().inflate(R.layout.layout_add_thoigianchatluongnuoc, null);
        final AlertDialog dialog = builder.create();
        dialog.setView(layout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        layout.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThoiGianChatLuongNuoc addthoiGianSanXuatTrongTrot = new ThoiGianChatLuongNuoc();
                addthoiGianSanXuatTrongTrot.setIdDiemDanhGia(idDiemDanhGia);
                addthoiGianSanXuatTrongTrot.setDienTich(dientich);
                addthoiGianSanXuatTrongTrot.setNgayCapNhat("GETDATE()");
                addthoiGianSanXuatTrongTrot.setMauNuoc("1");
                addthoiGianSanXuatTrongTrot.setMuiNuoc("1");
                addthoiGianSanXuatTrongTrot.setTinhTrangNuoc("1");
                boolean b = thoiGianChatLuongNuocDB.insertThoiGianChatLuongNuoc(addthoiGianSanXuatTrongTrot);
                if (b)
                    Toast.makeText(mainActivity.getApplicationContext(), "Thêm thành công dữ liệu", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void viewMoreInfo() {
        Map<String, Object> attr = mSelectedArcGISFeature.getAttributes();
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        View layout = mainActivity.getLayoutInflater().inflate(R.layout.layout_viewmoreinfo_feature, null);
        final FeatureViewMoreInfoAdapter adapter = new FeatureViewMoreInfoAdapter(mainActivity, new ArrayList<FeatureViewMoreInfoAdapter.Item>());
        final ListView lstView = layout.findViewById(R.id.lstView_alertdialog_info);
        lstView.setAdapter(adapter);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edit(parent, view, position, id);
            }
        });
        String[] updateFields = mFeatureLayerDTG.getUpdateFields();
        String typeIdField = mSelectedArcGISFeature.getFeatureTable().getTypeIdField();
        for (Field field : this.mSelectedArcGISFeature.getFeatureTable().getFields()) {
            Object value = attr.get(field.getName());
            if (field.getName().equals(Constant.IDDIEM_DANH_GIA)) {
                if (value != null)
                    ((TextView) layout.findViewById(R.id.txt_alertdialog_id_su_co)).setText(value.toString());
            } else {
                FeatureViewMoreInfoAdapter.Item item = new FeatureViewMoreInfoAdapter.Item();
                item.setAlias(field.getAlias());
                item.setFieldName(field.getName());
                if (value != null) {
                    if (item.getFieldName().equals(typeIdField)) {
                        List<FeatureType> featureTypes = mSelectedArcGISFeature.getFeatureTable().getFeatureTypes();
                        String valueFeatureType = getValueFeatureType(featureTypes, value.toString()).toString();
                        if (valueFeatureType != null) item.setValue(valueFeatureType);
                    } else if (field.getDomain() != null) {
                        List<CodedValue> codedValues = ((CodedValueDomain) this.mSelectedArcGISFeature.getFeatureTable().getField(item.getFieldName()).getDomain()).getCodedValues();
                        String valueDomain = getValueDomain(codedValues, value.toString()).toString();
                        if (valueDomain != null) item.setValue(valueDomain);
                    } else switch (field.getFieldType()) {
                        case DATE:
                            item.setValue(Constant.DATE_FORMAT.format(((Calendar) value).getTime()));
                            break;
                        case OID:
                        case TEXT:
                            item.setValue(value.toString());
                            break;
                        case SHORT:
                            item.setValue(value.toString());
                            break;
                    }
                }
                item.setEdit(false);
                for (String updateField : updateFields) {
                    if (item.getFieldName().equals(updateField)) {
                        item.setEdit(true);
                        break;
                    }
                }
                item.setFieldType(field.getFieldType());
                adapter.add(item);
                adapter.notifyDataSetChanged();
            }
        }
        builder.setView(layout);
        builder.setCancelable(false);
        builder.setPositiveButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setMessage("Bạn có muốn cập nhật tất cả thay đổi?");
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditAsync editAsync = new EditAsync(mainActivity, mServiceFeatureTable, mSelectedArcGISFeature);
                        try {
                            Void aVoid = editAsync.execute(adapter).get();
                            refressPopup();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alertDialog.show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.show();


    }

    private Object getValueDomain(List<CodedValue> codedValues, String code) {
        Object value = null;
        for (CodedValue codedValue : codedValues) {
            if (codedValue.getCode().toString().equals(code)) {
                value = codedValue.getName();
                break;
            }

        }
        return value;
    }

    private Object getValueFeatureType(List<FeatureType> featureTypes, String code) {
        Object value = null;
        for (FeatureType featureType : featureTypes) {
            if (featureType.getId().toString().equals(code)) {
                value = featureType.getName();
                break;
            }
        }
        return value;
    }

    private void edit(final AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) instanceof FeatureViewMoreInfoAdapter.Item) {
            final FeatureViewMoreInfoAdapter.Item item = (FeatureViewMoreInfoAdapter.Item) parent.getItemAtPosition(position);
            if (item.isEdit()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
                builder.setTitle("Cập nhật thuộc tính");
                builder.setMessage(item.getAlias());
                builder.setCancelable(false).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final LinearLayout layout = (LinearLayout) mainActivity.getLayoutInflater().
                        inflate(R.layout.layout_dialog_update_feature_listview, null);
                builder.setView(layout);
                final FrameLayout layoutTextView = layout.findViewById(R.id.layout_edit_viewmoreinfo_TextView);
                final TextView textView = layout.findViewById(R.id.txt_edit_viewmoreinfo);
                final Button button = layout.findViewById(R.id.btn_edit_viewmoreinfo);
                final LinearLayout layoutEditText = layout.findViewById(R.id.layout_edit_viewmoreinfo_Editext);
                final EditText editText = layout.findViewById(R.id.etxt_edit_viewmoreinfo);
                final LinearLayout layoutSpin = layout.findViewById(R.id.layout_edit_viewmoreinfo_Spinner);
                final Spinner spin = layout.findViewById(R.id.spin_edit_viewmoreinfo);

                final Domain domain = mSelectedArcGISFeature.getFeatureTable().getField(item.getFieldName()).getDomain();
                if (item.getFieldName().equals(mSelectedArcGISFeature.getFeatureTable().getTypeIdField())) {
                    layoutSpin.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(layout.getContext(), android.R.layout.simple_list_item_1, lstFeatureType);
                    spin.setAdapter(adapter);
                    if (item.getValue() != null)
                        spin.setSelection(lstFeatureType.indexOf(item.getValue()));
                } else if (domain != null) {
                    layoutSpin.setVisibility(View.VISIBLE);
                    List<CodedValue> codedValues = ((CodedValueDomain) domain).getCodedValues();
                    if (codedValues != null) {
                        List<String> codes = new ArrayList<>();
                        for (CodedValue codedValue : codedValues)
                            codes.add(codedValue.getName());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(layout.getContext(), android.R.layout.simple_list_item_1, codes);
                        spin.setAdapter(adapter);
                        if (item.getValue() != null)
                            spin.setSelection(codes.indexOf(item.getValue()));

                    }
                } else switch (item.getFieldType()) {
                    case DATE:
                        layoutTextView.setVisibility(View.VISIBLE);
                        textView.setText(item.getValue());
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final View dialogView = View.inflate(mainActivity, R.layout.date_time_picker, null);
                                final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mainActivity).create();
                                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                                        Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                        String s = String.format("%02d_%02d_%d", datePicker.getDayOfMonth(), datePicker.getMonth(), datePicker.getYear());

                                        textView.setText(s);
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.setView(dialogView);
                                alertDialog.show();
                            }
                        });
                        break;
                    case TEXT:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setText(item.getValue());
                        break;
                    case SHORT:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        editText.setText(item.getValue());


                        break;
                    case DOUBLE:
                        layoutEditText.setVisibility(View.VISIBLE);
                        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        editText.setText(item.getValue());
                        break;
                }
                builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (item.getFieldName().equals(mSelectedArcGISFeature.getFeatureTable().getTypeIdField()) || (domain != null)) {
                            item.setValue(spin.getSelectedItem().toString());
                        } else {
                            switch (item.getFieldType()) {
                                case DATE:
                                    item.setValue(textView.getText().toString());
                                    break;
                                case DOUBLE:
                                    try {
                                        double x = Double.parseDouble(editText.getText().toString());
                                        item.setValue(editText.getText().toString());
                                    } catch (Exception e) {
                                        Toast.makeText(mainActivity, "Số liệu nhập vào không đúng định dạng!!!", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case TEXT:
                                case SHORT:
                                    try {
                                        short x = Short.parseShort(editText.getText().toString());
                                        item.setValue(editText.getText().toString());
                                    } catch (Exception e) {
                                        Toast.makeText(mainActivity, "Số liệu nhập vào không đúng định dạng!!!", Toast.LENGTH_LONG).show();
                                    }
                                    break;
                            }
                        }
                        dialog.dismiss();
                        FeatureViewMoreInfoAdapter adapter = (FeatureViewMoreInfoAdapter) parent.getAdapter();
                        new NotifyDataSetChangeAsync(mainActivity).execute(adapter);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        }

    }

    private void deleteFeature() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn xóa sự cố này?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mSelectedArcGISFeature.loadAsync();

                // update the selected feature
                mSelectedArcGISFeature.addDoneLoadingListener(new Runnable() {
                    @Override
                    public void run() {
                        if (mSelectedArcGISFeature.getLoadStatus() == LoadStatus.FAILED_TO_LOAD) {
                            Log.d(getResources().getString(R.string.app_name), "Error while loading feature");
                        }
                        try {
                            // update feature in the feature table
                            ListenableFuture<Void> mapViewResult = mServiceFeatureTable.deleteFeatureAsync(mSelectedArcGISFeature);
                            mapViewResult.addDoneListener(new Runnable() {
                                @Override
                                public void run() {
                                    // apply change to the server
                                    final ListenableFuture<List<FeatureEditResult>> serverResult = mServiceFeatureTable.applyEditsAsync();
                                    serverResult.addDoneListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<FeatureEditResult> edits = null;
                                            try {
                                                edits = serverResult.get();
                                                if (edits.size() > 0) {
                                                    if (!edits.get(0).hasCompletedWithErrors()) {
                                                        Log.e("", "Feature successfully updated");
                                                    }
                                                }
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }
                            });

                        } catch (Exception e) {
                            Log.e(getResources().getString(R.string.app_name), "deteting feature in the feature table failed: " + e.getMessage());
                        }
                    }
                });
                if (mCallout != null) mCallout.dismiss();
            }
        }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
//            @Override
//
                break;
        }
    }
}