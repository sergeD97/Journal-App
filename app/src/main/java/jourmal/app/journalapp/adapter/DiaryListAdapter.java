package jourmal.app.journalapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import jourmal.app.journalapp.R;
import jourmal.app.journalapp.database.Diary;

/**
 * Created by root on 29/06/18.
 */

public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.ViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());



    private List<Diary> diaryList;
    private OnDiaryClickListener diaryClickListener;

    public DiaryListAdapter(List<Diary> list, OnDiaryClickListener listener){
        this.diaryList = list;
        this.diaryClickListener = listener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.home_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        View item;
        ImageView modify;
        TextView title, datetv;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            modify = (ImageView)itemView.findViewById(R.id.img_modify);
            title = (TextView)itemView.findViewById(R.id.title);
            datetv = (TextView) itemView.findViewById(R.id.tv_date);
            item.setOnClickListener(this);
            modify.setOnClickListener(this);
        }

        public void bind(int position){
            Diary diary = diaryList.get(position);
            title.setText(diary.getTitle());
            datetv.setText(dateFormat.format(diary.getDate()));

        }

        @Override
        public void onClick(View v) {
            if(diaryClickListener != null){
                diaryClickListener.onDiaryClick(diaryList.get(getAdapterPosition()), v);
            }
        }
    }

    public List<Diary> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<Diary> diaryList) {
        this.diaryList = diaryList;
        notifyDataSetChanged();
    }

    public OnDiaryClickListener getDiaryClickListener() {
        return diaryClickListener;
    }

    public void setDiaryClickListener(OnDiaryClickListener diaryClickListener) {
        this.diaryClickListener = diaryClickListener;
    }

    public interface OnDiaryClickListener{
        public void onDiaryClick(Diary diary, View view);
    }
}
