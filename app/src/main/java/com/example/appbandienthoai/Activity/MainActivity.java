package com.example.appbandienthoai.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.appbandienthoai.Adapter.DienThoaiAdapter;
import com.example.appbandienthoai.Adapter.MenuAdapter;
import com.example.appbandienthoai.Adapter.PhotoAdapter;
import com.example.appbandienthoai.Class.API;
import com.example.appbandienthoai.Class.DienThoaiMoi;
import com.example.appbandienthoai.Class.Photo;
import com.example.appbandienthoai.Class.StatusLogin;
import com.example.appbandienthoai.Database;
import com.example.appbandienthoai.R;
import com.example.appbandienthoai.Utils;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {
    Database db;
    StatusLogin status;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer mTimer;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    RecyclerView listsaleProduct;
    TextView tvHello;
    ImageView imgToProfile;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ListView lvManHinhChinh;
    MenuAdapter adapter = new MenuAdapter(this);
    List<DienThoaiMoi> mangSpMoi = new ArrayList<DienThoaiMoi>();
    List<DienThoaiMoi> saleProducts = new ArrayList<DienThoaiMoi>();
    DienThoaiAdapter spAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        AllProduct();
        SaleProduct();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this, "DBDienThoai.sqlite", null, 1);
        anhxa();
        actionBar();
        actionMenu();
        AllProduct();
        SaleProduct();
        imgToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AboutCustomer.class);
                startActivity(i);
            }
        });

        viewPager = findViewById(R.id.viewpager);
        circleIndicator = findViewById(R.id.circle_indicator);
        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(this, mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoSlideImages();
    }

    private void AllProduct() {
        API apiService = new API();
        apiService.fetchApiData("/api/public/products?perPage=5", new API.ApiResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    if (mangSpMoi != null) {
                        mangSpMoi.removeAll(mangSpMoi);
                    }

                    try {
                        JSONArray data = new JSONArray(response);

                        Log.i("API", response.toString());

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObj = data.getJSONObject(i);

                            DienThoaiMoi dienThoaiMoi = new DienThoaiMoi(
                                    jsonObj.getString("MaSPM"),
                                    jsonObj.getString("TenSP"),
                                    jsonObj.getJSONObject("HSX").getString("TenHSX"),
                                    jsonObj.getInt("SoLuong"),
                                    "",
                                    jsonObj.getString("MoTa"),
                                    jsonObj.getLong("GiaGoc"),
                                    jsonObj.getString("AnhBia")
                            );

                            db.AddProduct(
                                    dienThoaiMoi.getMASP(),
                                    dienThoaiMoi.getTENSP(),
                                    dienThoaiMoi.getPHANLOAI(),
                                    dienThoaiMoi.getSOLUONG(),
                                    dienThoaiMoi.getNOINHAP(),
                                    dienThoaiMoi.getNOIDUNG(),
                                    dienThoaiMoi.getDONGIA(),
                                    dienThoaiMoi.getHINHANH()
                            );

                            mangSpMoi.add(dienThoaiMoi);
                        }


                        spAdapter = new DienThoaiAdapter(MainActivity.this, mangSpMoi);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewManHinhChinh.setAdapter(spAdapter);
                        recyclerViewManHinhChinh.setLayoutManager(layoutManager);

                    } catch (JSONException e) {
                        Log.e("API", e.toString());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Log.e("API", error));
            }
        });
    }

    private void SaleProduct() {
        API apiService = new API();
        apiService.fetchApiData("/api/public/products?perPage=5", new API.ApiResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    if (saleProducts != null) {
                        saleProducts.removeAll(saleProducts);
                    }

                    try {
                        JSONArray data = new JSONArray(response);

                        Log.i("API", response.toString());

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObj = data.getJSONObject(i);


                            DienThoaiMoi dienThoaiMoi = new DienThoaiMoi(
                                    jsonObj.getString("MaSPM"),
                                    jsonObj.getString("TenSP"),
                                    jsonObj.getJSONObject("HSX").getString("TenHSX"),
                                    jsonObj.getInt("SoLuong"),
                                    "",
                                    jsonObj.getString("MoTa"),
                                    jsonObj.getLong("GiaGoc"),
                                    jsonObj.getString("AnhBia")
                            );

                            db.AddProduct(
                                    dienThoaiMoi.getMASP(),
                                    dienThoaiMoi.getTENSP(),
                                    dienThoaiMoi.getPHANLOAI(),
                                    dienThoaiMoi.getSOLUONG(),
                                    dienThoaiMoi.getNOINHAP(),
                                    dienThoaiMoi.getNOIDUNG(),
                                    dienThoaiMoi.getDONGIA(),
                                    dienThoaiMoi.getHINHANH()
                            );

                            saleProducts.add(dienThoaiMoi);
                        }


                        spAdapter = new DienThoaiAdapter(MainActivity.this, saleProducts);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1, LinearLayoutManager.HORIZONTAL, false);
                        listsaleProduct.setAdapter(spAdapter);
                        listsaleProduct.setLayoutManager(layoutManager);

                    } catch (JSONException e) {
                        Log.e("API", e.toString());
                    }
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> Log.e("API", error));
            }
        });


    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.hinhchuyendong1));
        list.add(new Photo(R.drawable.hinhchuyendong2));
        list.add(new Photo(R.drawable.hinhchuyendong3));
        list.add(new Photo(R.drawable.hinhchuyendong4));

        return list;
    }

    private void autoSlideImages() {
        if (mListPhoto == null || mListPhoto.isEmpty() || viewPager == null) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    private void actionMenu() {

        lvManHinhChinh.setAdapter(adapter);
        //chức năng của từng item trong actionmenu
        lvManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent sanpham = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        startActivity(sanpham);
                        break;

                    case 2:
                        Intent gioithieu = new Intent(getApplicationContext(), GioiThieuActivity.class);
                        startActivity(gioithieu);
                        break;

                    case 3:
                        Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangxuat);
                        break;
                }
            }
        });
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void anhxa() {
        //ánh xạ
        toolbar = (Toolbar) findViewById(R.id.toolbarManhinhChinh);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        recyclerViewManHinhChinh = (RecyclerView) findViewById(R.id.listnewProduct);
        listsaleProduct = (RecyclerView) findViewById(R.id.listsaleProduct);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        lvManHinhChinh = (ListView) findViewById(R.id.listManHinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        status = (StatusLogin) getApplication();
        tvHello = findViewById(R.id.tvHello);
        imgToProfile = findViewById(R.id.imgToProfile);

        if (status.isLogin() == false) {
            //khi chưa đnăg nhập, ẩn thứ cần ẩn
            tvHello.setVisibility(View.INVISIBLE);
            imgToProfile.setVisibility(View.INVISIBLE);
        } else {
            //Khi ddanwg nhap roi, hien thu can hien
            tvHello.setText("Hello, " + status.getUser());
            imgToProfile.setVisibility(View.VISIBLE);
            tvHello.setVisibility(View.VISIBLE);
        }

        mangSpMoi = new ArrayList<>();
        if (Utils.manggiohang == null) {
            Utils.manggiohang = new ArrayList<>();
        }

    }
}