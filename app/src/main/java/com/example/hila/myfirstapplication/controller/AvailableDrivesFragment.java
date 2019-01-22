package com.example.hila.myfirstapplication.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
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
public class AvailableDrivesFragment extends Fragment {
    public RecyclerView drivesRecyclerView;
    public LinearLayout details;
    public List<Drive> drives = new ArrayList<>();
    public TextView textDetails;
    public Button buttonChoose;
    IDataBase fb = FactoryDataBase.getDataBase();
    private DrivesRecycleViewAdapter adapter;
    private Menu menu;

    Driver driver;

    @SuppressLint("ValidFragment")
    AvailableDrivesFragment(Driver e) {
        this.driver = e;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_available_drives, container, false);

        textDetails = v.findViewById(R.id.text_details);
        details = v.findViewById(R.id.linear_details);
        details.setVisibility(View.GONE);

        getActivity().setTitle("Available Drives");

        drivesRecyclerView = v.findViewById(R.id.my_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new DrivesRecycleViewAdapter();
        drives = fb.getAvailableDrives();
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
        //   menu.clear();
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();//here stop working
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    adapter.getFilter().filter(newText);
                } catch (Exception r) {
                    r.printStackTrace();
                }

                return false;
            }
        });
    }


    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<DrivesRecycleViewAdapter.DriveViewHolder> implements Filterable

    {
        public List<Drive> drives2;
        public List<Drive> drivefull;

        public DrivesRecycleViewAdapter() {
            this.drives2 = drives;
            drivefull = new ArrayList<>(drives);

        }


        @Override
        public DriveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_drive, parent, false);
            return new DriveViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DriveViewHolder holder, int position) {
            Drive drive = drives.get(position);
            holder.nameTextView.setText(drive.getName());
            holder.nameTextView.setTextSize(20);
            holder.phoneTextView.setText(drive.getStartAddress());
            holder.phoneTextView.setTextSize(16);

        }


        @Override
        public int getItemCount() {
            return drives.size();
        }

        @Override
        public Filter getFilter() {

            return filter;
        }

        public Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Drive> filteredList = new ArrayList<>();//new list that contained only filtered items
                if (constraint == null || constraint.length() == 0)//we what to show all the results becuse we don't what filtering
                {
                    filteredList.clear();
                    filteredList.addAll(drives);
                } else { //we what to filter the list
                    String filterPattern = constraint.toString().toLowerCase().trim(); // sting that takes the input
                    for (Drive item : drives) {
                        if (item.getName().toLowerCase().contains((filterPattern))) {
                            filteredList.add(item);
                            notifyDataSetChanged();

                        }
                    }

                }
                if (constraint == null || constraint.length() == 0)//we what to show all the results becuse we don't what filtering
                {
                    filteredList.clear();
                    filteredList.addAll(drives);
                    notifyDataSetChanged();
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }


            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                drives.clear();
                drives.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };


        class DriveViewHolder extends RecyclerView.ViewHolder {
            TextView phoneTextView;
            TextView nameTextView;


            public DriveViewHolder(final View itemView) {
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

                                Drive drive = drives.get(getAdapterPosition());
                                textDetails.setText(drive.toString());
                                details.setVisibility(View.VISIBLE);


                                return true;
                            }
                        });
                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "take drive");
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives.get(getAdapterPosition());

                                fb.changeStatus(drive.getId(), driver, DriveStatus.TREATMENT, new IDataBase.Action() {
                                    @Override
                                    public void onSuccess() {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("The drive is in your care!")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        String mail = drive.getEmail();
                                                        String[] mails = mail.split(",");
                                                        Intent in = new Intent(Intent.ACTION_SEND);
                                                        in.putExtra(Intent.EXTRA_EMAIL, mails);
                                                        in.putExtra(Intent.EXTRA_SUBJECT, "get taxi");
                                                        in.putExtra(Intent.EXTRA_TEXT, "taxi will coming to you in few minutes");
                                                        in.setType("message/rfc822");
                                                        startActivity(Intent.createChooser(in, "choose email"));
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();


                                        drives.remove(getAdapterPosition());


                                        drivesRecyclerView.getAdapter().notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(Exception exception) {
                                        Toast.makeText(getActivity(), "הלקיחה נכשלה", Toast.LENGTH_LONG).show();

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
