package com.example.chad.activities;

import android.annotation.SuppressLint;
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

import com.example.chad.R;
import com.example.chad.models.Contact;


import java.util.ArrayList;

/**
 * Adapter class for contact listing and filtering
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> implements Filterable {
    private static final String TAG = ContactListAdapter.class.getName();

    private ArrayList<Contact> mContacts;
    private final ArrayList<Contact> mAllContacts;
    private final Context mContext;
    private int mLastPos = -1;

    ContactListAdapter(Context mContext, ArrayList<Contact> mContacts){
        this.mContacts = mContacts;
        this.mAllContacts = mContacts;
        this.mContext = mContext;
    }

    //Creating view holder for contact list
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.list_contacts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ViewHolder holder, int position) {
        Contact currentContact = this.mContacts.get(position);
        holder.bindTo(currentContact);

        if(holder.getBindingAdapterPosition() > mLastPos){
            Animation floatIn = AnimationUtils.loadAnimation(mContext, R.anim.float_in_conversation_anim);
            holder.itemView.startAnimation(floatIn);
            mLastPos = holder.getBindingAdapterPosition();
        }
    }

    @Override
    public int getItemCount() { return this.mContacts.size(); }

    @Override
    public Filter getFilter() { return contactFilter; }

    private final Filter contactFilter = new Filter(){

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<Contact> filteredContacts = new ArrayList<>();
            FilterResults res = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                res.count = mAllContacts.size();
                res.values = mAllContacts;
            }else{
                String filterString = charSequence.toString().toLowerCase().trim();
                for(Contact c: mAllContacts){
                    if(c.getName().toLowerCase().contains(filterString) || c.getUsername().toLowerCase().contains(filterString)){
                        filteredContacts.add(c);
                    }
                }
                res.count = filteredContacts.size();
                res.values = filteredContacts;
            }
            return res;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            try {
                mContacts = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }catch (Error e){
                Log.e(TAG, "publishResults: ", e);
            }

        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{

        private final String TAG = ViewHolder.class.getName();

        private TextView contactName;
        private TextView lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            lastMessage = itemView.findViewById(R.id.last_message);

            //When clicked on contact open the messenger activity with the contact
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i(TAG, "Contact clicked...");
                    Log.d(TAG, "onClick: Messenger activity started with the username: " + contactName.getText().toString());
                    Intent messenger = new Intent(mContext, MessageComposeActivity.class);
                    messenger.putExtra("username", contactName.getText().toString());
                    messenger.putExtra("SECRET_KEY", 42);
                    mContext.startActivity(messenger);
                }
            });
        }

        public void bindTo(Contact currentContact) {
            contactName.setText(currentContact.getUsername());
            lastMessage.setText("This is the Chadddenger! Under development...");
        }
    }
}
