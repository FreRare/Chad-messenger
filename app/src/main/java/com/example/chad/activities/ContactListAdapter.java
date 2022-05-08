package com.example.chad;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Adapter class for contact listing and filtering
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> implements Filterable {
    private static final String TAG = ContactAdapter.class.getName();

    private ArrayList<Contact> contacts;
    private ArrayList<Contact> allContacts;
    private Context context;
    private int lastPos = -1;

    ContactAdapter(Context context, ArrayList<Contact> contacts){
        this.contacts = contacts;
        this.allContacts = contacts;
        this.context = context;
    }

    //Creating view holder for contact list
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.context).inflate(R.layout.list_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact currentContact = this.contacts.get(position);
        holder.bindTo(currentContact);

        if(holder.getBindingAdapterPosition() > lastPos){
            Animation floatIn = AnimationUtils.loadAnimation(context, R.anim.float_in_contact_list_animation);
            holder.itemView.startAnimation(floatIn);
            lastPos = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() { return this.contacts.size(); }

    @Override
    public Filter getFilter() { return contactFilter; }

    private Filter contactFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Contact> filteredContacts = new ArrayList<>();
            FilterResults res = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                res.count = allContacts.size();
                res.values = allContacts;
            }else{
                String filterString = charSequence.toString().toLowerCase().trim();
                for(Contact c: allContacts){
                    if(c.getName().toLowerCase().contains(filterString) || c.getUsername().toLowerCase().contains(filterString)){
                        filteredContacts.add(c);
                    }
                }
                res.count = filteredContacts.size();
                res.values = filteredContacts;
            }

            return res;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            try {
                contacts = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }catch (Error e){
                Log.e(TAG, "publishResults: " + e.toString());
            }

        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private final String TAG = ViewHolder.class.getName();

        private TextView contactName;
        private TextView contactMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactMessage = itemView.findViewById(R.id.message);

            //When clicked on contact open the messenger activity with the contact
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Contact clicked...");
                }
            });
        }

        public void bindTo(Contact currentContact) {
            contactName.setText(currentContact.getUsername());
            contactMessage.setText(currentContact.getLastMessage().getMessage());
        }
    }
}
