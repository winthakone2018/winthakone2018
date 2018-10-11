package com.example.fsociety.datepickerdialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.fsociety.datepickerdialog.models.JobInWeek;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    TextView txtDate,txtTime;
    EditText editCv,editNd;
    Button btnDate,btnTime,btnAdd;

    ArrayList<JobInWeek>arrJob=new ArrayList<JobInWeek>();

    ArrayAdapter<JobInWeek>adapter=null;
    ListView lvCv;
    Calendar cal;
    Date dateFinish;
    Date hourFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getDefaultInfor();
        event();
    }

    public void init()
    {
        txtDate=(TextView) findViewById(R.id.txtdate);
        txtTime=(TextView) findViewById(R.id.txttime);
        editCv=(EditText) findViewById(R.id.editcongviec);
        editNd=(EditText) findViewById(R.id.editnoidung);
        btnDate=(Button) findViewById(R.id.btndate);
        btnTime=(Button) findViewById(R.id.btntime);
        btnAdd=(Button) findViewById(R.id.btncongviec);
        lvCv=(ListView) findViewById(R.id.lvcongviec);

        adapter=new ArrayAdapter<JobInWeek>(this, android.R.layout.simple_list_item_1, arrJob);

        lvCv.setAdapter(adapter);
    }

    public void event()
    {
        btnDate.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        lvCv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                arrJob.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        lvCv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, arrJob.get(position).getDesciption(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btndate:
                chooseDate();
                break;
            case R.id.btntime:
                chooseTime();
                break;
            case R.id.btncongviec:
                addJob();
                break;
        }
    }

    public void getDefaultInfor()
    {

        cal=Calendar.getInstance();
        SimpleDateFormat dft=null;

        dft=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        String strDate=dft.format(cal.getTime());

        txtDate.setText(strDate);

        dft=new SimpleDateFormat("hh:mm a",Locale.getDefault());
        String strTime=dft.format(cal.getTime());

        txtTime.setText(strTime);

        dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
        txtTime.setTag(dft.format(cal.getTime()));

        editCv.requestFocus();

        dateFinish=cal.getTime();
        hourFinish=cal.getTime();
    }


    public void chooseDate()
    {
        OnDateSetListener callback = new OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {

                txtDate.setText((dayOfMonth) +"/"+(monthOfYear+1)+"/"+year);
                cal.set(year, monthOfYear, dayOfMonth);
                dateFinish=cal.getTime();
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                MainActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                callback,
                year,month,day);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void chooseTime()
    {
        OnTimeSetListener callback=new OnTimeSetListener() {
            public void onTimeSet(TimePicker view,
                                  int hourOfDay, int minute) {

                String s=hourOfDay +":"+minute;
                int hour=hourOfDay;
                if(hour > 12) hour -= 12;
                txtTime.setText (hour +":"+minute +(hourOfDay > 12 ? " PM" : " AM"));
                txtTime.setTag(s);
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                hourFinish=cal.getTime();
            }
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(
                MainActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                callback,
                hour, minute, true);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }


    public void addJob()
    {
        String title=editCv.getText().toString();
        String description=editNd.getText().toString();

        if(title.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Ten cong viec khong the trong", Toast.LENGTH_SHORT).show();
            editCv.requestFocus();
        }
        else if(description.length() == 0)
        {

            Toast.makeText(getApplicationContext(), "Ten cong viec khong the trong", Toast.LENGTH_SHORT).show();
            editNd.requestFocus();
        }
        else
        {
            JobInWeek job=new JobInWeek(title, description, dateFinish, hourFinish);
            arrJob.add(job);
            adapter.notifyDataSetChanged();
            //sau khi cập nhật thì reset dữ liệu và cho focus tới editCV
            editCv.setText("");
            editNd.setText("");
            editCv.requestFocus();
        }
    }
}
