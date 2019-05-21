package com.example.android.lizatestapp.view.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.lizatestapp.R;
import com.example.android.lizatestapp.model.Contact;
import com.example.android.lizatestapp.view.callbacks.OnContactClickListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.iv_contact_avatar)
    ImageView ivContactAvatar;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;

    private OnContactClickListener onContactClickListener;
    private Contact contact;
    private Context context;

    public ContactsVH(@NonNull View itemView, OnContactClickListener onContactClickListener) {
        super(itemView);
        context = itemView.getContext();
        this.onContactClickListener = onContactClickListener;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    void bind(Contact contact) {
        this.contact = contact;
        if (contact.getGender() != null) {
            if (contact.getGender().equals(context.getString(R.string.male)))
                ivContactAvatar.setImageResource(R.drawable.placeholder_man);
            else if (contact.getGender().equals(context.getString(R.string.female)))
                ivContactAvatar.setImageResource(R.drawable.placeholder_woman);
        }
        tvContactName.setText(contact.getName());
    }

    @OnClick(R.id.iv_delete_contact)
    public void onClickDelete() {
        onContactClickListener.clickOnDelete(getAdapterPosition());
    }

    @Override
    public void onClick(View view) {
        onContactClickListener.clickOnContact(contact, getAdapterPosition());
    }
}
