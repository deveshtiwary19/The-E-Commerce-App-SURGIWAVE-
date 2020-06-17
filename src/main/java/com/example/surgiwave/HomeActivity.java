package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Products;
import Model.Users;
import Prevelant.Prevelant;
import ViewHolder.ProductViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mNavDrawer;

    FloatingActionButton cartButton;

    private RecyclerView orderlist;
    private DatabaseReference ref;
    private String chceck="";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_drawer_layout);








        cartButton=findViewById(R.id.cart_button);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        mNavDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mNavDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        mNavDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(Prevelant.currentOnlineUser.getName());
        Picasso.get().load(Prevelant.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView);


        orderlist=findViewById(R.id.home_view);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

        ref=FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ref,Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {

                        productViewHolder.name.setText(products.getPname());
                        productViewHolder.description.setText(products.getDescription());
                        productViewHolder.price.setText(products.getPrice());
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String id=products.getPid();
                               Intent in=new Intent(getApplicationContext(),ProductDetailsActivity.class);
                               in.putExtra("pid",id);
                               startActivity(in);

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout,parent,false);
                        return new ProductViewHolder(view);
                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();




        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(intent);
            }
        });








    }




    @Override
    protected void onStart()
    {
        super.onStart();



    }




    @Override
    public void onBackPressed() {

        if(mNavDrawer.isDrawerOpen(GravityCompat.START)){
            mNavDrawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_cart:
                Intent inten=new Intent(getApplicationContext(),CartActivity.class);
                startActivity(inten);
                break;
            case R.id.nav_search:
                Intent inte=new Intent(getApplicationContext(),SearchProductActivity.class);
                startActivity(inte);

                break;
            case R.id.nav_category:

                Intent ine=new Intent(getApplicationContext(),CategoryActivity.class);
                startActivity(ine);

                break;
            case R.id.nav_settings:
                Intent intent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                .setIcon(R.drawable.common_google_signin_btn_icon_light_normal_background)
                        .setMessage("Are you sure to logout?").setTitle("LOGOUT")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Paper.book().destroy();
                                finish();
                                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent1);
                            }
                        })
                        .setNegativeButton("No",null).show();
                break;

            case R.id.nav_contact:

                Intent intnt1=new Intent(getApplicationContext(),ContactUs.class);
                startActivity(intnt1);
                break;
        }
        mNavDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
