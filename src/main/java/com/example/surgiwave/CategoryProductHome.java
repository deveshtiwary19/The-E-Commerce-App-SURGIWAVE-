package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import Model.Products;
import ViewHolder.ProductViewHolder;

public class CategoryProductHome extends AppCompatActivity {
     private  String selectedCategory="";
    private RecyclerView orderlist;
    private DatabaseReference ref;
    private String getCategory;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_product_home);

        selectedCategory = getIntent().getStringExtra("category");

        orderlist = findViewById(R.id.category_home_view);
        orderlist.setLayoutManager(new LinearLayoutManager(this));
        t= findViewById(R.id.toolbar1);

        ref = FirebaseDatabase.getInstance().getReference().child("Products");


        if(selectedCategory.equals("icu")){
            t.setText("Products-ICU");
        }
        if(selectedCategory.equals("nicu")){
            t.setText("Products-NICU");
        }
        if(selectedCategory.equals("ot")){
            t.setText("Products-OT");
        }
        if(selectedCategory.equals("genral")){
            t.setText("Products-GENRAL");
        }
        if(selectedCategory.equals("instruments")){
            t.setText("Products-SURGICAL INSTRUMENTS");
        }
        if(selectedCategory.equals("pipeline")){
            t.setText("Products-PIPELINE");
        }
        if(selectedCategory.equals("others")){
            t.setText("Products-OTHERS");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ref.orderByChild("category").startAt(selectedCategory).endAt(selectedCategory),Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
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
                                String id = products.getPid();
                                Intent in = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                in.putExtra("pid", id);
                                startActivity(in);

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                        return new ProductViewHolder(view);
                    }
                };
        orderlist.setAdapter(adapter);
        adapter.startListening();


    }

}


