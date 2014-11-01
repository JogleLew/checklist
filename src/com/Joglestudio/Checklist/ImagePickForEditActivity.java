package com.Joglestudio.Checklist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jogle on 14/10/8.
 */
public class ImagePickForEditActivity extends ListActivity {

    private int pos;
    private List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
    public static final String theme[] = {"深色主题","浅色主题"};
    private DataModel data = new DataModel();
    private int themeNum;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        themeNum = Integer.parseInt(readFileData("theme.dat"));

        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), itemList, R.layout.imagelist,
                new String[]{"img","imagename"},
                new int[]{R.id.img,R.id.imagename});
        if (themeNum == 1){
            adapter = new SimpleAdapter(getApplicationContext(), itemList, R.layout.imagelist1,
                    new String[]{"img", "imagename"},
                    new int[]{R.id.img, R.id.imagename});
        }

        setListAdapter(adapter);
        setContentView(R.layout.imagepick);

        changeTheme();
        Intent toEditIntent = getIntent();
        int i = toEditIntent.getIntExtra("position", -1);
        pos = i;
        init();

        String s = readFileData("editing" + i + ".dat");
        data = DataModel.getUnpackedData(s);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                data.setImagePicked(i + 1);
                String s = DataModel.getPackedString(data);
                writeFileData("editing" + pos + ".dat", s);
                Intent sureSelectImageIntent = new Intent();
                sureSelectImageIntent.setClass(ImagePickForEditActivity.this, EditItemActivity.class);
                sureSelectImageIntent.putExtra("position", pos);
                startActivity(sureSelectImageIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                ImagePickForEditActivity.this.finish();
            }
        });

        Button backToAdd2Button = (Button) findViewById(R.id.backtoadd2);
        backToAdd2Button.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent backToAdd2Intent = new Intent();
                backToAdd2Intent.setClass(ImagePickForEditActivity.this, EditItemActivity.class);
                backToAdd2Intent.putExtra("position", pos);
                startActivity(backToAdd2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                ImagePickForEditActivity.this.finish();
            }
        });

    }

    private void init(){
        Map<String, Object> map = new HashMap<String, Object>();
        if (themeNum == 0) {
            map.put("img", R.drawable.w1);
            map.put("imagename", "空白");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w2);
            map.put("imagename", "文件夹");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w3);
            map.put("imagename", "闹钟");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w4);
            map.put("imagename", "礼物");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w5);
            map.put("imagename", "旅行");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w6);
            map.put("imagename", "饮料");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w7);
            map.put("imagename", "生活");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w8);
            map.put("imagename", "收件箱");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w9);
            map.put("imagename", "摄像");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.w10);
            map.put("imagename", "家务");
            itemList.add(map);
        }
        else if (themeNum == 1){
            map.put("img", R.drawable.w1);
            map.put("imagename", "空白");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b2);
            map.put("imagename", "文件夹");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b3);
            map.put("imagename", "闹钟");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b4);
            map.put("imagename", "礼物");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b5);
            map.put("imagename", "旅行");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b6);
            map.put("imagename", "饮料");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b7);
            map.put("imagename", "生活");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b8);
            map.put("imagename", "收件箱");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b9);
            map.put("imagename", "摄像");
            itemList.add(map);
            map = new HashMap<String, Object>();
            map.put("img", R.drawable.b10);
            map.put("imagename", "家务");
            itemList.add(map);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent backToAdd2Intent = new Intent();
                backToAdd2Intent.setClass(ImagePickForEditActivity.this, EditItemActivity.class);
                backToAdd2Intent.putExtra("position", pos);
                startActivity(backToAdd2Intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                ImagePickForEditActivity.this.finish();
            break;
            case KeyEvent.KEYCODE_MENU:
                showMenu();
            break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showMenu(){
        new AlertDialog.Builder(ImagePickForEditActivity.this)
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
                        themeIntent.setClass(ImagePickForEditActivity.this, ImagePickForEditActivity.class);
                        themeIntent.putExtra("position", pos);
                        startActivity(themeIntent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        ImagePickForEditActivity.this.finish();
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
        themeNum = Integer.parseInt(readFileData("theme.dat"));
        if (themeNum == 1) {
            RelativeLayout titleBar5 = (RelativeLayout) findViewById(R.id.titlebar5);
            titleBar5.setBackgroundColor(0xFFFFFFFF);
            Button aboutButton = (Button) findViewById(R.id.backtoadd2);
            aboutButton.setTextColor(0xFF27C7FF);

            TextView title = (TextView) findViewById(R.id.title5);
            title.setTextColor(0xFF000000);
            TextView subtitle = (TextView) findViewById(R.id.subtitle5);
            subtitle.setTextColor(0xFF000000);
            LinearLayout imagepicklayout = (LinearLayout) findViewById(R.id.imagepicklayout);
            imagepicklayout.setBackgroundColor(0xFFFFFFFF);
        }
    }
}
