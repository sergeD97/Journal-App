package jourmal.app.journalapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.AsyncTask;

import java.util.Date;

/**
 * Created by root on 29/06/18.
 */

@Entity(tableName = "diary")
public class Diary {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private Date date;

    @Ignore
    public Diary(){
    }

    @Ignore
    public Diary(String title, String content, Date date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Diary(int id, String title, String content, Date date){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /////


    ////
}
