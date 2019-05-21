package com.example.android.lizatestapp.view.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.lizatestapp.R;
import com.example.android.lizatestapp.view.callbacks.OnPhoneEmailActionsListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class AdapterContactPhonesEmails extends RecyclerView.Adapter<ContactPhonesEmailsVH> implements OnPhoneEmailActionsListener {

    private List<String> phonesEmailsList;
    private boolean editMode;
    private Context context;

    public AdapterContactPhonesEmails(Context context, List<String> phonesEmailsList, boolean editMode) {
        this.context = context;
        this.phonesEmailsList = phonesEmailsList;
        this.editMode = editMode;
    }

    public List<String> getPhonesEmailsList() {
        List<String> listWithoutEmpty = new ArrayList<>();
        for (int i = 0; i < phonesEmailsList.size(); i++) {
            if (!phonesEmailsList.get(i).trim().isEmpty())
                listWithoutEmpty.add(phonesEmailsList.get(i));
        }
        return listWithoutEmpty;
    }

    @NonNull
    @Override
    public ContactPhonesEmailsVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_phones_emails, viewGroup, false);
        return new ContactPhonesEmailsVH(view, editMode, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactPhonesEmailsVH holder, int position) {
        holder.bind(phonesEmailsList.get(position));
    }

    @Override
    public int getItemCount() {
        return phonesEmailsList.size();
    }

    @Override
    public void addPhoneEmail(int position) {
        if (phonesEmailsList.size() < 5) {
            phonesEmailsList.add("");
            notifyItemInserted(phonesEmailsList.size());
        } else {
            Toast.makeText(context, context.getString(R.string.you_cant_add_more_than_5), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updatePhoneEmail(int position, String text) {
        phonesEmailsList.set(position, text);
    }

    @Override
    public void removePhoneEmail(int position) {
        if (position != 0) {
            phonesEmailsList.remove(position);
            notifyItemRemoved(position);
        }
    }
}

class ContactPhonesEmailsVH extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.et_phone_email)
    EditText etPhoneEmail;
    @BindView(R.id.tv_phone_email)
    TextView tvPhoneEmail;
    @BindView(R.id.iv_remove_phone_email)
    ImageView ivRemovePhoneEmail;
    @BindView(R.id.iv_add_phone_email)
    ImageView ivAddPhoneEmail;

    private boolean editMode;
    private OnPhoneEmailActionsListener onPhoneEmailActionsListener;

    public ContactPhonesEmailsVH(@NonNull View itemView, boolean editMode, OnPhoneEmailActionsListener onPhoneEmailActionsListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.editMode = editMode;
        this.onPhoneEmailActionsListener = onPhoneEmailActionsListener;

        etPhoneEmail.setVisibility(editMode ? View.VISIBLE : View.GONE);
        ivRemovePhoneEmail.setVisibility(editMode ? View.VISIBLE : View.GONE);
        ivAddPhoneEmail.setVisibility(editMode ? View.VISIBLE : View.GONE);
        tvPhoneEmail.setVisibility(editMode ? View.GONE : View.VISIBLE);
        ivRemovePhoneEmail.setOnClickListener(this);
        ivAddPhoneEmail.setOnClickListener(this);
    }

    @OnTextChanged(R.id.et_phone_email)
    public void textChanged(CharSequence text) {
        onPhoneEmailActionsListener.updatePhoneEmail(getAdapterPosition(), text.toString());
    }

    void bind(String phoneOrEmail) {
        if (editMode) {
            etPhoneEmail.setText(phoneOrEmail);
        } else {
            tvPhoneEmail.setText(phoneOrEmail);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_remove_phone_email:
                onPhoneEmailActionsListener.removePhoneEmail(getAdapterPosition());
                break;
            case R.id.iv_add_phone_email:
                onPhoneEmailActionsListener.addPhoneEmail(getAdapterPosition());
                break;
        }
    }
}
