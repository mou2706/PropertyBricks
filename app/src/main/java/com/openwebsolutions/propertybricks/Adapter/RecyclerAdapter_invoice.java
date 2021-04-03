package com.openwebsolutions.propertybricks.Adapter;

//public class RecyclerAdapter_invoice {
//}
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
import com.openwebsolutions.propertybricks.Model.BillingModel;
import com.openwebsolutions.propertybricks.Model.GetProperty.Datum;
import com.openwebsolutions.propertybricks.Model.GetProperty.Tenant;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter_invoice extends RecyclerView.Adapter<RecyclerAdapter_invoice.MyViewHolder>{
    ArrayList<BillingModel> arrayList_pro=new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;
    int position;
    Tenant getInvoice;
    public ArrayList<MyViewHolder> cellviews = null;
    MyViewHolder ownAdapter;

    public RecyclerAdapter_invoice( ArrayList<BillingModel> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;
        cellviews=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerAdapter_invoice.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_activity,viewGroup,false);
        ownAdapter=new MyViewHolder(view);
        return ownAdapter;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerAdapter_invoice.MyViewHolder ownAdapter, int i) {
        try {
            ownAdapter.month.setText(arrayList_pro.get(i).getMonth());
            Log.d("month",arrayList_pro.get(i).getMonth());
            Log.d("size",""+arrayList_pro.size());
            if(!arrayList_pro.get(i).getTenant_id().equals(null)){

                ownAdapter.status.setText("paid");
                ownAdapter.tv_paid.setVisibility(View.GONE);

                // Picasso.get().load(AppData.image_url + arrayList_pro.get(i));


            }



        }
        catch (Exception e){

        }
        cellviews.add(ownAdapter);
    }
    @Override
    public int getItemCount() {
        return arrayList_pro.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView month;
        TextView tv_paid;
        TextView status;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            month=itemView.findViewById(R.id.month);
            tv_paid=itemView.findViewById(R.id.tv_paid);
            status=itemView.findViewById(R.id.status);


            tv_paid.setOnClickListener(this);
            tv_paid.setOnLongClickListener(this);

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


