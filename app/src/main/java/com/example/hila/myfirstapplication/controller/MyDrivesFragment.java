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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

@SuppressLint("ValidFragment")
public class MyDrivesFragment extends Fragment {

    IDataBase fb;
    Driver driver;
    public List<Drive> drives;
    public LinearLayout details;
    public TextView textDetails;
    public RecyclerView drivesRecyclerView;


    @SuppressLint("ValidFragment")
    MyDrivesFragment(Driver e) {
        this.driver = e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_drives, container, false);

        textDetails = v.findViewById(R.id.text_my_details);
        details = v.findViewById(R.id.linear_details_my_drive);
        details.setVisibility(View.GONE);

        getActivity().setTitle("My Drives");


        drivesRecyclerView = v.findViewById(R.id.my_drive_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fb = FactoryDataBase.getDataBase();

        drives = fb.getMyDrives(driver);
        drivesRecyclerView.setAdapter(new MyDrivesFragment.DrivesRecycleViewAdapter());

        return v;
    }


    @Override
    public void onDestroy() {
        Firebase_DBManager.stopNotifyToDriveList();
        super.onDestroy();
    }


    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<MyDrivesFragment.DrivesRecycleViewAdapter.DriveViewHolder>

    {
        @Override
        public MyDrivesFragment.DrivesRecycleViewAdapter.DriveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_drive, parent, false);
            return new MyDrivesFragment.DrivesRecycleViewAdapter.DriveViewHolder(v);
        }


        @Override
        public void onBindViewHolder(@NonNull MyDrivesFragment.DrivesRecycleViewAdapter.DriveViewHolder holder, int position) {
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
                        MenuItem addContact = menu.add(Menu.NONE, 1, 1, "add contact");
                        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Drive drive = drives.get(getAdapterPosition());
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
                                final Drive drive = drives.get(getAdapterPosition());
                                if (drive.getStatusOfRide().equals(DriveStatus.ENDING))
                                    Toast.makeText(getActivity(), "the drive already ending", Toast.LENGTH_LONG).show();
                                else
                                    fb.changeStatus(drive.getId(), driver, DriveStatus.ENDING, new IDataBase.Action() {
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
