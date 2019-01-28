package com.example.hila.myfirstapplication.controller;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * this class represent the fragment of available drive fragment
 * including recycle view adapter
 */
@SuppressLint("ValidFragment")
public class AvailableDrivesFragment extends Fragment {

    //the objects of the screen
    public RecyclerView drivesRecyclerView;
    public LinearLayout details;
    public List<Drive> drives = new ArrayList<>();//the drives list
    public TextView textDetails;
    IDataBase fb = FactoryDataBase.getDataBase();
    private DrivesRecycleViewAdapter adapter;
    CheckBox checkDistance;
    public boolean flag;//flag to switch between location and city
    View view;
    Driver driver;
    Location driverLocation;//the location of current driver

    /**
     * default constructor to set driver
     *
     * @param driver1 The current driver
     */
    @SuppressLint("ValidFragment")
    AvailableDrivesFragment(Driver driver1) {
        this.driver = driver1;
    }

    /***
     * this func create the fragment
     * @param inflater support for general purpose decompression
     * @param container the container we put the fragment
     * @param savedInstanceState argument to save state
     * @return the view of fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_available_drives, container, false);
        checkDistance = view.findViewById(R.id.distance_check);//set checkBox
        checkDistance.setOnClickListener(new View.OnClickListener() {//click listener
            @Override
            public void onClick(View v) {
                if (!checkDistance.isChecked()) {//not check
                    flag = false;//this flag for adapter to view and search by city
                    checkDistance.setText("search by distance");

                }
                if (checkDistance.isChecked()) {//check
                    flag = true;//this flag for adapter to view and search by distance
                    checkDistance.setText("search by city");

                }
                adapter = new DrivesRecycleViewAdapter(drives);//create adapter
                drivesRecyclerView.setAdapter(adapter);//set adapter
            }
        });
        textDetails = view.findViewById(R.id.text_details);//set text details
        details = view.findViewById(R.id.linear_details);//set layout details
        details.setVisibility(View.GONE);//cant see the layout until we press in menu
        getActivity().setTitle("Available Drives");//set title for fragment
        getLocation();//find the location of tne current driver
        new AsyncTask<Context, Void, Void>() {//we open asyncTask because to set the distance tak too much time

            @Override
            protected Void doInBackground(Context... contexts) {
                try {
                    fb.changeLocation(driverLocation, view.getContext());//change the distance between driver and client and set in entities
                    return null;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return null;
                }
            }
        }.execute();//start the asyncTask
        //set recycle view
        drivesRecyclerView = view.findViewById(R.id.my_list);
        drivesRecyclerView.setHasFixedSize(true);
        drivesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        drives = fb.getAvailableDrives();//set the list drive in available drives
        adapter = new DrivesRecycleViewAdapter(drives);//create new adapter
        drivesRecyclerView.setAdapter(adapter);
        setHasOptionsMenu(true);//to se the search view in fragment
        return view;//the view
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
     * this function calculate the location of current driver
     */
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
        location.setLatitude(31.785790);
        location.setLongitude(35.189450);
        driverLocation = location;

//
//                        }
//                    }
//                });
    }

    /***
     *
     * @param menu the search view
     * @param inflater support for general purpose decompression
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);//set on menu the menu search we create
        MenuItem searchItem = menu.findItem(R.id.action_search);//set on search view icon search
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
     * this class create recycle view adapter of available drives
     * doing filter
     */
    public class DrivesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
        //the objects of adapter
        public List<Drive> drives2;//the available drives
        public List<Drive> drivefull;//the filter drives

        /***
         * constructor
         * @param drives2 she available drives list
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drive, parent, false);//create a view holder☼
            viewHolder = new DriveViewHolder2(view);
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
            if (!flag) {//view holder by city
                DriveViewHolder2 vaultItemHolder1 = (DriveViewHolder2) holder;
                vaultItemHolder1.nameTextView.setText(drive.getName());//set name on item in adapter
                vaultItemHolder1.nameTextView.setTextSize(20);
                vaultItemHolder1.changeTextView.setText(drive.getStartAddress());//set start address on item in adapter
                vaultItemHolder1.changeTextView.setTextSize(16);
            } else {//view holder by distance
                DriveViewHolder2 vaultItemHolder2 = (DriveViewHolder2) holder;
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
                    if (!flag) {//we want filtering by city
                        for (Drive item : drivefull) {
                            String string = item.getStartAddress();//start address
                            String[] parts = string.split(", ");
                            String part2 = parts[1]; // city
                            if (part2.toLowerCase().contains(filterPattern)) {
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
             * this fuction update the adapter by the filter
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
        class DriveViewHolder2 extends RecyclerView.ViewHolder {
            TextView changeTextView;// location/city view
            TextView nameTextView;//name view


            public DriveViewHolder2(final View itemView) {
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
                        MenuItem addDrive = menu.add(Menu.NONE, 1, 1, "take drive");//take drive
                        addDrive.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {//click listener
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                final Drive drive = drives2.get(getAdapterPosition());//get the position drive
                                fb.changeStatus(drive, driver, DriveStatus.TREATMENT, new IDataBase.Action() {//change the status ond drive name of the drive
                                    @Override
                                    public void onSuccess() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());//set alert dialog
                                        builder.setMessage("The drive is in your care!")//title
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {//click listener
                                                        String mail = drive.getEmail();                                                        //send mail to the client that ask the drive
                                                        //send mail to the client that ask the drive
                                                        String[] mails = mail.split(",");
                                                        Intent in = new Intent(Intent.ACTION_SEND);
                                                        in.putExtra(Intent.EXTRA_EMAIL, mails);//send to
                                                        in.putExtra(Intent.EXTRA_SUBJECT, "get taxi");//subject
                                                        in.putExtra(Intent.EXTRA_TEXT, "taxi will coming to you in few minutes");//text
                                                        in.setType("message/rfc822");
                                                        startActivity(Intent.createChooser(in, "choose email"));
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();//show dialog
                                        drives2.remove(getAdapterPosition());//remove drive from list
                                        details.setVisibility(View.GONE);//gone the details
                                        drivesRecyclerView.getAdapter().notifyDataSetChanged();//update change
                                    }

                                    @Override
                                    public void onFailure(Exception exception) {//cannot take drive
                                        Toast.makeText(getActivity(), "הלקיחה נכשלה", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onProgress(String status, double percent) {//on progress
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
