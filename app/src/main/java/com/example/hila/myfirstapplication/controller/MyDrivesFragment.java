package com.example.hila.myfirstapplication.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hila.myfirstapplication.R;
import com.example.hila.myfirstapplication.model.backend.FactoryDataBase;
import com.example.hila.myfirstapplication.model.backend.IDataBase;
import com.example.hila.myfirstapplication.model.datasource.Firebase_DBManager;
import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.entities.DriveStatus;
import com.example.hila.myfirstapplication.model.entities.Driver;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class MyDrivesFragment extends Fragment {

    IDataBase fb = FactoryDataBase.getDataBase();
    Driver driver;
    public List<Drive> drives = new ArrayList<>();
    public LinearLayout details;
    public TextView textDetails;
    public RecyclerView drivesRecyclerView;
    private DrivesRecycleViewAdapter adapter;

    CheckBox checkDistance;
    public boolean flag;


    @SuppressLint("ValidFragment")
    MyDrivesFragment(Driver e) {
        this.driver = e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_drives, container, false);

        checkDistance = v.findViewById(R.id.my_distance_check);
        checkDistance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!checkDistance.isChecked()) {
                    flag = false;
                }
                if (checkDistance.isChecked()) {
                    flag = true;
                }
                adapter = new DrivesRecycleViewAdapter(drives);
                drivesRecyclerView.setAdapter(adapter);
            }


        });

        textDetails = v.findViewById(R.id.text_my_details);
        details = v.findViewById(R.id.linear_details_my_drive);
        details.setVisibility(View.GONE);

        getActivity().setTitle("My Drives");


        drivesRecyclerView = v.findViewById(R.id.my_drive_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drives = fb.getMyDrives(driver);
        adapter = new DrivesRecycleViewAdapter(drives);
        drivesRecyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);


        return v;
    }


    @Override
    public void onDestroy() {
        Firebase_DBManager.stopNotifyToDriveList();
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_time, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_time);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable

    {
        public List<Drive> drives2;
        public List<Drive> drivefull;

        public DrivesRecycleViewAdapter(List<Drive> drives2) {
            this.drives2 = drives2;
            drivefull = new ArrayList<>(drives);

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = null;
            RecyclerView.ViewHolder viewHolder = null;

            if (!flag) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drive, parent, false);
                viewHolder = new MyDriveViewHolder1(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drive, parent, false);
                viewHolder = new MyDriveViewHolder2(view);
            }

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Drive drive = drives.get(position);
            if (!flag) {
                MyDriveViewHolder1 vaultItemHolder1 = (MyDriveViewHolder1) holder;
                vaultItemHolder1.nameTextView.setText(drive.getName());
                vaultItemHolder1.nameTextView.setTextSize(20);
                vaultItemHolder1.phoneTextView.setText(drive.getStartTime());
                vaultItemHolder1.phoneTextView.setTextSize(16);
            } else {
                MyDriveViewHolder2 vaultItemHolder2 = (MyDriveViewHolder2) holder;
                vaultItemHolder2.nameTextView.setText(drive.getName());
                vaultItemHolder2.nameTextView.setTextSize(20);
                vaultItemHolder2.phoneTextView.setText(drive.getDistance());
                vaultItemHolder2.phoneTextView.setTextSize(16);
            }


        }

        @Override
        public int getItemCount() {
            return drives2.size();
        }


        @Override
        public Filter getFilter() {

            return filter;
        }

        public Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Drive> filteredList = new ArrayList<>();//new list that contained only filtered items

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(drivefull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    if (!flag) {
                        for (Drive item : drivefull) {
                            String string = item.getStartTime();
                            if (string.toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    } else {
                        for (Drive item : drivefull) {
                            String string = item.getDistance();
                            if (string.toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;

            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                drives2.clear();
                drives2.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };


        class MyDriveViewHolder1 extends RecyclerView.ViewHolder {
            TextView phoneTextView;
            TextView nameTextView;


            public MyDriveViewHolder1(final View itemView) {
                super(itemView);
                phoneTextView = itemView.findViewById(R.id.phone_item_drive);
                nameTextView = itemView.findViewById(R.id.name_item_drive);


                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle("Select Action");
                        MenuItem detailm = menu.add(Menu.NONE, 1, 1, "view details");
                        detailm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Drive drive = drives2.get(getAdapterPosition());
                                textDetails.setText(drive.toString());
                                details.setVisibility(View.VISIBLE);


                                return true;
                            }
                        });
                        MenuItem addContact = menu.add(Menu.NONE, 1, 1, "add contact");
                        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Drive drive = drives2.get(getAdapterPosition());
                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                                intent.putExtra(ContactsContract.Intents.Insert.NAME, drive.getName());
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, drive.getPhoneNumber());


                                startActivity(intent);


                                return true;
                            }
                        });


                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "finish drive");
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());
                                if (drive.getStatusOfRide().equals(DriveStatus.ENDING))
                                    Toast.makeText(getActivity(), "the drive already ending", Toast.LENGTH_LONG).show();
                                else
                                    fb.changeStatus(drive, driver, DriveStatus.ENDING, new IDataBase.Action() {
                                        @Override
                                        public void onSuccess() {

                                            Toast.makeText(getActivity(), "הנסיעה הסתיימה", Toast.LENGTH_LONG).show();


                                        }

                                        @Override
                                        public void onFailure(Exception exception) {
                                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onProgress(String status, double percent) {
                                            ;
                                        }
                                    });


                                return true;
                            }
                        });
                    }
                });
            }
        }

        class MyDriveViewHolder2 extends RecyclerView.ViewHolder {
            TextView phoneTextView;
            TextView nameTextView;


            public MyDriveViewHolder2(final View itemView) {
                super(itemView);
                phoneTextView = itemView.findViewById(R.id.phone_item_drive);
                nameTextView = itemView.findViewById(R.id.name_item_drive);


                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle("Select Action");
                        MenuItem detailm = menu.add(Menu.NONE, 1, 1, "view details");
                        detailm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Drive drive = drives2.get(getAdapterPosition());
                                textDetails.setText(drive.toString());
                                details.setVisibility(View.VISIBLE);


                                return true;
                            }
                        });
                        MenuItem addContact = menu.add(Menu.NONE, 1, 1, "add contact");
                        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Drive drive = drives2.get(getAdapterPosition());
                                Intent intent = new Intent(Intent.ACTION_INSERT);
                                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

                                intent.putExtra(ContactsContract.Intents.Insert.NAME, drive.getName());
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, drive.getPhoneNumber());


                                startActivity(intent);


                                return true;
                            }
                        });


                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "finish drive");
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());
                                if (drive.getStatusOfRide().equals(DriveStatus.ENDING))
                                    Toast.makeText(getActivity(), "the drive already ending", Toast.LENGTH_LONG).show();
                                else
                                    fb.changeStatus(drive, driver, DriveStatus.ENDING, new IDataBase.Action() {
                                        @Override
                                        public void onSuccess() {

                                            Toast.makeText(getActivity(), "הנסיעה הסתיימה", Toast.LENGTH_LONG).show();


                                        }

                                        @Override
                                        public void onFailure(Exception exception) {
                                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onProgress(String status, double percent) {
                                            ;
                                        }
                                    });


                                return true;
                            }
                        });
                    }
                });
            }
        }
    }


}
