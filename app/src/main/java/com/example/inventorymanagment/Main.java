package com.example.inventorymanagment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.GravityCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.inventorymanagment.data.ItemizeDbHelper;
import com.example.inventorymanagment.data.StaticFunctions;
import com.example.inventorymanagment.model.SaleProduct;
import com.example.inventorymanagment.product.AddProduct;
import com.example.inventorymanagment.product.ProductList;
import com.example.inventorymanagment.product.detail.PurchaseDetail;
import com.example.inventorymanagment.vendor.AddVendor;
import com.example.inventorymanagment.vendor.VendorList;
import com.google.android.material.navigation.NavigationView;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.Random;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SHOWCASE_ID = "sequence example";
    private final String rewardedPlacementId = "rewardedVideo";
    String consent = "1";
    ItemizeDbHelper DbHelper;
    CardView SalesDetail;
    CardView addProduct;
    CardView addVendor;
    CardView pieChart;
    CardView productList;
    CardView purchaseDetail;
    CardView saleProduct;
    CardView vendorList;
    DrawerLayout drawerLayout;
    NavigationView navView;
    private int[] grantResults;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);

        this.DbHelper = new ItemizeDbHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.trolly);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.trolly);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.trolly);
        this.addProduct = findViewById(R.id.add_product);
        this.addProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.startActivity(new Intent(Main.this, AddProduct.class));
            }
        });
        this.saleProduct = findViewById(R.id.sale_product);
        this.saleProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.startActivity(new Intent(Main.this, SaleProduct.class));
            }
        });
        this.productList = findViewById(R.id.product_list);
        this.productList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.startActivity(new Intent(Main.this, ProductList.class));
            }
        });
        this.addVendor = findViewById(R.id.add_vendor);
        this.addVendor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.startActivity(new Intent(Main.this, AddVendor.class));
            }
        });
        this.vendorList = findViewById(R.id.vendor_list);
        this.vendorList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.startActivity(new Intent(Main.this, VendorList.class));
            }
        });
        this.pieChart = findViewById(R.id.pie_chart);
        this.pieChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Cursor productsTableExists = Main.this.DbHelper.productsTableExists();
                if (productsTableExists == null) {
                    Toast.makeText(Main.this, "Add product first", Toast.LENGTH_SHORT).show();
                } else if (productsTableExists.moveToFirst()) {
                    Main.this.openChart();
                }
            }
        });
        this.purchaseDetail = findViewById(R.id.purchaseDetail);
        this.purchaseDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.DbHelper.getReadableDatabase();
                Cursor purchaseTable = Main.this.DbHelper.getPurchaseTable();
                if (purchaseTable == null) {
                    Toast.makeText(Main.this, "There is no Purchase transaction", Toast.LENGTH_SHORT).show();
                } else if (purchaseTable.moveToFirst()) {
                    Main.this.startActivity(new Intent(Main.this, PurchaseDetail.class));
                }
            }
        });
        this.SalesDetail = findViewById(R.id.sales_detail);
        this.SalesDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Main.this.DbHelper.getReadableDatabase();
                Cursor salesTable = Main.this.DbHelper.getSalesTable();
                if (salesTable == null) {
                    Toast.makeText(Main.this, "There is no Sales transaction", Toast.LENGTH_SHORT).show();
                } else if (salesTable.moveToFirst()) {
                    Main.this.startActivity(new Intent(Main.this, com.example.inventorymanagment.product.detail.SalesDetail.class));
                }
            }
        });
        askForPermission();
    }

    private void askForPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == -1) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            onRequestPermissionsResult(1, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, grantResults);
        }
    }


    public void openChart() {
        String[] arrayFromCursor = arrayFromCursor(this.DbHelper.selectColumnNames());
        int[] qArrayFromCursor = qArrayFromCursor(this.DbHelper.selectQuantity());
        @SuppressLint("RestrictedApi") int[] iArr = {-16776961, -65281, -16711936, -16711681, SupportMenu.CATEGORY_MASK, InputDeviceCompat.SOURCE_ANY, -7829368, -16711936};
        CategorySeries categorySeries = new CategorySeries(" Percentage of products in the inventory ");
        for (int i = 0; i < qArrayFromCursor.length; i++) {
            categorySeries.add(arrayFromCursor[i], (double) qArrayFromCursor[i]);
        }
        DefaultRenderer defaultRenderer = new DefaultRenderer();
        for (int i2 = 0; i2 < qArrayFromCursor.length; i2++) {
            SimpleSeriesRenderer simpleSeriesRenderer = new SimpleSeriesRenderer();
            if (i2 < iArr.length) {
                simpleSeriesRenderer.setColor(iArr[i2]);
            } else {
                simpleSeriesRenderer.setColor(getRandomColor());
            }
            simpleSeriesRenderer.setDisplayBoundingPoints(true);
            defaultRenderer.addSeriesRenderer(simpleSeriesRenderer);
        }
        defaultRenderer.setChartTitle("Percentage of products in the inventory ");
        defaultRenderer.setChartTitleTextSize(50.0f);
        defaultRenderer.setLabelsColor(-7829368);
        defaultRenderer.setLabelsTextSize(30.0f);
        defaultRenderer.setZoomButtonsVisible(false);
        startActivity(ChartFactory.getPieChartIntent(getBaseContext(), categorySeries, defaultRenderer, "AChartEnginePieChartDemo"));
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @SuppressLint("Range")
    public String[] arrayFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        String[] strArr = new String[count];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < count; i++) {
                strArr[i] = cursor.getString(cursor.getColumnIndex("name"));
                cursor.moveToNext();
            }
        }
        return strArr;
    }

    @SuppressLint("Range")
    public int[] qArrayFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        int[] iArr = new int[count];
        if (cursor.moveToFirst()) {
            for (int i = 0; i < count; i++) {
                iArr[i] = cursor.getInt(cursor.getColumnIndex("quantity"));
                cursor.moveToNext();
            }
        }
        return iArr;
    }

    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            drawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
//        if (itemId != R.id.home) {
        if (itemId == R.id.share) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", "Hey, check out the best Application for the Inventory Management \n--------------------------------------\n Link :+https://play.google.com/store/apps/details?id=" + getPackageName());
            intent.setType("text/plain");
            startActivity(intent);
//            } else if (itemId == R.id.rate) {
//                Intent intent2 = new Intent("android.intent.action.VIEW");
//                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
//                startActivity(intent2);
        } else if (itemId == R.id.user_manual) {
            startActivity(new Intent(this, HowTo.class));
        } else if (itemId == R.id.about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (itemId == R.id.rateUs) {
            Intent intent3 = new Intent("android.intent.action.VIEW");
            intent3.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(intent3);
        } else if (itemId == R.id.contact_us) {
            Intent intent4 = new Intent("android.intent.action.VIEW");
            intent4.setData(Uri.parse("mailto: crazyappsolutions@gmail.com"));
            startActivity(intent4);
//            } else if (itemId == R.id.support_us) {
////                loadRewardedVideoAd();
//                StaticFunctions.showProgress(this);
        }
//        }
        //        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
//        navView.get().setChecked(false);
        drawerLayout.closeDrawer(GravityCompat.START);


        return false;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }

/*
    private void presentShowcaseSequence() {
        ShowcaseConfig showcaseConfig = new ShowcaseConfig();
        showcaseConfig.setDelay(500);
        MaterialShowcaseSequence materialShowcaseSequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
        materialShowcaseSequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
            public void onShow(MaterialShowcaseView materialShowcaseView, int i) {

            }
        });
        materialShowcaseSequence.setConfig(showcaseConfig);
        materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(this.addVendor).setContentText((CharSequence) "Add vendor first of all, because you will be allowed to enter the products corresponding to some vendor").setTitleText((CharSequence) "ADD VENDOR").setDismissText((CharSequence) "NEXT").withRectangleShape().build());
        materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(this.addProduct).setDismissText((CharSequence) "NEXT").setTitleText((CharSequence) "ADD PRODUCT").setContentText((CharSequence) "Now add the product that is supplied by the vendor already present in vendor list").withRectangleShape().build());
        materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(this.saleProduct).setTitleText((CharSequence) "SALE PRODUCT").setDismissText((CharSequence) "NEXT").setContentText((CharSequence) "Now you can sale any produ ct that is stored in the product list").withRectangleShape().build());
        materialShowcaseSequence.start();
    }
*/


    public void onRewardedVideoAdFailedToLoad(int i) {
        StaticFunctions.dismiss();
        if (i == 0) {
            Toast.makeText(this, "Ad could not be loaded due to an internal error", Toast.LENGTH_LONG).show();
        } else if (i == 1) {
            Toast.makeText(this, "Invalid Ad request", Toast.LENGTH_LONG).show();
        } else if (i == 2) {
            Toast.makeText(this, "Ad could not be loaded due to slow network", Toast.LENGTH_LONG).show();
        } else if (i == 3) {
            Toast.makeText(this, "No ads to serve currently", Toast.LENGTH_LONG).show();
        }
    }

    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "Thank You !", Toast.LENGTH_SHORT).show();
    }
}
