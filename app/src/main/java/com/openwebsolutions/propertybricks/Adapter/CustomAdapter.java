package com.openwebsolutions.propertybricks.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.R;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> month;

    Context context;

    public CustomAdapter(Context context, ArrayList<String> month) {
        this.context = context;
        this.month = month;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_activity, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.month.setText(month.get(position));

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Toast.makeText(context, month.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return month.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView month;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            month = (TextView) itemView.findViewById(R.id.month);


        }
    }
}