package jourmal.app.journalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import jourmal.app.journalapp.adapter.DiaryListAdapter;
import jourmal.app.journalapp.database.AppDataBase;
import jourmal.app.journalapp.database.Diary;

public class MainActivity extends AppCompatActivity implements DiaryListAdapter.OnDiaryClickListener{

    private RecyclerView homeList;
    private ProgressBar progress;
    private FloatingActionButton fab;
    private DiaryListAdapter adapter;
    private AppDataBase mDb;
    public static final String DIARY_ID_EXTRA = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDb = AppDataBase.getInstance(getApplicationContext());

        homeList = (RecyclerView)findViewById(R.id.home_list);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        homeList.setLayoutManager(llm);
        homeList.setHasFixedSize(true);

        adapter = new DiaryListAdapter(new ArrayList<Diary>(), this);
        homeList.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launch add Diary ativity
                Intent add = new Intent(MainActivity.this, AddActivity.class);
                startActivity(add);
            }
        });

        ///////////////////////////////////////////////////
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                new Task("delete").execute(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(homeList);

        ////////////////////////////////////////////////


        loadData();
    }

    @Override
    public void onDiaryClick(Diary diary, View view) {
        int id = view.getId();
        if(id == R.id.item){
            //launch detail activity...
        }else if(id == R.id.img_modify){
           //launch update activity (addActivity)
            Intent add = new Intent(MainActivity.this, AddActivity.class);
            add.putExtra(DIARY_ID_EXTRA, diary.getId());
            startActivity(add);
        }
    }

    class Task extends AsyncTask<Integer, String, String>{

        String action;

        public Task(String action){
            this.action = action;
        }

        @Override
        protected String doInBackground(Integer... ids) {
            if(action.equals("delete")){
                int id = ids[0];
                mDb.diaryDAO().delete(adapter.getDiaryList().get(id));
            }
            return null;
        }
    }

    public void loadData(){
        LiveData<List<Diary>> dTask = mDb.diaryDAO().selectAll();
        dTask.observe(this, new Observer<List<Diary>>(){

            @Override
            public void onChanged(@Nullable List<Diary> diaries) {
                if(diaries != null){
                    adapter.setDiaryList(diaries);
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            //dec0nnect the user here and close the activity
            progress.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            progress.setVisibility(View.GONE);
                            finish();
                        }


                    });

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

}
