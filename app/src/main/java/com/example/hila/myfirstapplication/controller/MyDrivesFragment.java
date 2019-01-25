package com.example.hila.myfirstapplication.controller;

import android.annotation.SuppressLint;
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

/**
 * this class represent the fragment of driver's drive fragment
 * including recycle view adapter
 */
@SuppressLint("ValidFragment")
public class MyDrivesFragment extends Fragment {

    //the objects of the screen
    IDataBase fb = FactoryDataBase.getDataBase();
    Driver driver;
    public List<Drive> drives = new ArrayList<>();//the drives list
    public LinearLayout details;
    public TextView textDetails;
    public RecyclerView drivesRecyclerView;
    private DrivesRecycleViewAdapter adapter;
    CheckBox checkDistance;
    public boolean flag;//flag to switch between location and time

    /**
     * default constructor to set driver
     *
     * @param driver1 The current driver
     */
    @SuppressLint("ValidFragment")
    MyDrivesFragment(Driver driver1) {
        this.driver = driver1;
    }

    /***
     * this func create the fragment
     * @param inflater support for general purpose decompression
     * @param container the container we put the fragment
     * @param savedInstanceState argument to save state
     * @return the view of fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_my_drives, container, false);
        checkDistance = v.findViewById(R.id.my_distance_check);//set checkBox
        checkDistance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//click listener
                if (!checkDistance.isChecked()) {//not check
                    flag = false;//this flag for adapter to view and search by time
                    checkDistance.setText("search by distance");

                }
                if (checkDistance.isChecked()) {//check
                    flag = true;//this flag for adapter to view and search by distance
                    checkDistance.setText("search by time");
                }
                adapter = new DrivesRecycleViewAdapter(drives);//create adapter
                drivesRecyclerView.setAdapter(adapter);//set adapter
            }


        });

        textDetails = v.findViewById(R.id.text_my_details);//set text details
        details = v.findViewById(R.id.linear_details_my_drive);//set layout details
        details.setVisibility(View.GONE);//cant see the layout until we press in menu
        getActivity().setTitle("My Drives");//set title for fragment
        //set recycle view
        drivesRecyclerView = v.findViewById(R.id.my_drive_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drives = fb.getMyDrives(driver);//set the list drive in my drives
        adapter = new DrivesRecycleViewAdapter(drives);
        drivesRecyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);//to se the search view in fragment

        return v;//the view
    }

    /***
     * this function stop listener to drive list when the fragment closes
     */
    @Override
    public void onDestroy() {
        Firebase_DBManager.stopNotifyToDriveList();//stop the listing to drive list
        super.onDestroy();//call super method
    }

    /***
     *
     * @param menu the search view
     * @param inflater support for general purpose decompression
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_time, menu);//set on menu the menu search we create
        MenuItem searchItem = menu.findItem(R.id.action_search_time);//set on search view icon search
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//what to do when we write on tool
            @Override
            public boolean onQueryTextSubmit(String query) {
                //do nothing
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter by how we determine
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    ///////////////////// DrivesRecycleViewAdapter ////////////////////////////////

    /***
     * this class create recycle view adapter of driver's drives
     * doing filter
     */
    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

        //the objects of adapter
        public List<Drive> drives2;//the my drives
        public List<Drive> drivefull;//the filter drives

        /***
         * constructor
         * @param drives2 my drives list
         */
        public DrivesRecycleViewAdapter(List<Drive> drives2) {
            this.drives2 = drives2;
            drivefull = new ArrayList<>(drives);

        }

        /***
         * this function create view holders
         * @param parent the context
         * @param viewType the type of view
         * @return
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            RecyclerView.ViewHolder viewHolder = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drive, parent, false);//create a view
            viewHolder = new MyDriveViewHolder1(view);
            return viewHolder;
        }

        /***
         * this class set the view in items of recycle view
         * @param holder the view holder
         * @param position the position of item in list
         */
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Drive drive = drives.get(position);
            if (!flag) {//view holder by time
                MyDriveViewHolder1 vaultItemHolder1 = (MyDriveViewHolder1) holder;
                vaultItemHolder1.nameTextView.setText(drive.getName());//set name on item in adapter
                vaultItemHolder1.nameTextView.setTextSize(20);
                vaultItemHolder1.changeTextView.setText(drive.getStartTime());//set time on item in adapter
                vaultItemHolder1.changeTextView.setTextSize(16);
            } else {//view holder by distance
                MyDriveViewHolder1 vaultItemHolder2 = (MyDriveViewHolder1) holder;
                vaultItemHolder2.nameTextView.setText(drive.getName());//set name on item in adapter
                vaultItemHolder2.nameTextView.setTextSize(20);
                vaultItemHolder2.changeTextView.setText(drive.getDistance());//set distance on item in adapter
                vaultItemHolder2.changeTextView.setTextSize(16);
            }


        }

        /***
         * this class return the num of count
         * @return size of drives
         */
        @Override
        public int getItemCount() {
            return drives2.size();
        }

        /***
         * this class return the filter to menu
         * @return filter
         */
        @Override
        public Filter getFilter() {
            return filter;
        }

        /***
         * this item represent how to filter the recycle view adapter
         */
        public Filter filter = new Filter() {

            /***
             * this function filter the available drive list
             * @param constraint the value the user type in search view
             * @return the filer list
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Drive> filteredList = new ArrayList<>();//new list that contained only filtered items

                if (constraint == null || constraint.length() == 0) {//empty in search
                    filteredList.addAll(drivefull);//get all items
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();//convert all to same pattern (caps lock)
                    if (!flag) {//we want filtering by time
                        for (Drive item : drivefull) {
                            String string = item.getStartTime();//time
                            if (string.toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    } else {
                        for (Drive item : drivefull) {//we want filtering by distance
                            String string = item.getDistance();//distance
                            if (string.toLowerCase().contains(filterPattern)) {
                                filteredList.add(item);
                            }
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;//the filtering list
                return results;
            }

            /***
             * this function update the adapter by the filter
             * @param constraint the type user
             * @param results the filter list
             */
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                drives2.clear();
                drives2.addAll((List) results.values);
                notifyDataSetChanged();//update change in recycle
            }
        };

        /***
         * this class create view holder and represent how to act on any item press
         */
        class MyDriveViewHolder1 extends RecyclerView.ViewHolder {
            TextView changeTextView;// location/time view
            TextView nameTextView;//name view


            public MyDriveViewHolder1(final View itemView) {
                super(itemView);
                //connect to view
                changeTextView = itemView.findViewById(R.id.phone_item_drive);
                nameTextView = itemView.findViewById(R.id.name_item_drive);
                itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {//create a menu by long parse

                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                        menu.setHeaderTitle("Select Action");//the title of menu
                        MenuItem detailm = menu.add(Menu.NONE, 1, 1, "view details");//view details
                        detailm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {// click listener
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Drive drive = drives2.get(getAdapterPosition());//get the position drive
                                textDetails.setText(drive.toString());//set drive's details in text box
                                details.setVisibility(View.VISIBLE);//se the tex box details
                                return true;
                            }
                        });
                        MenuItem addContact = menu.add(Menu.NONE, 1, 1, "add contact");//add contact
                        addContact.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {//click listener
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Drive drive = drives2.get(getAdapterPosition());//get the position drive
                                //save client in phone number by content provider
                                Intent intent = new Intent(Intent.ACTION_INSERT);//create new intent
                                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);//type of contact content
                                intent.putExtra(ContactsContract.Intents.Insert.NAME, drive.getName());//get name ref
                                intent.putExtra(ContactsContract.Intents.Insert.PHONE, drive.getPhoneNumber());//get phone ref
                                startActivity(intent);//start activity
                                return true;
                            }
                        });

                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "finish drive");//update finish drive
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {//click listener
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());//get the position drive
                                if (drive.getStatusOfRide().equals(DriveStatus.ENDING))//check if drive already finish
                                    Toast.makeText(getActivity(), "the drive already ending", Toast.LENGTH_LONG).show();
                                else
                                    fb.changeStatus(drive, driver, DriveStatus.ENDING, new IDataBase.Action() {//change status of drive to end
                                        @Override
                                        public void onSuccess() {//update drive success
                                            Toast.makeText(getActivity(), "הנסיעה הסתיימה", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailure(Exception exception) {//update drive fail
                                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onProgress(String status, double percent) {//in progress
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
