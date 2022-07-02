package com.level_sense.app.roomDB.view_model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import com.level_sense.app.roomDB.Dao.GraphDataDao;
import com.level_sense.app.roomDB.database.GraphRoomDatabase;
import com.level_sense.app.roomDB.model.DeviceDataModelDB;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraphDataViewModel extends AndroidViewModel {
    //for Graph data
    private GraphDataDao graphDataDao;
    private GraphRoomDatabase graphDB;
    private List<DeviceDataModelDB> mAllDeviceData;
    //private LiveData<List<DeviceDataModelDB>> mAllDeviceData;

    public GraphDataViewModel(@NonNull Application application) {
        super(application);
        graphDB = GraphRoomDatabase.getDatabase(application);
        graphDataDao = graphDB.graphDataDao();
      //  mAllDeviceData = graphDataDao.getAllData();
    }

    public void insert(DeviceDataModelDB deviceDataModelDB) {
        new InsertAsyncTask(graphDataDao).execute(deviceDataModelDB);
    }

    public List<DeviceDataModelDB> getAllData() {
        try {
            return new GetDataAsyn().execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
   /* public LiveData<List<DeviceDataModelDB>> getAllData() {
        return mAllDeviceData;
    }*/

    public void update(DeviceDataModelDB deviceDataModelDB) {
        new UpdateAsyncTask(graphDataDao).execute(deviceDataModelDB);
    }

    public void delete(DeviceDataModelDB deviceDataModelDB) {
        new DeleteAsyncTask(graphDataDao).execute(deviceDataModelDB);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask().execute();
        // new DeleteAllAsyncTask(graphDataDao).execute(deviceDataModelDB);
    }

    public LiveData<DeviceDataModelDB> getData(String sensorId) {
        return graphDataDao.getData(sensorId);
    }


    public LiveData<DeviceDataModelDB> getDBtime(String db_time) {
        return graphDataDao.getDBtime(db_time);
    }


    private class GetDataAsyn extends AsyncTask<Void,Void,List<DeviceDataModelDB>>{


        @Override
        protected List<DeviceDataModelDB> doInBackground(Void... voids) {

            return graphDataDao.getAllData();
         //   return null;
        }
    }

    private class OperationsAsyncTask extends AsyncTask<DeviceDataModelDB, Void, Void> {
        GraphDataDao mAsyncTaskDao;

        OperationsAsyncTask(GraphDataDao dao) {
            this.mAsyncTaskDao = dao;
        }

        /* @Override
         protected Void doInBackground(GraphDataDao... notes) {
             return null;

         }*/
        @Override
        protected Void doInBackground(DeviceDataModelDB... deviceDataModelDBS) {
            return null;
        }
    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(GraphDataDao graphDataDao) {
            super(graphDataDao);
        }

        @Override
        protected Void doInBackground(DeviceDataModelDB... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(GraphDataDao graphDataDao) {
            super(graphDataDao);
        }

        @Override
        protected Void doInBackground(DeviceDataModelDB... dbs) {
            mAsyncTaskDao.update(dbs[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(GraphDataDao graphDataDao) {
            super(graphDataDao);
        }

        @Override
        protected Void doInBackground(DeviceDataModelDB... dbs) {
            mAsyncTaskDao.delete(dbs[0]);
            return null;
        }
    }

    private class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        // GraphDataDao mAsyncTaskDao;
        @Override
        protected Void doInBackground(Void... voids) {
            //mAsyncTaskDao.deleteAll();
            //mAsyncTaskDao.deleteAll();
            graphDataDao.deleteAll();
            return null;
        }
        /*//private WordDao mAsyncTaskDao;

         *//* DeleteAllAsyncTask(GraphDataDao graphDataDao) {
            super(graphDataDao);
        }*//*

        public DeleteAllAsyncTask(GraphDataDao graphDataDao) {
            super(graphDataDao);
        }
        @Override
        protected Void doInBackground(DeviceDataModelDB... dbs) {
            mAsyncTaskDao.deleteAll();
            return null;
        }*/
    }
}