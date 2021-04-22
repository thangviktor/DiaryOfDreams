package com.j.projectno0.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.j.projectno0.R;
import com.j.projectno0.data.Database;
import com.j.projectno0.data.Diary;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    Intent intent;
    final static boolean MODE_CREATE = true;
    final static boolean MODE_EDIT = false;

    EditText diaryTitle, diaryContent;
    TextView diaryDate;
    String date, title, content;
    boolean mode;
    Diary diary;

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.app_edit_name));

        diaryDate = findViewById(R.id.diaryDate);
        diaryTitle = findViewById(R.id.diaryTitle);
        diaryContent = findViewById(R.id.diaryContent);

        intent = getIntent();
        diary = (Diary) intent.getSerializableExtra("diary");
        if (diary == null) {
            mode = MODE_CREATE;
            date = dateFormat.format(calendar.getTime());
            diaryDate.setText(date);
        } else {
            mode = MODE_EDIT;
            diaryDate.setText(diary.getDate());
            diaryTitle.setText(diary.getTitle());
            diaryContent.setText(diary.getContent());
        }

        diaryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        diaryDate.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                Database database = new Database(this);

                date = diaryDate.getText().toString();
                title = diaryTitle.getText().toString();
                content = diaryContent.getText().toString();

                if (mode == MODE_CREATE) {
                    diary = new Diary(date, title, content);
                    database.addDiary(diary);
                } else {
                    diary.setTitle(title);
                    diary.setContent(content);
                    diary.setDate(date);
                    database.updateDiary(diary);
                }

                setResult(RESULT_OK);
                finish();
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}