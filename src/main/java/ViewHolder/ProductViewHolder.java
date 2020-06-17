package ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.surgiwave.R;
import com.squareup.picasso.Picasso;

import Interface.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    private ItemClickListener itemClickListener;
    public TextView name,description,price;
    public  ImageView imageView;





    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);



         name = itemView.findViewById(R.id.product_name);
         description = itemView.findViewById(R.id.product_descri);
         price = itemView.findViewById(R.id.product_price);
         imageView = itemView.findViewById(R.id.product_image);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
