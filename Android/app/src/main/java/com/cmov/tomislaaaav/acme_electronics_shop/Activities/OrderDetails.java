package com.cmov.tomislaaaav.acme_electronics_shop.Activities;

import android.content.Intent;
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

import com.cmov.tomislaaaav.acme_electronics_shop.R;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Order;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Product;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class OrderDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    User user = new User();
    Order order = new Order();
    ArrayList<Product> products = new ArrayList<Product>();
    ArrayList<MyListItem> products_names = new ArrayList<MyListItem>();
    ArrayAdapter<MyListItem> listAdapter;

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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

