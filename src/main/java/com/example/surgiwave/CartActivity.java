package com.example.surgiwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Model.Cart;
import Prevelant.Prevelant;
import ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txttotalAmount;
    private  float overTotalPrice=0;
    private float z=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn=findViewById(R.id.next_process_btn);

        txttotalAmount=findViewById(R.id.total_price);


        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                txttotalAmount.setText("Total Price= "+String.valueOf(overTotalPrice));



                Intent in=new Intent(getApplicationContext(),ConfirmFinalOrderActivity.class);
                in.putExtra("Total Price",String.valueOf(overTotalPrice));
                startActivity(in);
                finish();
            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();


        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options=
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                       .child(Prevelant.currentOnlineUser.getPhone()).child("Products"),Cart.class )
                        .build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                        cartViewHolder.txtProductquantity.setText("x"+cart.getQuantity());
                        cartViewHolder.txtProductPrice.setText("Price "+cart.getPrice());
                        cartViewHolder.txtProductName.setText(cart.getPname());



                        float oneTypeProductTotalPrice= Float.parseFloat(cart.getPrice().toString())*Float.parseFloat(cart.getQuantity().toString());
                        overTotalPrice=overTotalPrice+oneTypeProductTotalPrice;


                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]
                                        {
                                                "Edit",
                                                "Remove"

                                        };
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (which == 0) {
                                            Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                                            intent.putExtra("pid", cart.getPid());
                                            startActivity(intent);
                                            finish();
                                        }
                                        if (which == 1) {


                                            float oneTypeProductTotalPrice = Float.parseFloat(cart.getPrice().toString()) * Float.parseFloat(cart.getQuantity().toString());
                                            overTotalPrice = overTotalPrice - oneTypeProductTotalPrice;


                                            cartListRef.child("User View")
                                                    .child(Prevelant.currentOnlineUser.getPhone())
                                                    .child("Products")
                                                    .child(cart.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                z = 1;

                                                            }
                                                        }
                                                    });

                                            cartListRef.child("Admin View")
                                                    .child(Prevelant.currentOnlineUser.getPhone())
                                                    .child("Products")
                                                    .child(cart.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful() && z == 1) {
                                                                Toast.makeText(CartActivity.this, "Item Removed From Cart Sucessfully", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }
                                                    });

                                        }


                                    }
                                });

                                builder.show();


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                       CartViewHolder holder=new CartViewHolder(view);
                       return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();




    }
}
