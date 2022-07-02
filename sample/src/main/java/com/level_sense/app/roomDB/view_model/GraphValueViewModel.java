package com.level_sense.app.roomDB.view_model;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import android.os.AsyncTask;
import com.level_sense.app.roomDB.Dao.GraphValueDao;
import com.level_sense.app.roomDB.database.GraphRoomDatabase;
import com.level_sense.app.roomDB.model.GraphModelDB;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraphValueViewModel  extends AndroidViewModel {
    //for Graph value
    private GraphValueDao graphValueDao;
    private GraphRoomDatabase graphValDB;
    private LiveData<List<GraphModelDB>> mAllValue;

    public GraphValueViewModel(@NonNull Application application) {
        super(application);
        graphValDB = GraphRoomDatabase.getDatabase(application);
        graphValueDao = graphValDB.graphValueDao();
        mAllValue = graphValueDao.getAllValue();
    }

    public void insert(GraphModelDB graphModelDB) {
        new InsertAsyncTask(graphValueDao).execute(graphModelDB);
    }

    LiveData<List<GraphModelDB>> getAllValues() {
        return mAllValue;
    }

    public void update(GraphModelDB graphModelDB) {
        new UpdateAsyncTask(graphValueDao).execute(graphModelDB);
    }

    public void delete(GraphModelDB graphModelDB) {
        new DeleteAsyncTask(graphValueDao).execute(graphModelDB);
    }

    public List<GraphModelDB> getValueDB(String sensor_Id) {
        try {
            return new GetDataAsyn().execute(sensor_Id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        return null;
        }
    }

    /* public LiveData<GraphModelDB> getValueDB(String sensor_Id) {
        return graphValueDao.getValueDB(sensor_Id);
    }*/

    private class GetDataAsyn extends AsyncTask<String,String,List<GraphModelDB>>{

        @Override
        protected List<GraphModelDB> doInBackground(String... strings) {
          return graphValDB.graphValueDao().
                  getValueDB(strings[0]);
        //return null;
        }

    }

    private class OperationsAsyncTask extends AsyncTask<GraphModelDB, Void, Void> {

        GraphValueDao mAsyncTaskDao;

        OperationsAsyncTask(GraphValueDao dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(GraphModelDB... notes) {
            return null;
        }

    }

    private class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(GraphValueDao mGraphValueDao) {
            super(mGraphValueDao);
        }

        @Override
        protected Void doInBackground(GraphModelDB... graphModelDBS) {
            mAsyncTaskDao.insert(graphModelDBS[0]);
            return null;
        }
    }

    private class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(GraphValueDao graphValueDao) {
            super(graphValueDao);
        }

        @Override
        protected Void doInBackground(GraphModelDB... dbs) {
            mAsyncTaskDao.update(dbs[0]);
            return null;
        }
    }

    private class DeleteAsyncTask extends OperationsAsyncTask {

        public DeleteAsyncTask(GraphValueDao graphValueDao) {
            super(graphValueDao);
        }

        @Override
        protected Void doInBackground(GraphModelDB... dbs) {
            mAsyncTaskDao.delete(dbs[0]);
            return null;
        }
    }

}

