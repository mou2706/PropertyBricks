package com.openwebsolutions.propertybricks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Model.GetProperty.Datum;
import com.openwebsolutions.propertybricks.Model.GetProperty.Tenant;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter_Property extends RecyclerView.Adapter<RecyclerAdapter_Property.MyViewHolder>{
    ArrayList<Datum> arrayList_pro=new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;
    int position;
    Tenant getPropertyModel;
    public ArrayList<MyViewHolder> cellviews = null;
    MyViewHolder ownAdapter;

    public RecyclerAdapter_Property(Tenant responseUser, ArrayList<Datum> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;
        cellviews=new ArrayList<>();
        this.getPropertyModel=responseUser;
    }

    @NonNull
    @Override
    public RecyclerAdapter_Property.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_property_recycler,viewGroup,false);
        ownAdapter=new MyViewHolder(view);
        return ownAdapter;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_Property.MyViewHolder ownAdapter, int i) {
        try {
            ownAdapter.tv_property_name.setText(arrayList_pro.get(i).getPropertyName());
            ownAdapter.tv_property_location.setText(arrayList_pro.get(i).getPropertyLocation());
            ownAdapter.tv_property_price.setText(arrayList_pro.get(i).getPropertyPrice());
            Picasso.get().load(AppData.image_url + arrayList_pro.get(i).getImage()).into(ownAdapter.iv_property_image);

            if(arrayList_pro.get(i).getTenant()!=null){
                ownAdapter.tenant_profile_picture.setVisibility(View.VISIBLE);
                ownAdapter.txt_tenant.setVisibility(View.VISIBLE);
                ownAdapter.tv_occupied.setVisibility(View.VISIBLE);
                ownAdapter.tv_vacant.setVisibility(View.GONE);
                ownAdapter.tv_current_status.setText("Occupied");
                ownAdapter.rel_tenant.setBackgroundResource(R.color.light_colorText);
               // Picasso.get().load(AppData.image_url + arrayList_pro.get(i));

                Picasso.get().load(AppData.image_url_tenant +arrayList_pro.get(i).getTenant().getImage()).into(ownAdapter.tenant_profile_picture);

            }
        }
        catch (Exception e){

        }
        cellviews.add(ownAdapter);
        Log.d("print","yes");
    }
    @Override
    public int getItemCount() {
            return arrayList_pro.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView iv_property_image;
        TextView tv_property_name, tv_property_location, tv_property_price;
        TextView tv_vacant,tv_occupied;
        TextView txt_tenant;
         ImageView tenant_profile_picture;
        TextView tv_current_status;
        RelativeLayout rel_tenant;

        LinearLayout lin_itemview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_property_name = itemView.findViewById(R.id.tv_property_name);
            tv_property_location = itemView.findViewById(R.id.tv_property_location);
            tv_property_price = itemView.findViewById(R.id.tv_property_price);
            tv_occupied=itemView.findViewById(R.id.tv_occupied);
            tv_vacant=itemView.findViewById(R.id.tv_vacant);
            tv_current_status=itemView.findViewById(R.id.tv_current_status);

            tv_vacant.setOnClickListener(this);
            tv_occupied.setOnClickListener(this);
            iv_property_image=itemView.findViewById(R.id.iv_property_image);
            txt_tenant=itemView.findViewById(R.id.txt_tenant);
            tenant_profile_picture=itemView.findViewById(R.id.tenant_profile_picture);
            rel_tenant=itemView.findViewById(R.id.rel_tenant);
            lin_itemview=itemView.findViewById(R.id.lin_itemview);
            lin_itemview.setOnClickListener(this);
            lin_itemview.setOnLongClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
        @Override
        public boolean onLongClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemLongClick(v, getLayoutPosition());
                return true;
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}

