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
import com.openwebsolutions.propertybricks.Model.PropertyByLocation.Property;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListofPropertyByLocation_Adapter extends RecyclerView.Adapter<ListofPropertyByLocation_Adapter.MyViewHolder>{
        ArrayList<Property> arrayList_pro=new ArrayList<>();
        Context context;
        private ListofPropertyByLocation_Adapter.OnItemClickListener mItemClickListener;
        int position;

        public ArrayList<ListofPropertyByLocation_Adapter.MyViewHolder> cellviews = null;
        ListofPropertyByLocation_Adapter.MyViewHolder ownAdapter;

        public ListofPropertyByLocation_Adapter(ArrayList<Property> arrayList_pro, Context context) {
                this.arrayList_pro = arrayList_pro;
                this.context = context;
                cellviews=new ArrayList<>();
                }

@NonNull
@Override
public ListofPropertyByLocation_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_propertyadd_incomplex,viewGroup,false);
        ownAdapter=new ListofPropertyByLocation_Adapter.MyViewHolder(view);
        return ownAdapter;
        }
@Override
public void onBindViewHolder(@NonNull ListofPropertyByLocation_Adapter.MyViewHolder ownAdapter, int i) {
        try {

            ownAdapter.garment_title_property.setText(arrayList_pro.get(i).getPropertyName());
            ownAdapter.address_propertylist.setText(arrayList_pro.get(i).getPropertyLocation());
            Picasso.get().load(AppData.image_url + arrayList_pro.get(i).getImage()).into(ownAdapter.img_propertylist);
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

    TextView garment_title_property,address_propertylist;
    ImageView iv_add_propetylist,iv_remove_propetylist,img_propertylist;
    LinearLayout lin_pro_details;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        img_propertylist=itemView.findViewById(R.id.img_propertylist);
        iv_remove_propetylist=itemView.findViewById(R.id.iv_remove_propetylist);
        iv_add_propetylist=itemView.findViewById(R.id.iv_add_propetylist);

        garment_title_property=itemView.findViewById(R.id.garment_title_property);
        address_propertylist=itemView.findViewById(R.id.address_propertylist);
        lin_pro_details=itemView.findViewById(R.id.lin_pro_details);
        lin_pro_details.setOnClickListener(this);
        iv_add_propetylist.setOnClickListener(this);
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
    public void SetOnItemClickListener(final ListofPropertyByLocation_Adapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}