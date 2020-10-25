package com.example.contactshelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Contact[] contacts;
    private OnItemClickListener onItemClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        OnItemClickListener onItemClickListener;

        ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            this.view = view;
            this.onItemClickListener = onItemClickListener;

            view.setOnClickListener(this);
            view.findViewById(R.id.call_img).setOnClickListener(this);
            view.findViewById(R.id.whatsapp_img).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            switch (v.getTag().toString()) {
                case "row":
                    onItemClickListener.onItemClick(getAdapterPosition());
                    break;
                case "call":
                    onItemClickListener.onCallClick(getAdapterPosition());
                    break;
                case "whatsapp":
                    onItemClickListener.onWhatsappClick(getAdapterPosition());
                    break;
            }
        }
    }

    RecyclerAdapter(Contact[] contacts, OnItemClickListener onItemClickListener) {
        this.contacts = contacts;
        this.onItemClickListener = onItemClickListener;
    }

    void setContacts(Contact[] contacts) {
        this.contacts = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
         View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
         row.findViewById(R.id.call_img).setTag("call");
         row.findViewById(R.id.whatsapp_img).setTag("whatsapp");
         row.setTag("row");
        return new ViewHolder(row, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.view.findViewById(R.id.name_tv)).setText(contacts[position].name);
        ((TextView) holder.view.findViewById(R.id.number_tv)).setText(contacts[position].number + "");
    }

    @Override
    public int getItemCount() {
        return contacts.length;
    }
}