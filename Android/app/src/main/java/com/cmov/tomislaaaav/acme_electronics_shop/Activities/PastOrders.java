package com.cmov.tomislaaaav.acme_electronics_shop.Activities;

import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmov.tomislaaaav.acme_electronics_shop.R;
import com.cmov.tomislaaaav.acme_electronics_shop.RestAPI;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Order;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Product;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class PastOrders extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    User user = new User();
    ArrayList<Order> orders = new ArrayList<Order>();
    ArrayList<PastOrders.MyListItem> orders_uuid = new ArrayList<PastOrders.MyListItem>();
    RestAPI restAPI = new RestAPI();
    ArrayAdapter<PastOrders.MyListItem> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.past_orders);

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
        user = (User)i.getSerializableExtra("user");
        TextView name = navigationView.getHeaderView(0).findViewById(R.id.username);
        name.setText(user.getName());
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        email.setText(user.getEmail());


        // Call Rest API to get user's cart
        String[] str = new String[1];
        str[0] = "getPreviousOrders";
        new pastOrdersAPI().execute(str);

        ListView listView = (ListView) findViewById(R.id.past_orders);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PastOrders.MyListItem item = (PastOrders.MyListItem) adapterView.getItemAtPosition(i);
                Log.i(TAG, item.getId());

                Intent intent = new Intent(
                        PastOrders.this,
                        OrderDetails.class);
                intent.putExtra("user", user);
                for (int k = 0; k < orders.size(); k++) {
                    Log.i(TAG, orders_uuid.toString());
                    Log.i(TAG, orders.toString());
                    if (item.getId().equals(orders.get(k).getId()))
                        intent.putExtra("order", orders.get(k));
                }
                startActivity(intent);
                finish();
            }
        });
        listAdapter = new ArrayAdapter<>(this, R.layout.simplerow, orders_uuid);

        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(
                    PastOrders.this,
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
            Intent intent = new Intent(
                    PastOrders.this,
                    ProductList.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(
                    PastOrders.this,
                    Cart.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_orders) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyListItem {
        private String id;

        public MyListItem(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Order nº: " + id;
        }
    }

    public class pastOrdersAPI extends AsyncTask<String, Void, String> {

        public pastOrdersAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "getPreviousOrders":
                    return restAPI.retrievePreviousOrdersByID(user.getId());
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, s);
            if (s.equals("\"Error\"")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else if (isJSONValid(s)) {
                // it's the product from carts

                JSONObject obj = null;
                JSONArray arr= null;
                    try {
                        arr = new JSONArray(s);
                        // parse every order
                        for (int i = 0; i < arr.length(); i++) {
                            Log.i(TAG, arr.getJSONObject(i).getString("day"));
                            String[] dates = arr.getJSONObject(i).getString("day").split("-");

                            Date date = new Date(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
                            String id = arr.getJSONObject(i).getString("idOrder");
                            JSONArray products = arr.getJSONObject(i).getJSONArray("products");
                            ArrayList<Product> temp1= new ArrayList<Product>();
                            for (int j = 0; j < products.length(); j++) {
                                obj = products.getJSONObject(j);
                                Product p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), obj.getInt("quantity"));
                                Log.i(TAG, obj.getString("maker"));
                                temp1.add(p);
                            }
                            Order o = new Order(id, date, temp1);
                            orders.add(o);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                for (int j = 0; j < orders.size(); j++) {
                    MyListItem temp = new MyListItem(orders.get(j).getId());
                    orders_uuid.add(temp);
                }
                Log.i(TAG, orders_uuid.toString());
                listAdapter.notifyDataSetChanged();

            } else if (s.contains("Orders")) {
                Toast.makeText(getApplicationContext(), "No orders!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Product action completed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
