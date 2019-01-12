package com.example.hila.myfirstapplication.controller;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hila.myfirstapplication.R;
import com.example.hila.myfirstapplication.model.datasource.Firebase_DBManager;
import com.example.hila.myfirstapplication.model.entities.Drive;

import java.util.ArrayList;
import java.util.List;

public class AvailableDrivesFragment extends Fragment {
    public RecyclerView drivesRecyclerView;
    public LinearLayout details;
    public List<Drive> drives;
    public TextView textDetails;
    public Button buttonChoose;
    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_available_drives, container, false);

        buttonChoose=v.findViewById(R.id.button_choose);
        textDetails=v.findViewById(R.id.text_details);
        details=v.findViewById(R.id.linear_details);
        details.setVisibility(View.GONE);

        drivesRecyclerView = v.findViewById(R.id.my_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        //List <Drive> availableDrives = new ArrayList<>();
       // availableDrives=Firebase_DBManager.getAvailableDrives();
        //if (drivesRecyclerView.getAdapter() == null) {
          //  drives = availableDrives;
          //  drivesRecyclerView.setAdapter(new DrivesRecycleViewAdapter());
      //  } else
         //   drivesRecyclerView.getAdapter().notifyDataSetChanged();


        Firebase_DBManager.notifyToDriveList(new Firebase_DBManager.NotifyDataChange<List<Drive>>() {

            @Override
            public void onDataChange(List<Drive> obj) {
                if (drivesRecyclerView.getAdapter() == null) {
                    //drives = Firebase_DBManager.getAvailableDrives();
                    drives=obj;
                    drivesRecyclerView.setAdapter(new DrivesRecycleViewAdapter());
                } else
                    drivesRecyclerView.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void onFailure(Exception exception) {
                Toast.makeText(getActivity(), "error to get drives list\n" + exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
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
           // textDetails.setText(drive.toString());

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
                    }
                });
            }
        }
    }
}
