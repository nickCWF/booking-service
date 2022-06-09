package com.example.prototype2.customer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prototype2.R;
import com.example.prototype2.owner.barberService;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class customerBarberServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Activity mActivity;
    public static ArrayList<customerBarberService> mContentList;
    ArrayList<customerBarberService> checkedBarberService = new ArrayList<>();


    public customerBarberServiceAdapter(Context mContext, Activity mActivity, ArrayList<customerBarberService> mContentList){
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mContentList = mContentList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer_barberservie, parent, false);
        return new ViewHolder(view, viewType);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView barberService_name, barberService_price;
        CheckBox barberServiceCheckbox;

        public ViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            barberService_name = itemView.findViewById(R.id.customerBarberServiceName);
            barberService_price = itemView.findViewById(R.id.customerBarberServicePrice);
            barberServiceCheckbox = itemView.findViewById(R.id.barberServiceCheckBox);


        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder mainHolder = (ViewHolder) holder;
        final int pos = position;
        final customerBarberService model = mContentList.get(position);

        mainHolder.barberService_name.setText(model.getBarberServiceName());
        mainHolder.barberService_price.setText("RM "+model.getBarberServicePrice());
        mainHolder.barberServiceCheckbox.setChecked(model.isSelected());
        /**Get checkbox **/
        mainHolder.barberServiceCheckbox.setTag(position);

        mainHolder.barberServiceCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer pos = (Integer) mainHolder.barberServiceCheckbox.getTag();


                if (mContentList.get(pos).isSelected()){
                    mContentList.get(pos).setSelected(false);
                    Toast.makeText(mContext, mContentList.get(pos).getBarberServiceName() + " not clicked!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mContentList.get(pos).setSelected(true);
                    Toast.makeText(mContext, mContentList.get(pos).getBarberServiceName() + " clicked!", Toast.LENGTH_SHORT).show();
                }

            }
        });


//        mainHolder.barberServiceCheckbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final boolean isChecked = mainHolder.barberServiceCheckbox.isChecked();
//
//                for(int i = 0; i<mContentList.size();i++){
//                    if(isChecked){
//                        if(!checkedBarberService.contains(mContentList.get(pos).getBarberServiceName())){
//                            checkedBarberService.add(i, mContentList.get(pos).getBarberServiceName());
//                        }
//                    }
//
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }
}
