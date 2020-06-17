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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import Model.Products;
import ViewHolder.ProductViewHolder;

public class AdminDisplayHome extends AppCompatActivity {


    private RecyclerView orderlist;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_display_home);



        orderlist=findViewById(R.id.admin_home_view);
        orderlist.setLayoutManager(new LinearLayoutManager(this));

        ref= FirebaseDatabase.getInstance().getReference().child("Products");



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
                                Intent in=new Intent(getApplicationContext(),AdminMaintainProductActivity.class);
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

    }
}
