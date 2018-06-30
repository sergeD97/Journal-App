package jourmal.app.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import jourmal.app.journalapp.database.AppDataBase;
import jourmal.app.journalapp.database.Diary;

public class AddActivity extends AppCompatActivity {
    public static final int DEFAULT_ID = -1;

    EditText title, content;
    Button save;
    AppDataBase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        save = (Button)findViewById(R.id.save);
        mDb = AppDataBase.getInstance(getApplicationContext());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().isEmpty()){
                    Toast.makeText(AddActivity.this, "title cannot be empty..", Toast.LENGTH_LONG).show();
                }else{
                    new Task("add").execute();
                }
            }
        });
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        load();
    }

    class Task extends AsyncTask<Integer, String, String> {

        String action;

        public Task(String action){
            this.action = action;
        }

        @Override
        protected String doInBackground(Integer... ids) {
            int id = getIntent().getIntExtra(MainActivity.DIARY_ID_EXTRA, DEFAULT_ID);
            Diary d = new Diary(title.getText().toString(), content.getText().toString(), new Date());

            if(id == DEFAULT_ID){
                mDb.diaryDAO().insert(d);
            }else{
                d.setId(id);
                mDb.diaryDAO().update(d);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(AddActivity.this, "success.", Toast.LENGTH_LONG).show();
            NavUtils.navigateUpFromSameTask(AddActivity.this);
        }
    }

    public void load(){
        int id = getIntent().getIntExtra(MainActivity.DIARY_ID_EXTRA, DEFAULT_ID);
        if(id != DEFAULT_ID){
            LiveData<Diary> dtask = mDb.diaryDAO().selectById(id);
            dtask.observe(this, new Observer<Diary>() {
                @Override
                public void onChanged(@Nullable Diary diary) {
                    if(diary != null){
                        title.setText(diary.getTitle());
                        content.setText(diary.getContent());
                    }
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
