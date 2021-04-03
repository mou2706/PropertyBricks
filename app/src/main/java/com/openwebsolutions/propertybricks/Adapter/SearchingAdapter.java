package com.openwebsolutions.propertybricks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openwebsolutions.propertybricks.Model.Search_Item.Search;
import com.openwebsolutions.propertybricks.R;

import java.util.ArrayList;

public class SearchingAdapter extends  RecyclerView.Adapter<SearchingAdapter.MyViewHolder>{

        ArrayList<Search> arrayList_pro=new ArrayList<>();
        Context context;
        private SearchingAdapter.OnItemClickListener mItemClickListener;

        public ArrayList<SearchingAdapter.MyViewHolder> cellviews = null;
        SearchingAdapter.MyViewHolder ownAdapter;

        public SearchingAdapter(ArrayList<Search> arrayList_pro, Context context) {
        this.arrayList_pro = arrayList_pro;
        this.context = context;
        cellviews=new ArrayList<>();
        }

        @NonNull
        @Override
        public SearchingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_auto_design,viewGroup,false);
                ownAdapter=new SearchingAdapter.MyViewHolder(view);
                return ownAdapter;
                }
        @Override
        public void onBindViewHolder(@NonNull SearchingAdapter.MyViewHolder ownAdapter, int i) {

            try{

                ownAdapter.textView_.setText(arrayList_pro.get(i).getName());
                ownAdapter.tv_type.setText(arrayList_pro.get(i).getType());
                ownAdapter.tv_loc.setText(arrayList_pro.get(i).getLocation());
            }
            catch (Exception e){

            }

                }
        @Override
        public int getItemCount() {
                return arrayList_pro.size();
                }
        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

            TextView textView_,tv_type,tv_loc;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                textView_=itemView.findViewById(R.id.textView_);
                tv_type=itemView.findViewById(R.id.tv_type);
                tv_loc=itemView.findViewById(R.id.tv_loc);

                itemView.setOnClickListener(this);
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
            public void SetOnItemClickListener(final SearchingAdapter.OnItemClickListener mItemClickListener) {
                this.mItemClickListener = mItemClickListener;
            }
}