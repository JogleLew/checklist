package com.Joglestudio.Checklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;
import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecretEditItemActivity extends Activity {

    private DataModel data = new DataModel();
    public static final String theme[] = {"深色主题","浅色主题"};
    int pos;
    private int themeNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edititemmain);

        changeTheme();

        TextView title = (TextView)findViewById(R.id.title6);
        title.setTextColor(0xFF27C7FF);

        Intent toEditIntent = getIntent();
        int i = toEditIntent.getIntExtra("position", -1);
        pos = i;
        String s;
        if (isFileExists("sediting" + i + ".dat"))
            s = readFileData("sediting" + i + ".dat");
        else {
            s = readFileData("s" + i + ".dat");
            writeFileData("sediting" + i + ".dat", s);
        }
        data = DataModel.getUnpackedData(s);
        init(i);

        Button back2Button = (Button) findViewById(R.id.backtomain2);
        back2Button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent backToMain2Intent = new Intent();
                backToMain2Intent.setClass(SecretEditItemActivity.this, SecretItemListActivity.class);
                deleteTheFile("sediting" + pos + ".dat");
                startActivity(backToMain2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretEditItemActivity.this.finish();

            }
        });

        Button sureEditButton = (Button) findViewById(R.id.sureeditgroup);
        sureEditButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent sureEditIntent = new Intent();
                sureEditIntent.setClass(SecretEditItemActivity.this, SecretItemListActivity.class);
                TextView itemName = (TextView) findViewById(R.id.edittext2);
                data.setItemName(itemName.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
                deleteFile("s" + pos + ".dat");
                renameFile("sediting" + pos + ".dat", "s"+pos + ".dat");
                startActivity(sureEditIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretEditItemActivity.this.finish();
            }
        });

        final Button chooseTime2Button = (Button) findViewById(R.id.choosetime2);
        chooseTime2Button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent chooseTime2Intent = new Intent();
                chooseTime2Intent.setClass(SecretEditItemActivity.this, SecretTimePickForEditActivity.class);
                TextView itemName = (TextView) findViewById(R.id.edittext2);
                data.setItemName(itemName.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
                chooseTime2Intent.putExtra("position", pos);
                startActivity(chooseTime2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretEditItemActivity.this.finish();
            }
        });

        Button chooseImage2Button = (Button) findViewById(R.id.chooseimage2);
        chooseImage2Button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent chooseImage2Intent = new Intent();
                chooseImage2Intent.setClass(SecretEditItemActivity.this, SecretImagePickForEditActivity.class);
                TextView itemName = (TextView) findViewById(R.id.edittext2);
                data.setItemName(itemName.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
                chooseImage2Intent.putExtra("position", pos);
                startActivity(chooseImage2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretEditItemActivity.this.finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                new AlertDialog.Builder(SecretEditItemActivity.this)
                        .setTitle("备忘录")
                        .setMessage("您确定要删除吗？")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent deleteItemIntent = new Intent();
                                deleteItemIntent.setClass(SecretEditItemActivity.this, SecretItemListActivity.class);

                                deleteFile("s"+pos + ".dat");
                                deleteFile("sediting" + pos + ".dat");
                                String numString = readFileData("scount.dat");
                                int num = Integer.parseInt(numString);
                                for (int i = pos + 1; i < num; i++)
                                    renameFile("s"+i + ".dat", "s"+(i - 1) + ".dat");
                                num--;
                                writeFileData("scount.dat", num + "");

                                startActivity(deleteItemIntent);
                                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                                SecretEditItemActivity.this.finish();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
    }

    private void init(int i){
        if (i == -1) {
            Intent backIntent = new Intent();
            backIntent.setClass(SecretEditItemActivity.this, SecretItemListActivity.class);
            startActivity(backIntent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            SecretEditItemActivity.this.finish();
            return;
        }
        final TextView editText = (TextView) findViewById(R.id.edittext2);
        editText.setText(data.getItemName());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                data.setItemName(editText.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
            }
        });

        TextView editTime = (TextView) findViewById(R.id.edittime2);
        if (data.getMinute() >= 10)
            editTime.setText(data.getYear() + "年" + data.getMonth() + "月" + data.getDay() + "日\n"
                    + data.getHour() + "点" + data.getMinute() + "分");
        else
            editTime.setText(data.getYear() + "年" + data.getMonth() + "月" + data.getDay() + "日\n"
                    + data.getHour() + "点0" + data.getMinute() + "分");
        ImageView image = (ImageView) findViewById(R.id.imagedisplay2);
        if (themeNum == 0) {
            switch (data.getImagePicked()) {
                case 1:
                    image.setImageResource(R.drawable.w1);
                    break;
                case 2:
                    image.setImageResource(R.drawable.w2);
                    break;
                case 3:
                    image.setImageResource(R.drawable.w3);
                    break;
                case 4:
                    image.setImageResource(R.drawable.w4);
                    break;
                case 5:
                    image.setImageResource(R.drawable.w5);
                    break;
                case 6:
                    image.setImageResource(R.drawable.w6);
                    break;
                case 7:
                    image.setImageResource(R.drawable.w7);
                    break;
                case 8:
                    image.setImageResource(R.drawable.w8);
                    break;
                case 9:
                    image.setImageResource(R.drawable.w9);
                    break;
                case 10:
                    image.setImageResource(R.drawable.w10);
                    break;
                default:
                    image.setImageResource(R.drawable.w1);
                    break;
            }
        }
        else if (themeNum == 1) {
            switch (data.getImagePicked()) {
                case 1:
                    image.setImageResource(R.drawable.w1);
                    break;
                case 2:
                    image.setImageResource(R.drawable.b2);
                    break;
                case 3:
                    image.setImageResource(R.drawable.b3);
                    break;
                case 4:
                    image.setImageResource(R.drawable.b4);
                    break;
                case 5:
                    image.setImageResource(R.drawable.b5);
                    break;
                case 6:
                    image.setImageResource(R.drawable.b6);
                    break;
                case 7:
                    image.setImageResource(R.drawable.b7);
                    break;
                case 8:
                    image.setImageResource(R.drawable.b8);
                    break;
                case 9:
                    image.setImageResource(R.drawable.b9);
                    break;
                case 10:
                    image.setImageResource(R.drawable.b10);
                    break;
                default:
                    image.setImageResource(R.drawable.w1);
                    break;
            }
        }
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

    private boolean isFileExists(String fileName){
        String s = this.getFilesDir().getPath()+"/"+fileName;
        File file = new File(s);
        return file.exists();
    }

    private void deleteTheFile(String fileName){
        String s = this.getFilesDir().getPath()+"/"+fileName;
        File file = new File(s);
        file.delete();
    }

    private void renameFile(String fileName, String newName){
        String s = this.getFilesDir().getPath()+"/"+fileName;
        String news = this.getFilesDir().getPath()+"/"+newName;
        File file = new File(s);
        File newfile = new File(news);
        file.renameTo(newfile);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent backToMain2Intent = new Intent();
                backToMain2Intent.setClass(SecretEditItemActivity.this, SecretItemListActivity.class);
                deleteTheFile("sediting" + pos + ".dat");
                startActivity(backToMain2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretEditItemActivity.this.finish();
            break;
            case KeyEvent.KEYCODE_MENU:
                showMenu();
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(){
        new AlertDialog.Builder(SecretEditItemActivity.this)
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
                        writeFileData("stheme.dat", i + "");
                        Intent themeIntent = new Intent();
                        themeIntent.setClass(SecretEditItemActivity.this, SecretEditItemActivity.class);
                        themeIntent.putExtra("position", pos);
                        startActivity(themeIntent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        SecretEditItemActivity.this.finish();
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

    private void changeTheme(){
        themeNum = Integer.parseInt(readFileData("stheme.dat"));
        if (themeNum == 1) {
            RelativeLayout titleBar1 = (RelativeLayout) findViewById(R.id.titlebar3);
            titleBar1.setBackgroundColor(0xFFFFFFFF);
            Button addButton = (Button) findViewById(R.id.sureeditgroup);
            addButton.setTextColor(0xFF27C7FF);
            Button backButton = (Button) findViewById(R.id.backtomain2);
            backButton.setTextColor(0xFF27C7FF);
            TextView title = (TextView) findViewById(R.id.title6);
            title.setTextColor(0xFF000000);
            TextView subtitle = (TextView) findViewById(R.id.subtitle6);
            subtitle.setTextColor(0xFF000000);
            LinearLayout addItemLayout = (LinearLayout) findViewById(R.id.edititemlayout);
            addItemLayout.setBackgroundColor(0xFFFFFFFF);
            TextView textName1 = (TextView) findViewById(R.id.textname3);
            textName1.setTextColor(0xFF000000);
            EditText editText = (EditText) findViewById(R.id.edittext2);
            editText.setTextColor(0xFF000000);
            TextView textName2 = (TextView) findViewById(R.id.textname4);
            textName2.setTextColor(0xFF000000);
            TextView editTime1 = (TextView) findViewById(R.id.edittime2);
            editTime1.setTextColor(0xFF000000);
            Button chooseTime = (Button) findViewById(R.id.choosetime2);
            chooseTime.setTextColor(0xFF27C7FF);
            chooseTime.setBackgroundColor(0x00FFFFFF);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setTextColor(0xFF000000);
            Button chooseImage = (Button) findViewById(R.id.chooseimage2);
            chooseImage.setTextColor(0xFF27C7FF);
            chooseImage.setBackgroundColor(0x00FFFFFF);
            Button delete = (Button) findViewById(R.id.delete);
            delete.setTextColor(0xFF27C7FF);
            delete.setBackgroundColor(0x00FFFFFF);
        }
    }
}
