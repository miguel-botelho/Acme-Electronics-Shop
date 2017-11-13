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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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

import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by m_bot on 10/11/2017.
 */

public class Cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    User user = new User();
    ArrayList<Product> cart = new ArrayList<Product>();
    RestAPI restAPI = new RestAPI();
    CartItemAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

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

        ListView listView = (ListView) findViewById(R.id.Items);
        adapter = new CartItemAdapter(cart, this);
        listView.setAdapter(adapter);

        Button pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // db
                String[] str = new String[2];
                str[0] = "payOrder";
                String raw = "123456789";
                /*
                Signature instance = Signature.getInstance("SHA1withRSA");
                instance.initSign(user.getPrivateKey());
                instance.update(rawString.getBytes());
                signed = instance.sign();
                */
                str[1] = "";
                new cartAPI().execute(str);
            }
        });

        // Call Rest API to get user's cart
        String[] str = new String[1];
        str[0] = "getCart";
        new cartAPI().execute(str);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(
                    Cart.this,
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
                    Cart.this,
                    ProductList.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_cart) {
        } else if (id == R.id.nav_orders) {
            Intent intent = new Intent(
                    Cart.this,
                    PastOrders.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_front_page) {
            Intent intent = new Intent(
                    Cart.this,
                    FrontPage.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CartItemAdapter extends BaseAdapter implements ListAdapter {

        private ArrayList<Product> list = new ArrayList<Product>();
        private Context context;

        public CartItemAdapter(ArrayList<Product> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return list.get(i).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.product_shopping_cart, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position).getMaker() + " " + list.get(position).getModel());

            final TextView quantity = (TextView) view.findViewById(R.id.quantity);
            Log.i(TAG, list.get(position).getQuantity() + "");
            if (Integer.toString(list.get(position).getQuantity()).length() == 1) {
                quantity.setText("0" + list.get(position).getQuantity() + "");
            } else {
                quantity.setText(list.get(position).getQuantity() + "");
            }

            //Handle buttons and add onClickListeners
            Button subtractBtn = (Button)view.findViewById(R.id.subtract_btn);
            Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
            Button addBtn = (Button)view.findViewById(R.id.add_btn);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    // remove from the UI
                    // delete from db
                    String[] str = new String[2];
                    str[0] = "removeFromCart";
                    str[1] = list.get(position).getId() + "";
                    new Cart.cartAPI().execute(str);
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });
            subtractBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    if (Integer.parseInt(quantity.getText().toString()) < 2) {
                        quantity.setText("1");
                    } else {
                        // change db
                        String[] str = new String[3];
                        str[0] = "subtractFromCart";
                        str[2] = list.get(position).getQuantity() - 1 + "";
                        str[1] = list.get(position).getId() + "";
                        new Cart.cartAPI().execute(str);
                        list.get(position).setQuantity(list.get(position).getQuantity() - 1);
                        notifyDataSetChanged();
                    }
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    //change UI
                    quantity.setText(list.get(position).getQuantity() + 1 + "");
                    // db
                    String[] str = new String[3];
                    str[0] = "addToCart";
                    str[2] = list.get(position).getQuantity() + 1 + "";
                    str[1] = list.get(position).getId() + "";
                    new Cart.cartAPI().execute(str);

                    list.get(position).setQuantity(list.get(position).getQuantity() + 1);
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

    public class cartAPI extends AsyncTask<String, Void, String> {

        public cartAPI() {
            super();
        }

        @Override
        protected String doInBackground(String... strings) {
            switch (strings[0]) {
                case "addToCart":
                    return restAPI.addQuantityOfProductToCart(user.getId(), strings[1], strings[2]);
                case "removeFromCart":
                    return restAPI.removeProductFromCart(user.getId(), strings[1]);
                case "subtractFromCart":
                    return restAPI.removeQuantityOfProductFromCart(user.getId(), strings[1], strings[2]);
                case "getCart":
                    return restAPI.getCartByUser(user.getId());
                case "payOrder":
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("idUser", user.getId());

                    JSONObject us = new JSONObject(map);
                    return restAPI.payOrder(us);
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i(TAG, "TESTE123");
            if (s.equals("\"Error\"")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            } else if (s.contains("-")) {
                Toast.makeText(getApplicationContext(), "Your order " + s + " has been placed!", Toast.LENGTH_SHORT).show();

                // Launch login activity
                Intent intent = new Intent(
                        Cart.this,
                        PastOrders.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            } else if (isJSONValid(s)) {
                // it's the product from carts
                JSONObject obj = null;
                JSONArray arr= null;
                try {
                    obj = new JSONObject(s);
                    Log.i(TAG, obj.getString("maker"));
                    Product p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), obj.getInt("quantity"));
                    cart.add(p);
                } catch (JSONException ex) {
                    // edited, to include @Arthur's comment
                    // e.g. in case JSONArray is valid as well...
                    try {
                        arr = new JSONArray(s);
                        for (int i = 0; i < arr.length(); i++) {
                            obj = arr.getJSONObject(i);
                            Product p = new Product(obj.getInt("idProduct"), obj.getString("maker"), obj.getString("model"), obj.getInt("price"), obj.getString("description"), obj.getInt("quantity"));
                            Log.i(TAG, obj.getString("maker"));
                            cart.add(p);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i(TAG, cart.toString());
                adapter.notifyDataSetChanged();

            } else if (s.contains("Cart")) {
                Toast.makeText(getApplicationContext(), "No items in the cart!", Toast.LENGTH_SHORT).show();
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
