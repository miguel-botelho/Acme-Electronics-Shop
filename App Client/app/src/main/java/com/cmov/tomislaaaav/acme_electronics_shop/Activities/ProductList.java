package com.cmov.tomislaaaav.acme_electronics_shop.Activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmov.tomislaaaav.acme_electronics_shop.R;
import com.cmov.tomislaaaav.acme_electronics_shop.RestAPI;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Product;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class ProductList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    User user = new User();
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<MyListItem> products_names = new ArrayList<MyListItem>();
    RestAPI restAPI = new RestAPI();
    ArrayAdapter<MyListItem> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

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

        // Call Rest API to get all products
        String[] str = new String[1];
        str[0] = "getAllProducts";
        new ProductList.productAPI().execute(str);
        ListView listView = (ListView) findViewById(R.id.products_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyListItem item = (MyListItem) adapterView.getItemAtPosition(i);
                Log.i(TAG, item.getId() + " " + item.getMaker());

                Intent intent = new Intent(
                        ProductList.this,
                        ProductDetails.class);
                intent.putExtra("user", user);
                for (int k = 0; k < products.size(); k++) {
                    if (item.getId() == products.get(k).getId())
                        intent.putExtra("product", products.get(k));
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
                    ProductList.this,
                    FrontPage.class);
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
        } else if (id == R.id.nav_products) {
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(
                    ProductList.this,
                    Cart.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(
                    ProductList.this,
                    PastOrders.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_front_page) {
            Intent intent = new Intent(
                    ProductList.this,
                    FrontPage.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class productAPI extends AsyncTask<String, Void, String> {

        public productAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "getAllProducts":
                    return restAPI.getAllProducts();
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
                JSONArray arr = null;
                try {
                    obj = new JSONObject(s);
                    Product p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), 0);
                    Log.i(TAG, p.getModel());
                    products.add(p);
                } catch (JSONException ex) {
                    // edited, to include @Arthur's comment
                    // e.g. in case JSONArray is valid as well...
                }
                if (products.size() < 1) {
                    try {
                        arr = new JSONArray(s);
                        for (int i = 0; i < arr.length(); i++) {
                            obj = arr.getJSONObject(i);
                            Product p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), 0);
                            products.add(p);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 0; j < products.size(); j++) {
                    MyListItem temp = new MyListItem(products.get(j).getId(), products.get(j).getMaker(), products.get(j).getModel());
                    products_names.add(temp);
                }
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    public class MyListItem {
        private int id;
        private String maker;
        private String model;

        public MyListItem(int id, String maker, String model) {
            this.id = id;
            this.maker = maker;
            this.model = model;
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

        @Override
        public String toString() {
            return maker + " " + model;
        }
    }
}
