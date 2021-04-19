package com.j.projectno0.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.j.projectno0.data.Diary;
import com.j.projectno0.R;

import java.util.ArrayList;

public class DiaryAdapter extends BaseAdapter {
    private Boolean anim = true;
    private String searchedText = "";

    private final Context context;
    private final int layout;
    private final ArrayList<Diary> listDiary;

    public DiaryAdapter(Context context, int layout, ArrayList<Diary> listDiary) {
        this.context = context;
        this.layout = layout;
        this.listDiary = listDiary;
    }

    @Override
    public int getCount() {
        return listDiary.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void loadList(String searchedText) {
        this.searchedText = searchedText;
        anim = false;
        notifyDataSetChanged();
    }

    public void loadAnimation() {
        searchedText = "";
        anim = true;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView date, title, content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder = new ViewHolder();
            holder.date = convertView.findViewById(R.id.listdate);
            holder.title = convertView.findViewById(R.id.listtitle);
            holder.content = convertView.findViewById(R.id.listcontent);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        Diary diary = listDiary.get(position);
        holder.date.setText(diary.getDate());
        holder.title.setText(diary.getTitle());
        holder.content.setText(diary.getContent());

        if (!searchedText.isEmpty()) {
            if (diary.getContent().contains(searchedText)) {
                SpannableString string = getSearchedString(diary.getContent(), searchedText);
                holder.content.setText(string);
            }
            if (diary.getTitle().contains(searchedText)) {
                SpannableString string = getSearchedString(diary.getTitle(), searchedText);
                holder.title.setText(string);
            }
            if (diary.getDate().contains(searchedText)) {
                SpannableString string = getSearchedString(diary.getDate(), searchedText);
                holder.date.setText(string);
            }
        }

//        if (anim) {
//            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_view);
//            convertView.startAnimation(animation);
//        }

        return convertView;
    }

    private SpannableString getSearchedString(String text, String searchedText) {
        SpannableString string = new SpannableString(text);
        if (text.contains(searchedText)) {
            int start = text.indexOf(searchedText);
            string.setSpan(new ForegroundColorSpan(Color.RED), start, start + searchedText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return string;
    }
}
