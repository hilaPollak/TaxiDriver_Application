package com.example.hila.myfirstapplication.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class AvailableDrivesFragment extends Fragment {
    public RecyclerView drivesRecyclerView;
    public LinearLayout details;
    public List<Drive> drives = new ArrayList<>();
    public TextView textDetails;
    IDataBase fb = FactoryDataBase.getDataBase();
    private DrivesRecycleViewAdapter adapter;
    CheckBox checkDistance;
    public boolean flag;
    View view;
    Driver driver;
    Location driverLocation;


    @SuppressLint("ValidFragment")
    AvailableDrivesFragment(Driver e) {
        this.driver = e;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_available_drives, container, false);
        checkDistance = view.findViewById(R.id.distance_check);
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
        textDetails = view.findViewById(R.id.text_details);
        details = view.findViewById(R.id.linear_details);
        details.setVisibility(View.GONE);

        getActivity().setTitle("Available Drives");
        getLocation();


        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... contexts) {
                try {

                    fb.changeLocation(driverLocation, view.getContext());

                    return null;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }
        }.execute();



        drivesRecyclerView = view.findViewById(R.id.my_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drives = fb.getAvailableDrives();
        adapter = new DrivesRecycleViewAdapter(drives);
        drivesRecyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);


        return view;
    }


    private void getLocation() {
//        // check the Permission and request permissions if needed
//        if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]
//                    {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//
//        //get Provider location from the user location services
//        FusedLocationProviderClient mFusedLocationDriver = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        //run the function on the background and add onSuccess listener
//        mFusedLocationDriver.getLastLocation()
//                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onSuccess(Location _location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (_location != null) {
//                            List<Address> addresses;
//                            //save the location
//                            driverLocation = _location;
//                        } else {
//                            Toast.makeText(view.getContext(), "can't find your location", Toast.LENGTH_LONG).show();
                            Location location = new Location("gps");
                            location.setLatitude(31.77);
                            location.setLongitude(35.177);
                            driverLocation = location;

//
//                        }
//                    }
//                });
    }


    @Override
    public void onDestroy() {
        Firebase_DBManager.stopNotifyToDriveList();
        super.onDestroy();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
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
                viewHolder = new DriveViewHolder1(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drive, parent, false);
                viewHolder = new DriveViewHolder2(view);
            }

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Drive drive = drives.get(position);
            if (!flag) {
                DriveViewHolder1 vaultItemHolder1 = (DriveViewHolder1) holder;
                vaultItemHolder1.nameTextView.setText(drive.getName());
                vaultItemHolder1.nameTextView.setTextSize(20);
                vaultItemHolder1.phoneTextView.setText(drive.getStartAddress());
                vaultItemHolder1.phoneTextView.setTextSize(16);
            } else {
                DriveViewHolder2 vaultItemHolder2 = (DriveViewHolder2) holder;
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
                            String string = item.getStartAddress();
                            String[] parts = string.split(", ");
                            String part2 = parts[1]; // city
                            if (part2.toLowerCase().contains(filterPattern)) {
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


        class DriveViewHolder1 extends RecyclerView.ViewHolder {
            TextView phoneTextView;
            TextView nameTextView;


            public DriveViewHolder1(final View itemView) {
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
                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "take drive");
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());

                                fb.changeStatus(drive, driver, DriveStatus.TREATMENT, new IDataBase.Action() {
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

                                        drives2.remove(getAdapterPosition());
                                        details.setVisibility(View.GONE);
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

        /////////////////////////////////////
        class DriveViewHolder2 extends RecyclerView.ViewHolder {
            TextView phoneTextView;
            TextView nameTextView;


            public DriveViewHolder2(final View itemView) {
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
                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "take drive");
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());

                                fb.changeStatus(drive, driver, DriveStatus.TREATMENT, new IDataBase.Action() {
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

                                        drives2.remove(getAdapterPosition());
                                        //   details.setVisibility(View.GONE);
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
