package com.j.projectno0.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.j.projectno0.R;
import com.j.projectno0.data.Diary;

import java.util.ArrayList;

public class DiaryAdapter extends BaseAdapter {
//    private Boolean anim = true;
    private String searchedText = "";
    private Boolean ellipSizeStart = false;

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
//        anim = false;
        notifyDataSetChanged();
    }

//    public void loadAnimation() {
//        searchedText = "";
//        anim = true;
//        notifyDataSetChanged();
//    }

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
                if (ellipSizeStart) {
                    holder.content.setEllipsize(TextUtils.TruncateAt.START);
                    ellipSizeStart = false;
                }

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
        int index = text.indexOf(searchedText);

        // check if index of searchedText great than length of displayed text
        if (index > 40 || text.lastIndexOf(searchedText) - index > 40) {
            if (text.length() - index < 40)
                ellipSizeStart = true;
            else text = "..." + text.substring(index);
            index = text.indexOf(searchedText);
        }

        SpannableString string = new SpannableString(text);
        while (index >= 0) {
            string.setSpan(new ForegroundColorSpan(Color.RED), index, index + searchedText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            index = text.indexOf(searchedText, index + searchedText.length());
        }
        return string;
    }
}
