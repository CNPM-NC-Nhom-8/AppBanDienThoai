package com.example.appbandienthoai.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbandienthoai.Adapter.GioHangAdapter;
import com.example.appbandienthoai.Class.StatusLogin;
import com.example.appbandienthoai.Class.TinhTongEvent;
import com.example.appbandienthoai.Database;
import com.example.appbandienthoai.GioHang;
import com.example.appbandienthoai.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter;
    Database databaseHelper;
    SQLiteDatabase sqLiteDatabase;
    List<GioHang> gioHangList;
    StatusLogin statusLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        intView();
        intControl();
        totalMoney();
    }
    private void totalMoney() {
        long tongtiensp = 0;
        for (int i = 0; i < gioHangList.size(); i++){
            if (gioHangList.get(i).getIdVoucher().equals("")){
                tongtiensp += gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia();
                gioHangList.get(i).setTotalMoney(gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia());
            } else {
                Cursor cursor = databaseHelper.GetData("Select GIAM from VOUCHER where MAVOUCHER = '"+gioHangList.get(i).getIdVoucher()+"'");
                cursor.moveToFirst();
                double mucgiam = cursor.getDouble(0);
                tongtiensp += gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia()*(1-mucgiam);
                gioHangList.get(i).setTotalMoney(gioHangList.get(i).getSoLuong()*gioHangList.get(i).getDonGia()*(1-mucgiam));
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp) + " VNĐ ");
    }

    private void intControl() {
        setSupportActionBar(toolbar);//lay doi tuong actonbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//bat cai icon len

        //Click vào nút icon quay lại
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);//LayoutManager: xác định ra vị trí của các item trong RecyclerView.
        recyclerView.setLayoutManager(layoutManager);
        //trường hợp giỏ hàng trống
        Cursor cursor = databaseHelper.GetData("SELECT CARTLIST.IDCARTLIST, CARTLIST.IDVOUCHER, CARTLIST.IDSANPHAM, SANPHAM.TENSP, SANPHAM.HINHANH, CARTLIST.IDCUS, SANPHAM.DONGIA, CARTLIST.SOLUONG\n" +
                "FROM SANPHAM, CARTLIST\n" +
                "WHERE SANPHAM.MASP = CARTLIST.IDSANPHAM\n" +
                "AND CARTLIST.IDCUS = '"+statusLogin.getUser()+"'");
        gioHangList = new ArrayList<>();
        adapter = new GioHangAdapter(getApplicationContext(), gioHangList);
        recyclerView.setAdapter(adapter);
        Cursor cursor1 = cursor;
        if (!cursor1.moveToFirst()){
            giohangtrong.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
        }
        else{
            cursor.moveToFirst();
            GioHang gioHang = new GioHang();//Taọ class
            gioHang.setIdCartList(cursor.getInt(0));//Thêm thông tin
            gioHang.setIdVoucher(cursor.getString(1));
            gioHang.setIdSanPham(cursor.getString(2));
            gioHang.setTenSP(cursor.getString(3));
            gioHang.setHinhSanPham(cursor.getString(4));
            gioHang.setIdCus(cursor.getString(5));
            gioHang.setDonGia(cursor.getLong(6));
            gioHang.setSoLuong(cursor.getInt(7));
            gioHangList.add(gioHang);
            while (cursor.moveToNext()){
                gioHang = new GioHang();
                gioHang.setIdCartList(cursor.getInt(0));
                gioHang.setIdVoucher(cursor.getString(1));
                gioHang.setIdSanPham(cursor.getString(2));
                gioHang.setTenSP(cursor.getString(3));
                gioHang.setHinhSanPham(cursor.getString(4));
                gioHang.setIdCus(cursor.getString(5));
                gioHang.setDonGia(cursor.getLong(6));
                gioHang.setSoLuong(cursor.getInt(7));
                gioHangList.add(gioHang);
            }
            adapter.notifyDataSetChanged();
        }
        //
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gioHangList.size() > 0)
                {

                    for (int i = 0; i < gioHangList.size(); i++){
                        if (!gioHangList.get(i).getIdVoucher().equals("")){
                            Cursor cursor2 =
                                    databaseHelper.GetData("SELECT* " +
                                            "FROM VOUCHER " +
                                            "WHERE MAVOUCHER = '"+ gioHangList.get(i).getIdVoucher() +"' " +
                                            "AND HSD > '"+ LocalDate.now().toString() + "' ");
                            if (!cursor2.moveToFirst())
                            {
                                Toast.makeText(getApplicationContext(), "Voucher của sản phẩm " + gioHangList.get(i).getTenSP() + " đã hết hạn sử dụng.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    if (!statusLogin.isLogin()) {
                        AlertDialog.Builder d = new AlertDialog.Builder(GioHangActivity.this);
                        d.setTitle("THÔNG BÁO THANH TOÁN");
                        d.setMessage("Bạn đồng ý thanh toán giỏ hàng với " + gioHangList.size() + " sản phẩm." +
                                "\nTổng giá trị: " + tongtien.getText());
                        EditText dialogName = new EditText(getApplicationContext());
                        EditText dialogAddress = new EditText(getApplicationContext());
                        EditText dialogPhonenumber = new EditText(getApplicationContext());
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.VERTICAL);
                        dialogName.setHint("Nhập tên người nhận.");
                        dialogAddress.setHint("Nhập địa chỉ người nhận.");
                        dialogPhonenumber.setHint("SDT. VD: 0123456789");
                        dialogPhonenumber.setInputType(InputType.TYPE_CLASS_PHONE);
                        layout.addView(dialogName);
                        layout.addView(dialogAddress);
                        layout.addView(dialogPhonenumber);
                        d.setView(layout);
                        d.setNegativeButton("Thanh toán", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialogName.getText().equals("") || dialogAddress.getText().equals("") || dialogPhonenumber.getText().equals("")) {
                                    Toast.makeText(getApplicationContext(), "Thông tin vân chuyển không được bỏ trống.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (dialogPhonenumber.getText().toString().length() != 10) {
                                    Toast.makeText(getApplicationContext(), "Số điện thoại phải đủ 10 chữ số.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (!isNumber(dialogPhonenumber.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "Số điện thoại không hợp lệ. Ví dụ: 0123456789 là hợp lệ.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                //Sau khi kiểm tra dữ liệu đầu vào, bắt đầu thêm dữ liệu vào talbe BILL và BILLDETAIL
                                //Và xóa dữ liệu từ Cartlist
                                //Và giảm số lượng trong SANPHAM

                                //Thêm dữ liệu vào table BILL
                                ContentValues values = new ContentValues();
                                LocalDate date = LocalDate.now();
                                values.put("DATEORDER", date.toString());
                                values.put("TAIKHOANCUS", "");
                                values.put("NAMECUS", dialogName.getText().toString());
                                values.put("ADDRESSDELIVERRY", dialogAddress.getText().toString());
                                values.put("SDT", dialogPhonenumber.getText().toString());
                                long i = sqLiteDatabase.insert("BILL", null, values);
                                long i2 = -1;
                                //Thêm dữ liệu vào table BILLDETAIL
                                if (i != -1){
                                    Cursor cursor2 = databaseHelper.GetData("SELECT* FROM BILL");
                                    cursor2.moveToLast();
                                    for (int j = 0; j < gioHangList.size(); j++){
                                        //Thêm vào table BILLDETAIL
                                        ContentValues values1 = new ContentValues();
                                        values1.put("MASP", gioHangList.get(j).getIdSanPham());
                                        values1.put("IDORDER", cursor2.getInt(0));
                                        values1.put("IDVoucher", gioHangList.get(j).getIdVoucher());
                                        values1.put("QUANTITY", gioHangList.get(j).getSoLuong());
                                        values1.put("UNITPRICE", gioHangList.get(j).getDonGia());
                                        values1.put("TOTALPRICE", gioHangList.get(j).getTotalMoney());
                                        i2 = sqLiteDatabase.insert("BILLDETAIL", null, values1);
                                        //Giảm số lượng trong bảng SANPHAM
                                        Cursor cursor3 = databaseHelper.GetData("Select SOLUONG from SANPHAM WHERE MASP = '" + gioHangList.get(j).getIdSanPham() + "'");
                                        cursor3.moveToFirst();
                                        ContentValues values2 = new ContentValues();
                                        values2.put("SOLUONG", cursor3.getInt(0) - gioHangList.get(j).getSoLuong());
                                        sqLiteDatabase.update("SANPHAM", values2, "MASP=?", new String[]{gioHangList.get(j).getIdSanPham()});
                                    }
                                }
                                //Xóa dữ liệu trong Cartlist
                                sqLiteDatabase.delete("CARTLIST", "IDCUS=?", new String[]{statusLogin.getUser()});
                                if (i2 != -1){
                                    gioHangList.removeAll(gioHangList);
                                    adapter.notifyDataSetChanged();
                                    intControl();
                                    Toast.makeText(getApplicationContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thanh toán thất bại, đã xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            private boolean isNumber(String x){
                                return x.chars().allMatch( Character::isDigit );
                            }
                        });
                        AlertDialog dialog = d.create();
                        dialog.show();
                    } else {
                        AlertDialog.Builder d = new AlertDialog.Builder(GioHangActivity.this);
                        d.setTitle("THÔNG BÁO THANH TOÁN");
                        d.setMessage("Bạn đồng ý thanh toán giỏ hàng với " + gioHangList.size() + " sản phẩm." +
                                "\nTổng giá trị: " + tongtien.getText());
                        Cursor cursor2 = databaseHelper.GetData("Select* from ACCOUNT WHERE TAIKHOAN = '"+statusLogin.getUser()+"'");
                        cursor2.moveToFirst();
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        layout.setOrientation(LinearLayout.VERTICAL);
                        TextView tvName = new EditText(getApplicationContext());
                        TextView tvDiaChi = new EditText(getApplicationContext());
                        TextView tvPhoneNumber = new EditText(getApplicationContext());
                        tvName.setText(cursor2.getString(3));
                        tvDiaChi.setText(cursor2.getString(6));
                        tvPhoneNumber.setText(cursor2.getString(4));
                        layout.addView(tvName);
                        layout.addView(tvDiaChi);
                        layout.addView(tvPhoneNumber);
                        d.setView(layout);
                        d.setNegativeButton("Thanh toán", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Thêm dữ liệu vào table BILL
                                ContentValues values = new ContentValues();
                                LocalDate date = LocalDate.now();
                                values.put("DATEORDER", date.toString());
                                values.put("TAIKHOANCUS", cursor2.getString(0));
                                values.put("NAMECUS", cursor2.getString(3));
                                values.put("ADDRESSDELIVERRY", cursor2.getString(6));
                                values.put("SDT", cursor2.getString(4));
                                long i = sqLiteDatabase.insert("BILL", null, values);
                                long i2 = 0;
                                //Thêm dữ liệu vào table BILLDETAIL
                                if (i != -1){
                                    Cursor cursor2 = databaseHelper.GetData("SELECT* FROM BILL");
                                    cursor2.moveToLast();
                                    for (int j = 0; j < gioHangList.size(); j++){
                                        //Thêm vào table BILLDETAIL
                                        ContentValues values1 = new ContentValues();
                                        values1.put("MASP", gioHangList.get(j).getIdSanPham());
                                        values1.put("IDORDER", cursor2.getInt(0));
                                        values1.put("IDVoucher", gioHangList.get(j).getIdVoucher());
                                        values1.put("QUANTITY", gioHangList.get(j).getSoLuong());
                                        values1.put("UNITPRICE", gioHangList.get(j).getDonGia());
                                        values1.put("TOTALPRICE", gioHangList.get(j).getTotalMoney());
                                        i2 = sqLiteDatabase.insert("BILLDETAIL", null, values1);
                                        //Giảm số lượng trong SANPHAM
                                        Cursor cursor3 = databaseHelper.GetData("Select SOLUONG from SANPHAM WHERE MASP = '" + gioHangList.get(j).getIdSanPham() + "'");
                                        cursor3.moveToFirst();
                                        ContentValues values2 = new ContentValues();
                                        values2.put("SOLUONG", cursor3.getInt(0) - gioHangList.get(j).getSoLuong());
                                        sqLiteDatabase.update("SANPHAM", values2, "MASP=?", new String[]{gioHangList.get(j).getIdSanPham()});
                                    }
                                }
                                //Xóa dữ liệu trong Cartlist
                                sqLiteDatabase.delete("CARTLIST", "IDCUS=?", new String[]{statusLogin.getUser()});
                                if (i2 != -1){
                                    gioHangList.removeAll(gioHangList);
                                    Toast.makeText(getApplicationContext(), "Thanh toán thành công.", Toast.LENGTH_SHORT).show();
                                    adapter.notifyDataSetChanged();
                                    intControl();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thanh toán thất bại, đã xảy ra lỗi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            private boolean isNumber(String x){
                                return x.chars().allMatch( Character::isDigit );
                            }
                        });
                        AlertDialog dialog = d.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Giỏ hàng trống, không thể thanh toán.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void intView(){
        giohangtrong = findViewById(R.id.tvGioHangTrong);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleViewgiohang);
        tongtien = findViewById(R.id.tongtien);
        btnmuahang = findViewById(R.id.btnMuahang);
        databaseHelper = new Database(this, "DBDienThoai.sqlite", null, 1);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        statusLogin = (StatusLogin) getApplication();
        gioHangList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);//đăng ký lắng nghe sk
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    //ThreadMode:sẽ chạy trên luồng nào và nó sẽ cập nhật gì thì chạy luồng này
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event){
        if (event != null){
            intControl();
            totalMoney();
        }
    }
}