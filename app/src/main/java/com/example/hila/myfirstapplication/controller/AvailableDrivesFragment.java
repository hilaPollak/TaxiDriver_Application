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
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public List<Drive> drives;
    public TextView textDetails;
    public Button buttonChoose;
    IDataBase fb;
    Driver driver;
    String email;

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

        fb = FactoryDataBase.getDataBase();
        drives = fb.getAvailableDrives();
        drivesRecyclerView.setAdapter(new DrivesRecycleViewAdapter());
        drivesRecyclerView.getAdapter().notifyDataSetChanged();

        return v;
    }



    @Override
    public void onDestroy() {
        Firebase_DBManager.stopNotifyToDriveList();
        super.onDestroy();
    }




    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<DrivesRecycleViewAdapter.DriveViewHolder>

    {
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
                                        drivesRecyclerView.removeViewAt(getAdapterPosition());
                                        drivesRecyclerView.getAdapter().notifyItemRemoved(getAdapterPosition());
                                        drivesRecyclerView.getAdapter().notifyItemRangeChanged(getAdapterPosition(), drives.size());
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
