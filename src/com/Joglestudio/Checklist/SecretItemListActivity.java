package com.Joglestudio.Checklist;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


public class SecretItemListActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    private List<Map<String, Object>> itemList = new ArrayList<Map<String, Object>>();
    public static final String menu[] = {"主题","关于"};
    public static final String theme[] = {"深色主题","浅色主题"};
    private int themeNum;
    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), itemList, R.layout.itemlist,
                new String[]{"title", "info", "img", "tick"},
                new int[]{R.id.title, R.id.info, R.id.img, R.id.tick});

        if (themeNum == 1){
            adapter = new SimpleAdapter(getApplicationContext(), itemList, R.layout.itemlist1,
                    new String[]{"title", "info", "img", "tick"},
                    new int[]{R.id.title, R.id.info, R.id.img, R.id.tick});
        }

        setListAdapter(adapter);
        setContentView(R.layout.items);

        TextView title = (TextView)findViewById(R.id.title);

        changeTheme();
        title.setTextColor(0xFF27C7FF);

        //aboutButton
        Button aboutButton = (Button) findViewById(R.id.about);
        aboutButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                showMenu();
            }
        });
        aboutButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent secretIntent = new Intent();
                secretIntent.setClass(SecretItemListActivity.this, ItemListActivity.class);
                startActivity(secretIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretItemListActivity.this.finish();
                return true;
            }
        });

        //addItemButton
        Button addItemButton = (Button) findViewById(R.id.newitem);
        addItemButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent addItemIntent = new Intent();
                addItemIntent.setClass(SecretItemListActivity.this, SecretAddItemActivity.class);

                String numString = readFileData("scount.dat");
                int num = Integer.parseInt(numString);
                addItemIntent.putExtra("position", num);

                startActivity(addItemIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretItemListActivity.this.finish();
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int pos = i;

                    TextView tick = (TextView) view.findViewById(R.id.tick);
                    tick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String s = (String) itemList.get(pos).get("tick");

                            String item = readFileData("s" + pos + ".dat");
                            DataModel dataModel = DataModel.getUnpackedData(item);

                            if (s.equals("√")) {
                                dataModel.setIsTicked(false);
                                itemList.get(pos).put("tick", " ");
                                //showToast("[" + itemList.get(pos).get("title").toString() + "] " + "未完成");
                            } else {
                                dataModel.setIsTicked(true);
                                itemList.get(pos).put("tick", "√");
                                //showToast("[" + itemList.get(pos).get("title").toString() + "] " + "已完成");
                            }

                            String newString = DataModel.getPackedString(dataModel);
                            writeFileData("s" + pos + ".dat", newString);

                            SimpleAdapter adapter = (SimpleAdapter) getListView().getAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    });
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toEditIntent = new Intent();
                toEditIntent.setClass(SecretItemListActivity.this, SecretEditItemActivity.class);
                toEditIntent.putExtra("position", i);
                startActivity(toEditIntent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                SecretItemListActivity.this.finish();
                return true;
            }
        });
    }

    private void init(){
        int i;
        //For people who first use
        if (!isFileExists("scount.dat")){
            Calendar calendar=Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            String a = "进入私密空间时，标题“清单”将会变为蓝色\n"+year+" "+monthOfYear+" "+dayOfMonth+" "+hourOfDay+" "+minute+"\n2\ntrue";
            writeFileData("s0.dat", a);
            writeFileData("scount.dat", "1");
            writeFileData("stheme.dat", "0");
        }

        //Get number of items
        String numberOfItemsString = readFileData("scount.dat");
        int numberOfItems = Integer.parseInt(numberOfItemsString);
        themeNum = Integer.parseInt(readFileData("stheme.dat"));

        for (i = 0; i < numberOfItems; i++){
            if (isFileExists("s"+i+".dat")) {
                Map<String, Object> map = makemap(i);
                itemList.add(map);
            }
            else{
                writeFileData("scount.dat", i+"");
                break;
            }
        }

    }

    public Map<String, Object> makemap(int i){
        Map<String, Object> map = new HashMap<String, Object>();
        String item = readFileData("s" + i + ".dat");
        DataModel dataModel = DataModel.getUnpackedData(item);
        map.put("title", dataModel.getItemName());
        if (dataModel.getMinute()>= 10)
            map.put("info", dataModel.getYear() + "年" + dataModel.getMonth() + "月" + dataModel.getDay() + "日"
                    + dataModel.getHour() + "点" + dataModel.getMinute() + "分");
        else
            map.put("info", dataModel.getYear() + "年" + dataModel.getMonth() + "月" + dataModel.getDay() + "日"
                    + dataModel.getHour() + "点0" + dataModel.getMinute() + "分");
        if (themeNum == 0) {
            switch (dataModel.getImagePicked()) {
                case 1:
                    map.put("img", R.drawable.w1);
                    break;
                case 2:
                    map.put("img", R.drawable.w2);
                    break;
                case 3:
                    map.put("img", R.drawable.w3);
                    break;
                case 4:
                    map.put("img", R.drawable.w4);
                    break;
                case 5:
                    map.put("img", R.drawable.w5);
                    break;
                case 6:
                    map.put("img", R.drawable.w6);
                    break;
                case 7:
                    map.put("img", R.drawable.w7);
                    break;
                case 8:
                    map.put("img", R.drawable.w8);
                    break;
                case 9:
                    map.put("img", R.drawable.w9);
                    break;
                case 10:
                    map.put("img", R.drawable.w10);
                    break;
                default:
                    map.put("img", R.drawable.w1);
                    break;
            }
        }
        else if (themeNum == 1) {
            switch (dataModel.getImagePicked()) {
                case 1:
                    map.put("img", R.drawable.w1);
                    break;
                case 2:
                    map.put("img", R.drawable.b2);
                    break;
                case 3:
                    map.put("img", R.drawable.b3);
                    break;
                case 4:
                    map.put("img", R.drawable.b4);
                    break;
                case 5:
                    map.put("img", R.drawable.b5);
                    break;
                case 6:
                    map.put("img", R.drawable.b6);
                    break;
                case 7:
                    map.put("img", R.drawable.b7);
                    break;
                case 8:
                    map.put("img", R.drawable.b8);
                    break;
                case 9:
                    map.put("img", R.drawable.b9);
                    break;
                case 10:
                    map.put("img", R.drawable.b10);
                    break;
                default:
                    map.put("img", R.drawable.w1);
                    break;
            }
        }

        if (dataModel.getIsTicked())
            map.put("tick", "√");
        else
            map.put("tick", " ");
        return map;
    }
    public void showInfo(){
        new AlertDialog.Builder(this)
                .setTitle("备忘录")
                .setMessage("作者：13061086 吕佳高\n\n" +
                        "简介：一款实用的备忘录软件，方便记录自己需要做的事情和勾选已经完成的事件。\n\n" +
                        "使用说明：点击项目右侧以勾选或取消勾选，长按以编辑和删除项目\n\n" +
                        "版本：1.0")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    public void showMenu(){
        new AlertDialog.Builder(SecretItemListActivity.this)
                .setTitle("设定")
                .setItems(menu, new DialogInterface.OnClickListener() {
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
                        themeIntent.setClass(SecretItemListActivity.this, SecretItemListActivity.class);
                        startActivity(themeIntent);
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        SecretItemListActivity.this.finish();
                    }
                })
                .show();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                    Intent secretIntent = new Intent();
                    secretIntent.setClass(SecretItemListActivity.this, ItemListActivity.class);
                    startActivity(secretIntent);
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    SecretItemListActivity.this.finish();
                break;
            case KeyEvent.KEYCODE_MENU:
                showMenu();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeTheme(){
        themeNum = Integer.parseInt(readFileData("stheme.dat"));
        if (themeNum == 1) {
            RelativeLayout titleBar1 = (RelativeLayout) findViewById(R.id.titlebar1);
            titleBar1.setBackgroundColor(0xFFFFFFFF);
            Button aboutButton = (Button) findViewById(R.id.about);
            aboutButton.setTextColor(0xFF27C7FF);
            Button newItemButton = (Button) findViewById(R.id.newitem);
            newItemButton.setTextColor(0xFF27C7FF);
            TextView title = (TextView) findViewById(R.id.title);
            title.setTextColor(0xFF000000);
            TextView subtitle = (TextView) findViewById(R.id.subtitle);
            subtitle.setTextColor(0xFF000000);
            getListView().setBackgroundColor(0xFFFFFFFF);
            
        }
    }

}