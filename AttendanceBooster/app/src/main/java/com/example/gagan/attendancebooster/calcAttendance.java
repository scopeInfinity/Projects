package com.example.gagan.attendancebooster;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class calcAttendance extends ActionBarActivity {
    TextView output_text;
    static List calc_course,calc_att,calc_tot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_attendance);
        output_text=(TextView)findViewById(R.id.id_tv_ca_text);
        findViewById(R.id.id_b_ca_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calculate())
                    output_text.setText(toString());

            }
        });

    }

    static boolean calculate()
    {
        if(!Information.isDateInit()) return false;
        if(!Information.JustcheckSEdate()) return false;

        Information.gvS.


        calc_course=new ArrayList();
        calc_att=new ArrayList();
        calc_tot=new ArrayList();

        if(Information.data_attend==null)
            Information.data_attend=new HashMap<Integer,HashMap>();

            Set data_attend_set=Information.data_attend.entrySet();
            Iterator<Map.Entry<Integer,HashMap>> it= data_attend_set.iterator();
            while(it.hasNext())
            {
                Map.Entry<Integer,HashMap> eihm=it.next();
                calc_course.add((int) eihm.getKey());
                HashMap hm=eihm.getValue();
                int a=0;
                /**************Storing Inner hashmap*******/
                Set innerSet=hm.entrySet();
                Iterator iti=innerSet.iterator();

                while(iti.hasNext())
                {
                    Map.Entry e=(Map.Entry<Integer,Integer>)iti.next();
                    int x=(int)e.getValue();
                    int count=0;
                    while(x!=0)
                    {
                        x=x&(x-1);count++;
                    }
                    int day=(int)e.getKey();

                    a+=count;
                }
                calc_att.add(a);


            }

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc_attendance, menu);
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
