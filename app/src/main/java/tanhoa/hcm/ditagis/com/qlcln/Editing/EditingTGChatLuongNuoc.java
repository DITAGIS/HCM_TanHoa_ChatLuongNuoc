package tanhoa.hcm.ditagis.com.qlcln.Editing;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureEditResult;
import com.esri.arcgisruntime.data.Field;
import com.esri.arcgisruntime.data.ServiceFeatureTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import tanhoa.hcm.ditagis.com.qlcln.QuanLyChatLuongNuoc;
import tanhoa.hcm.ditagis.com.qlcln.R;
import tanhoa.hcm.ditagis.com.qlcln.adapter.ChiTietCLNAdapter;
import tanhoa.hcm.ditagis.com.qlcln.adapter.ThoiGianCLNAdapter;
import tanhoa.hcm.ditagis.com.qlcln.async.NotifyChiTietCLNAdapterChangeAsync;
import tanhoa.hcm.ditagis.com.qlcln.async.RefreshTableThoiGianCLNAsync;
import tanhoa.hcm.ditagis.com.qlcln.entities.MauKiemNghiem;
import tanhoa.hcm.ditagis.com.qlcln.libs.FeatureLayerDTG;
import tanhoa.hcm.ditagis.com.qlcln.utities.Constant;

/**
 * Created by NGUYEN HONG on 5/7/2018.
 */

public class EditingTGChatLuongNuoc implements RefreshTableThoiGianCLNAsync.AsyncResponse {
    private QuanLyChatLuongNuoc mainActivity;
    private ServiceFeatureTable table_thoigiancln;
    private FeatureLayerDTG featureLayerDTG_thoigiancln;
    private ThoiGianCLNAdapter thoiTgTrongTrotAdapter;
    private List<Feature> table_feature;
    private String idDiemDanhGia;

    public EditingTGChatLuongNuoc(QuanLyChatLuongNuoc mainActivity, FeatureLayerDTG featureLayerDTG_thoigiancln) {
        this.mainActivity = mainActivity;
        this.featureLayerDTG_thoigiancln = featureLayerDTG_thoigiancln;
        table_thoigiancln = (ServiceFeatureTable) featureLayerDTG_thoigiancln.getFeatureLayer().getFeatureTable();
    }

    public void showThoiGianChatLuongNuoc(final ArcGISFeature mSelectedArcGISFeature) {
        final Map<String, Object> attributes = mSelectedArcGISFeature.getAttributes();
        final String idDiemDanhGia = attributes.get("IDDiemDanhGia").toString();
        this.idDiemDanhGia = idDiemDanhGia;
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
                    addThoiGianChatLuongNuoc(attributes);
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                    final MauKiemNghiem itemAtPosition = thoiTgTrongTrotAdapter.getItems().get(position);
                    HashMap<String, String> attributes = itemAtPosition.getString_attributes();
                    View layout_chitiet_chatluongnuoc = mainActivity.getLayoutInflater().inflate(R.layout.layout_title_listview, null);
                    ListView listview_chitietchatluongnuoc = (ListView) layout_chitiet_chatluongnuoc.findViewById(R.id.listview);

                    final List<ChiTietCLNAdapter.Item> items = new ArrayList<>();
                    List<Field> fields = table_thoigiancln.getFields();
                    final String[] updateFields = featureLayerDTG_thoigiancln.getUpdateFields();
                    for (Field field : fields) {
                        ChiTietCLNAdapter.Item item = new ChiTietCLNAdapter.Item();
                        item.setAlias(field.getAlias());
                        item.setFieldName(field.getName());
                        item.setFieldType(field.getFieldType());
                        Object value = attributes.get(field.getName());
                        if (value != null) {
                            if (field.getDomain() != null) {
                                List<CodedValue> codedValues = ((CodedValueDomain) field.getDomain()).getCodedValues();
                                String valueDomain = getValueDomain(codedValues, value.toString()).toString();
                                if (valueDomain != null) item.setValue(valueDomain);
                            } else switch (field.getFieldType()) {
                                case DATE:
                                    item.setValue(((String) value).split(" ")[0]);
                                    break;
                                default:
                                    if (attributes.get(field.getName()) != null)
                                        item.setValue(attributes.get(field.getName()));
                            }
                        }
                        for (String updateField : updateFields) {
                            if (field.getName().equals(updateField)) {
                                item.setEdit(true);
                            }
                        }
                        items.add(item);
                    }
                    ChiTietCLNAdapter chiTietCLNAdapter = new ChiTietCLNAdapter(mainActivity, items);
                    if (items != null) listview_chitietchatluongnuoc.setAdapter(chiTietCLNAdapter);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
                    builder.setView(layout_chitiet_chatluongnuoc);
                    builder.setPositiveButton("Cập nhật", null);
                    builder.setNegativeButton("Xóa", null);
                    builder.setNeutralButton("Thoát", null);
                    listview_chitietchatluongnuoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            editValueAttribute(parent, view, position, id);
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    builder.setPositiveButton(android.R.string.ok, null);
                    dialog.show();
                    // Chỉnh sửa
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Feature selectedFeature = getSelectedFeature(items.get(0).getValue());
                            for (ChiTietCLNAdapter.Item item : items) {
                                Domain domain = table_thoigiancln.getField(item.getFieldName()).getDomain();
                                Object codeDomain = null;
                                if (domain != null) {
                                    List<CodedValue> codedValues = ((CodedValueDomain) domain).getCodedValues();
                                    codeDomain = getCodeDomain(codedValues, item.getValue());
                                }
                                switch (item.getFieldType()) {
                                    case DATE:
                                        if (item.getCalendar() != null)
                                            selectedFeature.getAttributes().put(item.getFieldName(), item.getCalendar());
                                        break;
                                    case DOUBLE:
                                        if (item.getValue() != null)
                                            selectedFeature.getAttributes().put(item.getFieldName(), Double.parseDouble(item.getValue()));
                                        break;
                                    case SHORT:
                                        if (codeDomain != null) {
                                            selectedFeature.getAttributes().put(item.getFieldName(), Short.parseShort(codeDomain.toString()));
                                        } else if (item.getValue() != null)
                                            selectedFeature.getAttributes().put(item.getFieldName(), Short.parseShort(item.getValue()));
                                        break;
                                    case TEXT:
                                        if (codeDomain != null) {
                                            selectedFeature.getAttributes().put(item.getFieldName(), codeDomain.toString());
                                        } else if (item.getValue() != null)
                                            selectedFeature.getAttributes().put(item.getFieldName(), item.getValue());
                                        break;
                                }
                            }
                            updateFeature(selectedFeature);

                        }
                    });
                    // Xóa
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Feature selectedFeature = getSelectedFeature(items.get(0).getValue());
                            deleteFeature(selectedFeature);
                            dialog.dismiss();
                        }
                    });
                    // Thoát
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            });
            List<MauKiemNghiem> thoiGianChatLuongNuocs = new ArrayList<>();
            thoiTgTrongTrotAdapter = new ThoiGianCLNAdapter(mainActivity, thoiGianChatLuongNuocs);
            listView.setAdapter(thoiTgTrongTrotAdapter);
            getRefreshTableThoiGianCLNAsync();
            builder.setView(layout_timetable_chatluongnuoc);
            AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        }
    }

    private void getRefreshTableThoiGianCLNAsync() {
        new RefreshTableThoiGianCLNAsync(mainActivity, table_thoigiancln, thoiTgTrongTrotAdapter, new RefreshTableThoiGianCLNAsync.AsyncResponse() {
            @Override
            public void processFinish(List<Feature> features, List<MauKiemNghiem> thoiGianChatLuongNuocs) {
                table_feature = features;
            }
        }).execute(idDiemDanhGia);
    }

    private Feature getSelectedFeature(String OBJECTID) {
        Feature rt_feature = null;
        for (Feature feature : table_feature) {
            if (feature.getAttributes().get("OBJECTID") != null && feature.getAttributes().get("OBJECTID").toString().equals(OBJECTID)) {
                rt_feature = feature;
            }
        }
        return rt_feature;
    }

    private String getValueAttributes(Feature feature, String fieldName) {
        if (feature.getAttributes().get(fieldName) != null)
            return feature.getAttributes().get(fieldName).toString();
        return null;
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

    private void addThoiGianChatLuongNuoc(Map<String, Object> attributes) {
        final Feature table_thoigianclnFeature = table_thoigiancln.createFeature();
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        View layout_add_chatluongnuoc = mainActivity.getLayoutInflater().inflate(R.layout.layout_title_listview_button, null);
        ListView listView = (ListView) layout_add_chatluongnuoc.findViewById(R.id.listview);
        final List<ChiTietCLNAdapter.Item> items = new ArrayList<>();
        final ChiTietCLNAdapter chiTietCLNAdapter = new ChiTietCLNAdapter(mainActivity, items);
        if (items != null) listView.setAdapter(chiTietCLNAdapter);
        builder.setView(layout_add_chatluongnuoc);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editValueAttribute(parent, view, position, id);
            }
        });
        List<Field> fields = table_thoigiancln.getFields();
        String[] updateFields = featureLayerDTG_thoigiancln.getUpdateFields();
        for (Field field : fields) {
            if (!field.getName().equals(Constant.OBJECTID)) {
                ChiTietCLNAdapter.Item item = new ChiTietCLNAdapter.Item();
                item.setAlias(field.getAlias());
                item.setFieldName(field.getName());
                item.setFieldType(field.getFieldType());
                for (String updateField : updateFields) {
                    if (field.getName().equals(updateField)) {
                        item.setEdit(true);
                    }
                }
                if (field.getName().equals(Constant.IDDIEM_DANH_GIA)) {
                    String idDiemDanhGia = attributes.get(Constant.IDDIEM_DANH_GIA).toString();
                    item.setValue(idDiemDanhGia);
                }
                if (field.getName().equals(Constant.DIENTICH)) {
                    Object dientich = attributes.get(Constant.DIENTICH);
                    if (dientich != null) {
                        item.setValue(dientich.toString());
                    }
                }
                if (field.getName().equals(Constant.NGAY_CAP_NHAT)) {
                    item.setValue(Constant.DATE_FORMAT.format(Calendar.getInstance().getTime()));
                    item.setCalendar(Calendar.getInstance());
                }
                items.add(item);
            }
        }

        table_thoigianclnFeature.getAttributes().put(Constant.IDDIEM_DANH_GIA, attributes.get(Constant.IDDIEM_DANH_GIA).toString());
        Button btnAdd = (Button) layout_add_chatluongnuoc.findViewById(R.id.btnAdd);
        btnAdd.setText("Thêm");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                for (ChiTietCLNAdapter.Item item : items) {
                    Domain domain = table_thoigiancln.getField(item.getFieldName()).getDomain();
                    Object codeDomain = null;
                    if (domain != null) {
                        List<CodedValue> codedValues = ((CodedValueDomain) domain).getCodedValues();
                        codeDomain = getCodeDomain(codedValues, item.getValue());
                        table_thoigianclnFeature.getAttributes().put(item.getFieldName(), item.getValue());

                    }
                    switch (item.getFieldType()) {
                        case DATE:
                            if (item.getCalendar() != null)
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), item.getCalendar());
                            break;
                        case DOUBLE:
                            if (item.getValue() != null)
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), Double.parseDouble(item.getValue()));
                            break;
                        case SHORT:
                            if (codeDomain != null) {
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), Short.parseShort(codeDomain.toString()));
                            } else if (item.getValue() != null)
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), Short.parseShort(item.getValue()));
                            break;
                        case TEXT:
                            if (codeDomain != null) {
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), codeDomain.toString());
                            } else if (item.getValue() != null)
                                table_thoigianclnFeature.getAttributes().put(item.getFieldName(), item.getValue());
                            break;
                    }
                }
                addFeature(table_thoigianclnFeature);
            }
        });
    }

    private Object getCodeDomain(List<CodedValue> codedValues, String value) {
        Object code = null;
        for (CodedValue codedValue : codedValues) {
            if (codedValue.getName().equals(value)) {
                code = codedValue.getCode();
                break;
            }

        }
        return code;
    }

    private void addFeature(final Feature table_thoigianclnFeature) {
        ListenableFuture<Void> mapViewResult = table_thoigiancln.addFeatureAsync(table_thoigianclnFeature);
        mapViewResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = table_thoigiancln.applyEditsAsync();
                listListenableEditAsync.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                            if (featureEditResults.size() > 0) {
                                Toast.makeText(mainActivity.getApplicationContext(), "Thêm thành công dữ liệu", Toast.LENGTH_SHORT).show();
                                getRefreshTableThoiGianCLNAsync();
                            } else {
                                Toast.makeText(mainActivity.getApplicationContext(), "Không thêm được dữ liệu", Toast.LENGTH_SHORT).show();
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
    }

    private void deleteFeature(Feature table_thoigianclnFeature) {
        final ListenableFuture<Void> mapViewResult = table_thoigiancln.deleteFeatureAsync(table_thoigianclnFeature);
        mapViewResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = table_thoigiancln.applyEditsAsync();
                listListenableEditAsync.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                            if (featureEditResults.size() > 0) {
                                Toast.makeText(mainActivity.getApplicationContext(), "Xóa thành công dữ liệu", Toast.LENGTH_SHORT).show();
                                getRefreshTableThoiGianCLNAsync();
                            } else {
                                Toast.makeText(mainActivity.getApplicationContext(), "Không xóa được dữ liệu", Toast.LENGTH_SHORT).show();
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
    }

    private void updateFeature(final Feature table_thoigianclnFeature) {
        final ListenableFuture<Void> mapViewResult = table_thoigiancln.updateFeatureAsync(table_thoigianclnFeature);
        mapViewResult.addDoneListener(new Runnable() {
            @Override
            public void run() {
                final ListenableFuture<List<FeatureEditResult>> listListenableEditAsync = table_thoigiancln.applyEditsAsync();
                listListenableEditAsync.addDoneListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            List<FeatureEditResult> featureEditResults = listListenableEditAsync.get();
                            if (featureEditResults.size() > 0) {
                                Toast.makeText(mainActivity.getApplicationContext(), "Cập nhật thành công dữ liệu", Toast.LENGTH_SHORT).show();
                                getRefreshTableThoiGianCLNAsync();
                            } else {
                                Toast.makeText(mainActivity.getApplicationContext(), "Không cập nhật được dữ liệu", Toast.LENGTH_SHORT).show();
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
    }

    private void editValueAttribute(final AdapterView<?> parent, View view, int position, final long id) {
        final ChiTietCLNAdapter.Item item = (ChiTietCLNAdapter.Item) parent.getItemAtPosition(position);
        if (item.isEdit()) {
            final Calendar[] calendar = new Calendar[1];
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

            final Domain domain = table_thoigiancln.getField(item.getFieldName()).getDomain();
            if (domain != null) {
                layoutSpin.setVisibility(View.VISIBLE);
                List<CodedValue> codedValues = ((CodedValueDomain) domain).getCodedValues();
                if (codedValues != null) {
                    List<String> codes = new ArrayList<>();
                    for (CodedValue codedValue : codedValues)
                        codes.add(codedValue.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(layout.getContext(), android.R.layout.simple_list_item_1, codes);
                    spin.setAdapter(adapter);
                    if (item.getValue() != null) spin.setSelection(codes.indexOf(item.getValue()));

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
                                    calendar[0] = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                                    String date = String.format("%02d_%02d_%d", datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
                                    textView.setText(date);
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
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                    editText.setText(item.getValue());
                    break;
                case DOUBLE:
                    layoutEditText.setVisibility(View.VISIBLE);
                    editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                    editText.setText(item.getValue());
                    break;
            }
            builder.setPositiveButton("Cập nhật", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (domain != null) {
                        item.setValue(spin.getSelectedItem().toString());
                    } else {
                        switch (item.getFieldType()) {
                            case DATE:
                                item.setValue(textView.getText().toString());
                                item.setCalendar(calendar[0]);
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
                                item.setValue(editText.getText().toString());
                                break;
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
                    ChiTietCLNAdapter adapter = (ChiTietCLNAdapter) parent.getAdapter();
                    new NotifyChiTietCLNAdapterChangeAsync(mainActivity).execute(adapter);
//                    dialog.dismiss();
                }
            });
            builder.setView(layout);
            AlertDialog dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.show();

        }
    }

    @Override
    public void processFinish(List<Feature> features, List<MauKiemNghiem> thoiGianChatLuongNuocs) {

    }
}
