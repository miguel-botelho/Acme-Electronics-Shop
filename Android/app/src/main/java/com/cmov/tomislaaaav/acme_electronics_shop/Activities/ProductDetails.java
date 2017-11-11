package com.cmov.tomislaaaav.acme_electronics_shop.Activities;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cmov.tomislaaaav.acme_electronics_shop.R;
import com.cmov.tomislaaaav.acme_electronics_shop.RestAPI;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.Product;
import com.cmov.tomislaaaav.acme_electronics_shop.Structures.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class ProductDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    User user = new User();
    RestAPI restAPI = new RestAPI();
    Product product = new Product();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product);

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
        product = (Product)i.getSerializableExtra("product");

        TextView name = navigationView.getHeaderView(0).findViewById(R.id.username);
        name.setText(user.getName());
        TextView email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        email.setText(user.getEmail());
        TextView description = findViewById(R.id.TextViewDescriptionDetails);
        TextView maker = findViewById(R.id.TextViewMakerDetails);
        TextView model = findViewById(R.id.TextViewModelDetails);
        TextView price = findViewById(R.id.TextViewPriceDetails);
        description.setText("Description: " + product.getDescription());
        maker.setText("Maker: " + product.getMaker());
        model.setText("Model: " + product.getModel());
        price.setText("Price: " + product.getPrice() + "€");

        Button btn = findViewById(R.id.btn_add_to_cart);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // db
                String[] str = new String[3];
                str[0] = "addProductToCart";
                new ProductDetails.productAPI().execute(str);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(
                    ProductDetails.this,
                    ProductList.class);
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
                    ProductDetails.this,
                    ProductList.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(
                    ProductDetails.this,
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

    public class productAPI extends AsyncTask<String, Void, String> {

        public productAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "addProductToCart":
                    return restAPI.addProductToCart(user.getId(), product.getId() + "");
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
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                // Launch login activity
                Intent intent = new Intent(
                        ProductDetails.this,
                        Cart.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        }
    }
}
