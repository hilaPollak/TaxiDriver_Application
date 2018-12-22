package com.example.hila.myfirstapplication.model.backend;

import com.example.hila.myfirstapplication.model.datasource.Firebase_DBManager;

/***
 * this class is the factory that choose the database to be by firebase
 */
public class FactoryDataBase {
    static IDataBase idb = null;
    public static IDataBase getDataBase()
    {
        if(idb==null)//the database is empty
            idb = new Firebase_DBManager();//create new database by firebase
        return  idb;
    }
}
