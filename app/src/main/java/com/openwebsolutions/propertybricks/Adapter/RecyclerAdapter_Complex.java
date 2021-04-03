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
import com.openwebsolutions.propertybricks.Model.GetComplexModel.Datum_Complex;
import com.openwebsolutions.propertybricks.Model.GetProperty.Tenant;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerAdapter_Complex extends RecyclerView.Adapter<RecyclerAdapter_Complex.MyViewHolder>{
    ArrayList<Datum_Complex> arrayList_pro=new ArrayList<>();
    Context context;
    private RecyclerAdapter_Complex.OnItemClickListener mItemClickListener;
    int position;
    Tenant getPropertyModel;
    public ArrayList<RecyclerAdapter_Complex.MyViewHolder> cellviews = null;
    RecyclerAdapter_Complex.MyViewHolder ownAdapter;

    public RecyclerAdapter_Complex(ArrayList<Datum_Complex> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;
        cellviews=new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerAdapter_Complex.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_complex,viewGroup,false);
        ownAdapter=new RecyclerAdapter_Complex.MyViewHolder(view);
        return ownAdapter;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter_Complex.MyViewHolder ownAdapter, int i) {
        try {
           ownAdapter.tv_complexname.setText(arrayList_pro.get(i).getComplexName());
            ownAdapter.tv_complexlocation.setText(arrayList_pro.get(i).getComplexLocation());
            Picasso.get().load(AppData.image_url_complex + arrayList_pro.get(i).getImage()).into(ownAdapter.complex_image);
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

        TextView tv_complexname,tv_complexlocation,tv_addproperty_complex,view_property;
        ImageView iv_addproperty_complex,complex_image;
        LinearLayout lin_itemview1;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_complexlocation=itemView.findViewById(R.id.tv_complexlocation);
            tv_complexname=itemView.findViewById(R.id.tv_complexname);
            tv_addproperty_complex=itemView.findViewById(R.id.tv_addproperty_complex);
            iv_addproperty_complex=itemView.findViewById(R.id.iv_addproperty_complex);
            complex_image=itemView.findViewById(R.id.complex_image);
            view_property=itemView.findViewById(R.id.view_property);
            lin_itemview1=itemView.findViewById(R.id.lin_itemview1);
            lin_itemview1.setOnClickListener(this);
            lin_itemview1.setOnLongClickListener(this);

            view_property.setOnClickListener(this);
            tv_addproperty_complex.setOnClickListener(this);
            iv_addproperty_complex.setOnClickListener(this);
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
    public void SetOnItemClickListener(final RecyclerAdapter_Complex.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}