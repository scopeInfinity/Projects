package com.example.gagan.attendancebooster;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Introduction extends ActionBarActivity {
    Intent in;
    static MainActivity mainActivityObj=null;


    boolean openSettingLater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        in=getIntent();
        openSettingLater=false;
        if(in.hasExtra("settingLater"));
            openSettingLater=in.getBooleanExtra("settingLater",true);
    }

    public void closeActivity(View v)
    {
        if(v.getId()!=R.id.id_intro_done) return;
        if(openSettingLater)
        {
            if(mainActivityObj==null)
                mainActivityObj=new MainActivity();
            mainActivityObj.openSetting();
        }

        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_introduction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
