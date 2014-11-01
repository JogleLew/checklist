package com.Joglestudio.Checklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import org.apache.http.util.EncodingUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by jogle on 14/10/8.
 */
public class TimePickForAddActivity extends Activity {

    private int pos;
    DataModel dataModel = new DataModel();
    public static final String theme[] = {"深色主题","浅色主题"};
    private int themeNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        themeNum = Integer.parseInt(readFileData("theme.dat"));
        if (themeNum == 0)
            setContentView(R.layout.timepick);
        else if (themeNum == 1)
            setContentView(R.layout.timepick1);

        Intent intent = getIntent();
        int i = intent.getIntExtra("position", -1);
        pos = i;

        if (i == -1){
            Intent backToEdit2Intent = new Intent();
            backToEdit2Intent.setClass(TimePickForAddActivity.this, ItemListActivity.class);
            backToEdit2Intent.putExtra("position", pos);
            startActivity(backToEdit2Intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            TimePickForAddActivity.this.finish();
        }

        String s = readFileData("editing" + i + ".dat");
        dataModel = DataModel.getUnpackedData(s);

        DatePicker datePicker=(DatePicker)findViewById(R.id.datePicker);
        TimePicker timePicker=(TimePicker)findViewById(R.id.timePicker);

        int year, monthOfYear, dayOfMonth, hourOfDay, minute;
        Calendar calendar=Calendar.getInstance();
        if (dataModel.getYear() != 0) {
            year = dataModel.getYear();
            monthOfYear = dataModel.getMonth() - 1;
            dayOfMonth = dataModel.getDay();
            hourOfDay = dataModel.getHour();
            minute = dataModel.getMinute();
        }
        else{
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            dataModel.setYear(year);
            dataModel.setMonth(monthOfYear + 1);
            dataModel.setDay(dayOfMonth);
            dataModel.setHour(hourOfDay);
            dataModel.setMinute(minute);
        }

        TextView dateText = (TextView) findViewById(R.id.datepicked);
        TextView timeText = (TextView) findViewById(R.id.timepicked);
        dateText.setText(year + "年" + (monthOfYear + 1) +"月"+ dayOfMonth + "日");
        timeText.setText(hourOfDay + "点" + minute + "分");

        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                TextView datePicked=(TextView)findViewById(R.id.datepicked);
                datePicked.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
                dataModel.setYear(year);
                dataModel.setMonth(monthOfYear + 1);
                dataModel.setDay(dayOfMonth);
            }

        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                TextView timePicked=(TextView)findViewById(R.id.timepicked);
                if (minute >= 10)
                    timePicked.setText(hourOfDay+"点"+minute+"分");
                else
                    timePicked.setText(hourOfDay+"点0"+minute+"分");
                dataModel.setHour(hourOfDay);
                dataModel.setMinute(minute);
            }

        });

        Button backToAddButton = (Button) findViewById(R.id.backtoadd);
        backToAddButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent backToAdd2Intent = new Intent();
                backToAdd2Intent.setClass(TimePickForAddActivity.this, AddItemActivity.class);
                backToAdd2Intent.putExtra("position", pos);
                startActivity(backToAdd2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                TimePickForAddActivity.this.finish();
            }
        });

        Button sureChooseTimeButton = (Button) findViewById(R.id.surechoosetime);
        sureChooseTimeButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent sureChooseTimeIntent = new Intent();
                sureChooseTimeIntent.setClass(TimePickForAddActivity.this, AddItemActivity.class);
                sureChooseTimeIntent.putExtra("position", pos);
                String s = DataModel.getPackedString(dataModel);
                writeFileData("editing" + pos + ".dat", s);
                startActivity(sureChooseTimeIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                TimePickForAddActivity.this.finish();
                //Need something here
            }
        });
    }

    private void writeFileData(String fileName,String message){
        try{
            FileOutputStream fout = openFileOutput(fileName, Context.MODE_PRIVATE);
            byte [] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String readFileData(String fileName){
        String res="";
        try{
            FileInputStream fin = openFileInput(fileName);
            int length = fin.available();
            byte [] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent backToAdd2Intent = new Intent();
                backToAdd2Intent.setClass(TimePickForAddActivity.this, AddItemActivity.class);
                backToAdd2Intent.putExtra("position", pos);
                startActivity(backToAdd2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                TimePickForAddActivity.this.finish();
            break;
            case KeyEvent.KEYCODE_MENU:
                showMenu();
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(){
        new AlertDialog.Builder(TimePickForAddActivity.this)
                .setTitle("设定")
                .setItems(ItemListActivity.menu, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        if (i == 0){
                            showThemePicker();
                        }
                        else if (i == 1)
                            showInfo();
                    }
                })
                .show();
    }

    private void showThemePicker(){
        new AlertDialog.Builder(this)
                .setTitle("选择主题")
                .setItems(theme, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {
                        writeFileData("theme.dat", i + "");
                        Intent themeIntent = new Intent();
                        themeIntent.setClass(TimePickForAddActivity.this, TimePickForAddActivity.class);
                        themeIntent.putExtra("position", pos);
                        startActivity(themeIntent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        TimePickForAddActivity.this.finish();
                    }
                })
                .show();
    }

    public void showInfo(){
        new AlertDialog.Builder(this)
                .setTitle("备忘录")
                .setMessage("作者：吕佳高\n\n" +
                        "简介：一款实用的备忘录软件，方便记录自己需要做的事情和勾选已经完成的事件。\n\n" +
                        "使用说明：短按以勾选或取消勾选，长按以编辑和删除项目\n\n" +
                        "版本：1.0")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }
}
