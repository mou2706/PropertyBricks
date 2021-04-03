package com.openwebsolutions.propertybricks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Model.GetTenantDetails.Datum_Tenant;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter_Tenant extends RecyclerView.Adapter<RecyclerAdapter_Tenant.MyViewHolder>{

    ArrayList<Datum_Tenant> arrayList_per=new ArrayList<>();
    Context context;
    public ArrayList<MyViewHolder> cellviews = null;
    MyViewHolder ownAdapter;

    private OnItemClickListener mItemClickListener;


    public RecyclerAdapter_Tenant(ArrayList<Datum_Tenant> arrayList_per, Context context) {
        this.arrayList_per = arrayList_per;
        this.context = context;
        cellviews=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerAdapter_Tenant.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_tenants_recycler,viewGroup,false);
        ownAdapter=new MyViewHolder(view);
        return ownAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_Tenant.MyViewHolder ownAdapter, int i) {
        try {
            ownAdapter.tenant_name.setText(arrayList_per.get(i).getTenantOwnerName());
            ownAdapter.tenant_email.setText(arrayList_per.get(i).getTenantOwnerEmail());
            ownAdapter.tenant_phone.setText(arrayList_per.get(i).getTenantOwnerPhone());
            ownAdapter.tenant_address.setText(arrayList_per.get(i).getTenantOwnerAddress());
            Picasso.get().load(AppData.image_url_tenant + arrayList_per.get(i).getImage()).into(ownAdapter.profile_img_tenant);
            ownAdapter.tenant_property_name.setText(arrayList_per.get(i).getProperty().getPropertyName());
        }
        catch(Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return arrayList_per.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        TextView tv_view_details_tenants,tenant_email,tenant_phone,tenant_address,tenant_name,tenant_property_name;
        ImageView profile_img_tenant;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tenant_name=itemView.findViewById(R.id.tenant_name);
            tv_view_details_tenants=itemView.findViewById(R.id.tv_view_details_tenants);
            tenant_email=itemView.findViewById(R.id.tenant_email);
            tenant_phone=itemView.findViewById(R.id.tenant_phone);
            tenant_address=itemView.findViewById(R.id.tenant_address);
            profile_img_tenant=itemView.findViewById(R.id.profile_img);
            tenant_property_name=itemView.findViewById(R.id.tenant_property_name);

            tv_view_details_tenants.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
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
