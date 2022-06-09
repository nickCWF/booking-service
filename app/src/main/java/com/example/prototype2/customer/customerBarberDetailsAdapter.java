package com.example.prototype2.customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.owner.barber;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class customerBarberDetailsAdapter extends FirestoreRecyclerAdapter<ratingData,customerBarberDetailsAdapter.holder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public customerBarberDetailsAdapter(@NonNull FirestoreRecyclerOptions<ratingData> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull customerBarberDetailsAdapter.holder holder, int position, @NonNull ratingData model) {
        holder.ratingBar.setRating(model.getRate().floatValue());
        holder.comment.setText(model.getComment());
    }

    @NonNull
    @Override
    public customerBarberDetailsAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer_rating,parent,false);
        return new holder(view);
    }

    public class holder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView comment;

        public holder(@NonNull View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingReview);
            comment =itemView.findViewById(R.id.commentReview);
        }
    }
}
