package com.example.android.lizatestapp.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.lizatestapp.R;
import com.example.android.lizatestapp.model.Contact;
import com.example.android.lizatestapp.utils.ExtraConstants;
import com.example.android.lizatestapp.view.adapters.ContactsAdapter;
import com.example.android.lizatestapp.view.callbacks.OnContactActionsListener;
import com.example.android.lizatestapp.view.callbacks.OnContactClickListener;
import com.example.android.lizatestapp.viewmodel.ContactsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FragmentContactList extends Fragment implements View.OnClickListener, OnContactClickListener, OnContactActionsListener {

    @BindView(R.id.rv_contacts_list)
    RecyclerView rvContactList;
    @BindView(R.id.vg_sort_mode)
    ViewGroup vgSortMode;
    @BindView(R.id.tv_sort_type)
    TextView tvSortType;

    private List<Contact> contactList = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private Unbinder unbinder;
    private ContactsViewModel contactsViewModel;
    private DatabaseReference databaseReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        contactsViewModel = ViewModelProviders.of(this).get(ContactsViewModel.class);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        contactsAdapter = new ContactsAdapter(contactList, this);
        rvContactList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContactList.setAdapter(contactsAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            ((ContactsActivity) getActivity()).fabAction.setImageResource(R.drawable.ic_add_white);
            ((ContactsActivity) getActivity()).fabAction.setOnClickListener(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        contactsViewModel.getContactListFromLocalDbLiveData().observe(getViewLifecycleOwner(), this::onGetContactsFromLocalDb);
        contactsViewModel.getContactListFromDbLiveData().observe(getViewLifecycleOwner(), this::onGetContactsFromDb);

        contactsViewModel.loadContactsListFromLocalDB();
    }

    private void onGetContactsFromLocalDb(List<Contact> contacts) {
        if (contacts.size() > 0) {
            contactList.clear();
            contactList.addAll(contacts);
            refreshAdapter(contacts);
            contactsViewModel.loadContactsListFromDB();
        } else {
            contactsViewModel.loadContactsListFromDB();
        }
    }

    private void onGetContactsFromDb(List<Contact> contacts) {
        contactList.clear();
        contactList.addAll(contacts);
        refreshAdapter(contacts);
    }

    private void refreshAdapter(List<Contact> contacts) {
        contactsAdapter.refreshItems(contacts);
    }

    private void enableSortMode(boolean enable, String sortType) {
        vgSortMode.setVisibility(enable ? View.VISIBLE : View.GONE);
        if (enable) tvSortType.setText(sortType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_sort_by_alpha:
                enableSortMode(true, item.getTitle().toString());
                Collections.sort(contactList, (contact1, contact2) -> contact1.getName().compareToIgnoreCase(contact2.getName()));
                refreshAdapter(contactList);
                break;
            case R.id.item_sort_reverse:
                enableSortMode(true, item.getTitle().toString());
                Collections.sort(contactList, (contact1, contact2) -> -contact1.getName().compareToIgnoreCase(contact2.getName()));
                refreshAdapter(contactList);
                break;
            case R.id.item_only_female:
                enableSortMode(true, item.getTitle().toString());
                sortByGender(false);
                break;
            case R.id.item_only_male:
                enableSortMode(true, item.getTitle().toString());
                sortByGender(true);
                break;
        }
        return true;
    }

    private void sortByGender(boolean onlyMale) {
        List<Contact> sortedList = new ArrayList<>();
        for (Contact contact : contactList) {
            if (contact.getGender() != null && contact.getGender().equals(onlyMale ? getString(R.string.male) : getString(R.string.female)))
                sortedList.add(contact);
        }
        contactsAdapter.refreshItems(sortedList);
    }

    @OnClick(R.id.tv_reset_sort)
    public void onClickReset() {
        enableSortMode(false, "");
        refreshAdapter(contactList);
    }

    public void onClick(View view) {
        if (getActivity() != null)
            ((ContactsActivity) getActivity()).replaceFragment(FragmentEditContact.newInstance(this, null));
    }

    @Override
    public void clickOnContact(Contact contact, int position) {
        if (getActivity() != null)
            ((ContactsActivity) getActivity()).replaceFragment(FragmentContactDetails.newInstance(contact, position, this));
    }

    @Override
    public void clickOnDelete(int position) {
        contactList.remove(position);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            databaseReference.child(ExtraConstants.CHILD_USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(contactList);
    }

    @Override
    public void addNewContact(Contact contact) {
        contactList.add(contact);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            databaseReference.child(ExtraConstants.CHILD_USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(contactList);
    }

    @Override
    public void editContact(Contact contact, int position) {
        contactList.set(position, contact);
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            databaseReference.child(ExtraConstants.CHILD_USERS).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(contactList);
    }

    @Override
    public void onStop() {
        super.onStop();
        contactsViewModel.getContactListFromLocalDbLiveData().removeObservers(getViewLifecycleOwner());
        contactsViewModel.getContactListFromDbLiveData().removeObservers(getViewLifecycleOwner());
        contactsViewModel.loadContactsListToLocalDB(contactList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
