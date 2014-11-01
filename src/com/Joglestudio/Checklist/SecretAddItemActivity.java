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

/**
 * Created by jogle on 14/10/7.
 */
public class SecretAddItemActivity extends Activity {

    private DataModel data = new DataModel();
    public static final String theme[] = {"深色主题","浅色主题"};
    private int pos;
    private int themeNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.additemmain);

        changeTheme();

        TextView title = (TextView)findViewById(R.id.title2);
        title.setTextColor(0xFF27C7FF);

        Intent toEditIntent = getIntent();
        int i = toEditIntent.getIntExtra("position", -1);
        pos = i;

        String s;
        if (!isFileExists("sediting" + i + ".dat"))
            writeFileData("sediting" + i + ".dat", "\n0 0 0 0 0\n1\nfalse");
        s = readFileData("sediting" + i + ".dat");

        data = DataModel.getUnpackedData(s);
        final TextView editText = (TextView) findViewById(R.id.edittext1);
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
        TextView editTime = (TextView) findViewById(R.id.edittime1);
        if (data.getYear() != 0 && data.getMinute() >= 10)
            editTime.setText(data.getYear() + "年" + data.getMonth() + "月" + data.getDay() + "日\n"
                + data.getHour() + "点" + data.getMinute() + "分");
        if (data.getYear() != 0 && data.getMinute() < 10)
            editTime.setText(data.getYear() + "年" + data.getMonth() + "月" + data.getDay() + "日\n"
                    + data.getHour() + "点0" + data.getMinute() + "分");

        themeNum = Integer.parseInt(readFileData("stheme.dat"));
        ImageView image = (ImageView) findViewById(R.id.imagedisplay);
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
        else if (themeNum == 1){
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
        Button backButton = (Button) findViewById(R.id.backtomain);
        backButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent backToMainIntent = new Intent();
                backToMainIntent.setClass(SecretAddItemActivity.this, SecretItemListActivity.class);
                deleteTheFile("sediting" + pos + ".dat");
                startActivity(backToMainIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretAddItemActivity.this.finish();
            }
        });

        Button sureButton = (Button) findViewById(R.id.sureaddgroup);
        sureButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                if (data.getYear() == 0){
                    new AlertDialog.Builder(SecretAddItemActivity.this)
                            .setTitle("备忘录")
                            .setMessage("请设置时间。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
                else {
                    Intent sureIntent = new Intent();
                    sureIntent.setClass(SecretAddItemActivity.this, SecretItemListActivity.class);
                    TextView itemName = (TextView) findViewById(R.id.edittext1);
                    data.setItemName(itemName.getText().toString());
                    String s = DataModel.getPackedString(data);
                    writeFileData("sediting" + pos + ".dat", s);
                    deleteFile("s" + pos + ".dat");
                    renameFile("sediting" + pos + ".dat", "s" + pos + ".dat");
                    writeFileData("scount.dat", (pos + 1) + "");
                    startActivity(sureIntent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    SecretAddItemActivity.this.finish();
                }
            }
        });

        Button chooseTimeButton = (Button) findViewById(R.id.choosetime);
        chooseTimeButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent chooseTimeIntent = new Intent();
                chooseTimeIntent.setClass(SecretAddItemActivity.this, SecretTimePickForAddActivity.class);

                TextView itemName = (TextView) findViewById(R.id.edittext1);
                data.setItemName(itemName.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
                chooseTimeIntent.putExtra("position", pos);

                startActivity(chooseTimeIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretAddItemActivity.this.finish();
            }
        });

        Button chooseImageButton = (Button) findViewById(R.id.chooseimage);
        chooseImageButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent chooseImageIntent = new Intent();
                chooseImageIntent.setClass(SecretAddItemActivity.this, SecretImagePickForAddActivity.class);

                TextView itemName = (TextView) findViewById(R.id.edittext1);
                data.setItemName(itemName.getText().toString());
                String s = DataModel.getPackedString(data);
                writeFileData("sediting" + pos + ".dat", s);
                chooseImageIntent.putExtra("position", pos);

                startActivity(chooseImageIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretAddItemActivity.this.finish();
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
                Intent backToMainIntent = new Intent();
                backToMainIntent.setClass(SecretAddItemActivity.this, SecretItemListActivity.class);
                deleteTheFile("sediting" + pos + ".dat");
                startActivity(backToMainIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretAddItemActivity.this.finish();
            break;

            case KeyEvent.KEYCODE_MENU:
                showMenu();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(){
        new AlertDialog.Builder(SecretAddItemActivity.this)
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
                        themeIntent.setClass(SecretAddItemActivity.this, SecretAddItemActivity.class);
                        themeIntent.putExtra("position", pos);
                        startActivity(themeIntent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        SecretAddItemActivity.this.finish();
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
            RelativeLayout titleBar1 = (RelativeLayout) findViewById(R.id.titlebar2);
            titleBar1.setBackgroundColor(0xFFFFFFFF);
            Button addButton = (Button) findViewById(R.id.sureaddgroup);
            addButton.setTextColor(0xFF27C7FF);
            Button backButton = (Button) findViewById(R.id.backtomain);
            backButton.setTextColor(0xFF27C7FF);
            TextView title = (TextView) findViewById(R.id.title2);
            title.setTextColor(0xFF000000);
            TextView subtitle = (TextView) findViewById(R.id.subtitle2);
            subtitle.setTextColor(0xFF000000);
            LinearLayout addItemLayout = (LinearLayout) findViewById(R.id.additemlayout);
            addItemLayout.setBackgroundColor(0xFFFFFFFF);
            TextView textName1 = (TextView) findViewById(R.id.textname1);
            textName1.setTextColor(0xFF000000);
            EditText editText = (EditText) findViewById(R.id.edittext1);
            editText.setTextColor(0xFF000000);
            TextView textName2 = (TextView) findViewById(R.id.textname2);
            textName2.setTextColor(0xFF000000);
            TextView editTime1 = (TextView) findViewById(R.id.edittime1);
            editTime1.setTextColor(0xFF000000);
            Button chooseTime = (Button) findViewById(R.id.choosetime);
            chooseTime.setTextColor(0xFF27C7FF);
            chooseTime.setBackgroundColor(0x00FFFFFF);
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setTextColor(0xFF000000);
            Button chooseImage = (Button) findViewById(R.id.chooseimage);
            chooseImage.setTextColor(0xFF27C7FF);
            chooseImage.setBackgroundColor(0x00FFFFFF);
        }
    }
}
