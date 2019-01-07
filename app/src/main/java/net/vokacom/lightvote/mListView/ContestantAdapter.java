package net.vokacom.lightvote.mListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.vokacom.lightvote.R;
import net.vokacom.lightvote.mData.Contestants;
import net.vokacom.lightvote.mData.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emma on 10/18/2017.
 */

public class ContestantAdapter extends RecyclerView.Adapter<ContestantAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Contestants> productList;
    private OnItemClickListener mListener;

    public ContestantAdapter(Context mCtx, List<Contestants> productList) {
        this.mCtx =  mCtx;
        this.productList = productList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener =  listener;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.custom_layout_contestant, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Contestants product = productList.get(position);

        //loading the image

        Glide.with(mCtx)
                .load(product.getImage())
                .into(holder.image);

        holder.textViewTitle.setText(product.getTitle());
        holder.textViewShortDesc.setText(product.getshort_description());
        holder.textViewRating.setText(String.valueOf(product.getTown()));

    }


    @Override
    public int getItemCount() {

        return productList.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {



        TextView textViewTitle, textViewShortDesc, textViewRating;
        ImageView image;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            image = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(productList != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);

                        }
                    }

                }
            });
        }
    }
}