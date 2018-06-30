package jourmal.app.journalapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by root on 29/06/18.
 */

@Dao
public interface DiaryDAO {

    @Query("SELECT * FROM diary ORDER BY date DESC")
    LiveData<List<Diary>> selectAll();

    @Insert
    void insert(Diary diary);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Diary diary);

    @Delete
    void delete(Diary diary);

    @Query("SELECT * FROM diary WHERE id = :id")
    LiveData<Diary> selectById(int id);
}
