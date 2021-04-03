package com.openwebsolutions.propertybricks.Adapter;




import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.openwebsolutions.propertybricks.Model.BillingModel;
import com.openwebsolutions.propertybricks.R;

import java.util.ArrayList;
import java.util.List;

public class BillingListAdapter extends BaseAdapter {
    Activity mContext;
    LayoutInflater inflater;
    private List<BillingModel> billingModelList = null;
//    private BillingListAdapter.OnItemClickListener mItemClickListener;

    private ArrayList<BillingModel> arraylist;

    public BillingListAdapter(Activity mContext, List<BillingModel> billingModelList) {
        this.mContext = mContext;
        this.billingModelList = billingModelList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<BillingModel>();
        this.arraylist.addAll(billingModelList);
    }
    public class ViewHolder {
        TextView month;
        TextView tv_paid;


    }


    @Override
    public int getCount() {
        return billingModelList.size();
    }


    @Override
    public Object getItem(int position) {
        return billingModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.invoice_activity, null);
            holder.month = (TextView) convertView.findViewById(R.id.month);
            holder.tv_paid = (TextView) convertView.findViewById(R.id.tv_paid);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.month.setText(billingModelList.get(position).getMonth());
        holder.tv_paid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Let the event be handled in onItemClick()
//                final Dialog dialog = new Dialog(mContext);
//                // Include dialog.xml file
//                dialog.setContentView(R.layout.activity_custom_dialog);
//                // Set dialog title
//                dialog.setTitle("Custom Dialog");
//                dialog.show();
//               TextView apply = (TextView) dialog.findViewById(R.id.apply);
//               apply.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       Intent intent = new Intent(mContext, InvoiceActivity.class);
//                       mContext.startActivity(intent);
//                   }
//               });
//                TextView continue_without_load = (TextView) dialog.findViewById(R.id.continue_without_load);
//                continue_without_load.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mContext, InvoiceActivity.class);
//                        mContext.startActivity(intent);
//                    }
//                });
//                ImageView cancel = (ImageView) dialog.findViewById(R.id.cancel);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });


//                CustomDialog customDialog=new CustomDialog(mContext);
//                customDialog.show();
//              Intent intent=new Intent(mContext,CustomDialog.class);
//              mContext.startActivity(intent);
//              mContext.finish();


            }
        });
        return convertView;
    }
//    @Override
//    public void onClick(View v) {
//        if (mItemClickListener != null) {
//            mItemClickListener.onItemClick(v, getLayoutPosition());
//        }
//    }
//
//    public class OnItemClickListener {
//        void onItemClick(View view, int position);
//        void onItemLongClick(View view, int position);
//    }
//    public void SetOnItemClickListener(final BillingListAdapter.OnItemClickListener mItemClickListener) {
//        this.mItemClickListener = mItemClickListener;
//    }
}