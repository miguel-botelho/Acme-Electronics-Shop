package com.cmov.tomislaaaav.acme_electronics_shop.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmov.tomislaaaav.acme_electronics_shop.R;
import com.cmov.tomislaaaav.acme_electronics_shop.RestAPI;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Order;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Product;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class OrderDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    final int WHITE = 0xFFFFFFFF;
    final int BLACK = 0xFF000000;
    final int WIDTH=500;

    User user = new User();
    Order order = new Order();
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<MyListItem> products_names = new ArrayList<MyListItem>();
    ArrayAdapter<MyListItem> listAdapter;
    RestAPI restAPI = new RestAPI();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_orders_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.username);
        name.setText(user.getName());
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        email.setText(user.getEmail());

        order = (Order) i.getSerializableExtra("order");

        TextView idOrder = (TextView) findViewById(R.id.id_order);
        idOrder.setText("Order: " + order.getId());
        TextView dateOrder = (TextView) findViewById(R.id.date_order);
        dateOrder.setText("Date: " + order.getDate().toString());
        TextView price = (TextView) findViewById(R.id.total_price_order);
        price.setText("Total Price: " + order.getTotalPrice());
        ImageView qrCode = (ImageView) findViewById(R.id.qr_code);
        try {
            Bitmap bitmap = encodeAsBitmap(order.getId());
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < order.getProducts().size(); j++) {
            MyListItem myListItem = new MyListItem(order.getProducts().get(j).getId(), order.getProducts().get(j).getMaker(), order.getProducts().get(j).getModel(), order.getProducts().get(j).getQuantity());
            products.add(order.getProducts().get(j));
            products_names.add(myListItem);
        }

        ListView listView = (ListView) findViewById(R.id.products);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyListItem item = (MyListItem) adapterView.getItemAtPosition(i);
                Log.i(TAG, item.getId() + " " + item.getMaker());

                Intent intent = new Intent(
                        OrderDetails.this,
                        ProductDetails.class);
                intent.putExtra("user", user);
                for (int j = 0; j < products_names.size(); j++) {
                    for (int k = 0; k < products.size(); k++) {
                        if (products_names.get(j).getId() == products.get(k).getId())
                            intent.putExtra("product", products.get(k));
                    }
                }
                startActivity(intent);
                finish();
            }
        });
        listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, products_names);

        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(
                    OrderDetails.this,
                    PastOrders.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan) {
            // Handle the camera action

            IntentIntegrator integrator = new IntentIntegrator(OrderDetails.this);
            integrator.initiateScan();
        } else if (id == R.id.nav_products) {
            Intent intent = new Intent(
                    OrderDetails.this,
                    ProductList.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(
                    OrderDetails.this,
                    Cart.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(
                    OrderDetails.this,
                    PastOrders.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_front_page) {
            Intent intent = new Intent(
                    OrderDetails.this,
                    FrontPage.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();
            Log.i("code", re);
            String[] strs = new String[2];
            strs[0] = "getProductByID";
            strs[1] = re;
            new OrderDetailsAPI().execute(strs);
        }
        // else continue with any other code you need in the method
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }

    public class OrderDetailsAPI extends AsyncTask<String, Void, String> {

        public OrderDetailsAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "getProductByID":
                    return restAPI.getProductByID(strings[1]);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, s);
            if (s.equals("\"Error\"")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else {
                // it's the product from carts
                JSONObject obj = null;
                Product p = null;
                try {
                    obj = new JSONObject(s);
                    p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), 0);
                    Log.i(TAG, p.getModel());
                    Intent intent = new Intent(
                            OrderDetails.this,
                            ProductDetails.class);
                    intent.putExtra("user", user);
                    intent.putExtra("product", p);
                    startActivity(intent);
                    finish();
                } catch (JSONException ex) {
                    // edited, to include @Arthur's comment
                    // e.g. in case JSONArray is valid as well...
                }

            }
        }
    }

    public class MyListItem {
        private int id;
        private String maker;
        private String model;
        private int quantity;

        public MyListItem(int id, String maker, String model, int quantity) {
            this.id = id;
            this.maker = maker;
            this.model = model;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMaker() {
            return maker;
        }

        public void setMaker(String maker) {
            this.maker = maker;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Name: " + maker + " " + model + "  " + "qty: " + quantity;
        }
    }
}

