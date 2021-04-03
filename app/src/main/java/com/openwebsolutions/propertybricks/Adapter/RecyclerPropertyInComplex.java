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
import android.widget.TextView;

import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.DatumViewProperty;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerPropertyInComplex extends RecyclerView.Adapter<RecyclerPropertyInComplex.MyViewHolder>{
    ArrayList<DatumViewProperty> arrayList_pro=new ArrayList<>();
    Context context;
    private RecyclerPropertyInComplex.OnItemClickListener mItemClickListener;

    public ArrayList<RecyclerPropertyInComplex.MyViewHolder> cellviews = null;
    RecyclerPropertyInComplex.MyViewHolder ownAdapter;

    public RecyclerPropertyInComplex( ArrayList<DatumViewProperty> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;
        cellviews=new ArrayList<>();

    }

    @NonNull
    @Override
    public RecyclerPropertyInComplex.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_list_property,viewGroup,false);
        ownAdapter=new RecyclerPropertyInComplex.MyViewHolder(view);
        return ownAdapter;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerPropertyInComplex.MyViewHolder ownAdapter, int i) {

        try {
            ownAdapter.propertyname.setText(arrayList_pro.get(i).getPropertyName());
            ownAdapter.propertylocation.setText(arrayList_pro.get(i).getPropertyLocation());

            Picasso.get().load(AppData.image_url + arrayList_pro.get(i).getImage()).into(ownAdapter.propertyimage);

            if(arrayList_pro.get(i).getTenant()!=null){
                ownAdapter.lin_tenants_details.setVisibility(View.VISIBLE);
                Picasso.get().load(AppData.image_url_tenant + arrayList_pro.get(i).getTenant().getImage()).into(ownAdapter.tenant_pic);
                ownAdapter.tenant_nam.setText(arrayList_pro.get(i).getTenant().getTenantOwnerName());
                ownAdapter.tenant_entry_date.setText(arrayList_pro.get(i).getTenant().getTenantOwnerEntrydate());
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

        ImageView propertyimage,deleteproperty;
        TextView propertyname,propertylocation,tenant_nam,tenant_entry_date,tenant_viewdetails;
        LinearLayout lin_tenants_details,lin_property_underComplex;

        ImageView tenant_pic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyimage=itemView.findViewById(R.id.propertyimage);
            propertyname=itemView.findViewById(R.id.propertyname);
            propertylocation=itemView.findViewById(R.id.propertylocation);
            deleteproperty=itemView.findViewById(R.id.deleteproperty);
            tenant_pic=itemView.findViewById(R.id.tenant_pic);
            tenant_nam=itemView.findViewById(R.id.tenant_nam);
            tenant_entry_date=itemView.findViewById(R.id.tenant_entry_date);
            tenant_viewdetails=itemView.findViewById(R.id.tenant_viewdetails);
            lin_tenants_details=itemView.findViewById(R.id.lin_tenants_details);
            lin_property_underComplex=itemView.findViewById(R.id.lin_property_underComplex);

            lin_property_underComplex.setOnClickListener(this);

            deleteproperty.setOnClickListener(this);
            tenant_viewdetails.setOnClickListener(this);

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
    public void SetOnItemClickListener(final RecyclerPropertyInComplex.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}

