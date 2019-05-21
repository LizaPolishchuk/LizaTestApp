package com.example.android.lizatestapp.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.lizatestapp.R;
import com.example.android.lizatestapp.model.Contact;
import com.example.android.lizatestapp.view.adapters.AdapterContactPhonesEmails;
import com.example.android.lizatestapp.view.callbacks.OnContactActionsListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FragmentContactDetails extends Fragment implements View.OnClickListener, OnContactActionsListener {

    @BindView(R.id.iv_contact_avatar)
    ImageView ivContactAvatar;
    @BindView(R.id.tv_contact_name)
    TextView tvContactName;
    @BindView(R.id.rv_contact_emails)
    RecyclerView rvContactEmails;
    @BindView(R.id.rv_contact_phones)
    RecyclerView rvContactPhones;
    @BindView(R.id.tv_no_emails)
    TextView tvNoEmails;
    @BindView(R.id.tv_no_phones)
    TextView tvNoPhones;

    private Contact contact;
    private Unbinder unbinder;
    private OnContactActionsListener onContactActionsListener;
    private int position;

    public static FragmentContactDetails newInstance(Contact contact, int position, OnContactActionsListener onContactActionsListener) {
        FragmentContactDetails fragment = new FragmentContactDetails();
        fragment.contact = contact;
        fragment.position = position;
        fragment.onContactActionsListener = onContactActionsListener;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        List<String> emailsList = new ArrayList<>();
        List<String> phonesList = new ArrayList<>();
        if (contact != null) {
            tvContactName.setText(contact.getName());
            if (contact.getEmails() != null)
                emailsList.addAll(contact.getEmails());
            if (contact.getPhones() != null)
                phonesList.addAll(contact.getPhones());
            if (contact.getGender() != null) {
                if (contact.getGender().equals(getString(R.string.male)))
                    ivContactAvatar.setImageResource(R.drawable.placeholder_man);
                else if (contact.getGender().equals(getString(R.string.female)))
                    ivContactAvatar.setImageResource(R.drawable.placeholder_woman);
            }
            checkEmptyLists(emailsList, phonesList);
        }

        AdapterContactPhonesEmails adapterContactEmails = new AdapterContactPhonesEmails(getContext(), emailsList, false);
        AdapterContactPhonesEmails adapterContactPhones = new AdapterContactPhonesEmails(getContext(), phonesList, false);

        rvContactEmails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactEmails.setAdapter(adapterContactEmails);
        rvContactPhones.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactPhones.setAdapter(adapterContactPhones);
        return view;
    }

    private void checkEmptyLists(List<String> emailsList, List<String> phonesList) {
        rvContactEmails.setVisibility(emailsList.size() == 0 ? View.GONE : View.VISIBLE);
        tvNoEmails.setVisibility(emailsList.size() == 0 ? View.VISIBLE : View.GONE);

        rvContactPhones.setVisibility(phonesList.size() == 0 ? View.GONE : View.VISIBLE);
        tvNoPhones.setVisibility(phonesList.size() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((ContactsActivity) getActivity()).fabAction.setImageResource(R.drawable.ic_edit);
            ((ContactsActivity) getActivity()).fabAction.setOnClickListener(this);
        }
    }

    @Override
    public void addNewContact(Contact contact) {
        this.contact = contact;
        onContactActionsListener.editContact(contact, position);
    }

    @Override
    public void editContact(Contact contact, int position) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        if (getActivity() != null)
            ((ContactsActivity) getActivity()).replaceFragment(FragmentEditContact.newInstance(this, contact));
    }
}
