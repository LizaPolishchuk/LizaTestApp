package com.example.android.lizatestapp.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.lizatestapp.R;
import com.example.android.lizatestapp.model.Contact;
import com.example.android.lizatestapp.view.callbacks.OnContactClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsVH> {

    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> sortedList = new ArrayList<>();
    private OnContactClickListener onContactClickListener;

    public ContactsAdapter(List<Contact> contactList, OnContactClickListener onContactClickListener) {

        this.onContactClickListener = onContactClickListener;
    }

    public void refreshItems(List<Contact> contacts) {
        contactList.clear();
        contactList.addAll(contacts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_contact, viewGroup, false);
        return new ContactsVH(view, onContactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsVH holder, int position) {
        holder.bind(contactList.get(position));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
