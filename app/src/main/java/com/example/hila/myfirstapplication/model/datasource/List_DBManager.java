package com.example.hila.myfirstapplication.model.datasource;
import com.example.hila.myfirstapplication.model.entities.Drive;
import com.example.hila.myfirstapplication.model.backend.DB_manager;

import java.util.ArrayList;
import java.util.List;

public class List_DBManager implements DB_manager {
    static List<Drive> drives= new ArrayList<>();
    @Override
    public void addDrive(Drive drive){
        drives.add(drive);
        return;
    }


}
