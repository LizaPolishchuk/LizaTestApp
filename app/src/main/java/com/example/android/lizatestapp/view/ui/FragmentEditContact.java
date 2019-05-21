package com.example.android.lizatestapp.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class FragmentEditContact extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.iv_contact_avatar)
    ImageView ivContactAvatar;
    @BindView(R.id.et_contact_name)
    EditText etContactName;
    @BindView(R.id.rg_gender)
    RadioGroup rgContactGender;
    @BindView(R.id.rv_contact_emails)
    RecyclerView rvContactEmails;
    @BindView(R.id.rv_contact_phones)
    RecyclerView rvContactPhones;

    private Unbinder unbinder;
    private Contact contact;
    private OnContactActionsListener onContactActionsListener;
    private AdapterContactPhonesEmails adapterContactPhones;
    private AdapterContactPhonesEmails adapterContactEmails;
    private List<String> emailsList = new ArrayList<>();
    private List<String> phonesList = new ArrayList<>();

    public static FragmentEditContact newInstance(OnContactActionsListener onContactActionsListener, Contact contact) {
        FragmentEditContact fragment = new FragmentEditContact();
        fragment.contact = contact;
        fragment.onContactActionsListener = onContactActionsListener;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);
        unbinder = ButterKnife.bind(this, view);
        rgContactGender.setOnCheckedChangeListener(this);

        if (contact != null) {
            if (contact.getName() != null) etContactName.setText(contact.getName());
            if (contact.getEmails() != null) emailsList.addAll(contact.getEmails());
            if (contact.getPhones() != null) phonesList.addAll(contact.getPhones());
            if (contact.getGender() != null) {
                if (contact.getGender().equals(getString(R.string.male)))
                    rgContactGender.check(R.id.rb_male);
                else if (contact.getGender().equals(getString(R.string.female)))
                    rgContactGender.check(R.id.rb_female);
            }
        }

        if (emailsList.size() == 0) emailsList.add("");
        if (phonesList.size() == 0) phonesList.add("");

        adapterContactEmails = new AdapterContactPhonesEmails(getContext(), emailsList, true);
        adapterContactPhones = new AdapterContactPhonesEmails(getContext(), phonesList, true);

        rvContactEmails.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactEmails.setAdapter(adapterContactEmails);
        rvContactPhones.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactPhones.setAdapter(adapterContactPhones);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((ContactsActivity) getActivity()).fabAction.setOnClickListener(this);
            ((ContactsActivity) getActivity()).fabAction.setImageResource(R.drawable.ic_done);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View view) {
        String name = etContactName.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
        } else {
            String gender;
            switch (rgContactGender.getCheckedRadioButtonId()) {
                case R.id.rb_male:
                    gender = getString(R.string.male);
                    break;
                case R.id.rb_female:
                    gender = getString(R.string.female);
                    break;
                default:
                    gender = "";
            }

            onContactActionsListener.addNewContact(new Contact(name, adapterContactEmails.getPhonesEmailsList(), adapterContactPhones.getPhonesEmailsList(), gender));
            if (getActivity() != null)
                getActivity().onBackPressed();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {
            case R.id.rb_male:
                ivContactAvatar.setImageResource(R.drawable.placeholder_man);
                break;
            case R.id.rb_female:
                ivContactAvatar.setImageResource(R.drawable.placeholder_woman);
                break;
        }
    }
}
