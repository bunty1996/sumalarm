package com.level_sense.app.roomDB.Dao;

import androidx.room.Insert;
import androidx.room.Query;

public interface GraphDataValueJoinDao {
    @Insert
    void insert(GraphDataValueJoinDao graphDataValueJoinDao);

  /*  @Query("SELECT * FROM graph_data_table INNER JOIN user_repo_join ON
            user.id=user_repo_join.userId WHERE
            user_repo_join.repoId=:repoId")
            List<User> getUsersForRepository(final int repoId);*/

  //@Query("Select * from graph_data_table INNER JOIN ")

}
