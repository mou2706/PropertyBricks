package com.openwebsolutions.propertybricks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openwebsolutions.propertybricks.Model.Sqlite_ModelDemo.ModelDemo;
import com.openwebsolutions.propertybricks.R;

import java.util.ArrayList;

public class RecyclerRecentSearch extends RecyclerView.Adapter<RecyclerRecentSearch.MyViewHolder>{
    ArrayList<ModelDemo> arrayList_pro=new ArrayList<>();
    Context context;
    private RecyclerRecentSearch.OnItemClickListener mItemClickListener;

    RecyclerRecentSearch.MyViewHolder ownAdapter;

    public RecyclerRecentSearch( ArrayList<ModelDemo> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerRecentSearch.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_history_row,viewGroup,false);
        ownAdapter=new RecyclerRecentSearch.MyViewHolder(view);
        return ownAdapter;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerRecentSearch.MyViewHolder ownAdapter, int i) {

        try{
            ownAdapter.tv_recent_propertyname.setText(arrayList_pro.get(i).getName());
            ownAdapter.search_loc.setText(arrayList_pro.get(i).getLoc());
            ownAdapter.tv_recent_type.setText(arrayList_pro.get(i).getType());
        }
        catch (Exception e){

        }
    }
    @Override
    public int getItemCount() {
        return arrayList_pro.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tv_recent_propertyname,tv_recent_type,search_loc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_recent_propertyname=itemView.findViewById(R.id.tv_recent_propertyname);
            search_loc=itemView.findViewById(R.id.search_loc);
            tv_recent_type=itemView.findViewById(R.id.tv_recent_type);
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
    public void SetOnItemClickListener(final RecyclerRecentSearch.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
