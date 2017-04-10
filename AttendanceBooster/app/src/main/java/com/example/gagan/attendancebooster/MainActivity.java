package com.example.gagan.attendancebooster;


import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    static String LOG_TEXT=MainActivity.class.getSimpleName();
    static boolean waitForOpenSetting;
    static Calendar calMA;
    static ListAdapter MA_listadapter;
    static ListView MA_listview;
    static boolean DB_LOADED=false,DB_LOADED_FT=true;
    static LinearLayout MA_llList;
    static MainActivity activity;
    static int MA_date_d=-1,MA_date_m=-1,MA_date_y=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        activity=this;
        waitForOpenSetting=true;

        Log.w(LOG_TEXT,"Opening Introduction");

        Information.makedata_course_list_relatedNotNull();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


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
            openSetting();
            return true;
        } else if(id == R.id.action_introduction)
        {
            openIntroduction(this);
        }
        else if(id == R.id.action_c_tt)
        {
            openCTT();
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSetting()
    {
        Intent in=new Intent(this,Information.class);
        startActivity(in);
    }
    public void openCTT()
    {
        Log.w(LOG_TEXT,"Open CTT");

        if(!Information.isDateInit())
        {
            Log.w(LOG_TEXT,"Date Not Init!!");
            Toast.makeText(getApplicationContext(),"First Change Semester Duration",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent in=new Intent(this,time_table.class);
        Log.w(LOG_TEXT, "Starting TT!!");
        startActivity(in);
    }
    public static void openIntroduction(MainActivity obj,boolean openSettingLater)
    {

        Intent in=new Intent(activity,Introduction.class);
        in.putExtra("settingLater",openSettingLater);
        Log.w(LOG_TEXT,"Opening Introduction : DataSet .Starting Activity");

        activity.startActivity(in);
        Log.w(LOG_TEXT,"Opening Introduction : Giving MainActivityObj");

        Introduction.mainActivityObj=activity;
    }
    public void openIntroduction(MainActivity obj)
    {
        openIntroduction(obj,false);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        static View rootView;
        static TextView tv_MT_sdate;


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_main, container, false);

            tv_MT_sdate=(TextView) rootView.findViewById(R.id.id_tv_MA_date);
            ((Button)rootView.findViewById(R.id.id_b_MA_datetoday)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init_date_today();
                }
            });
            ((Button)rootView.findViewById(R.id.id_b_MA_pickdate)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init_date_pick();
                }
            });

            //MA_listview=(ListView)rootView.findViewById(R.id.id_lv_MA);
            MA_llList=(LinearLayout) rootView.findViewById(R.id.id_list_MA);

            ((Button)rootView.findViewById(R.id.id_b_destroy_db)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    func_DestroyDB();
                }
            });

            if(!is_date_set())
                init_date_today();
            init_date();

            return rootView;

        }
        void func_DestroyDB()
        {
            Toast.makeText(activity,"DESTROY",Toast.LENGTH_SHORT).show();
            Information.destroyDB(Information.data_default_filename);
        }


        private void init_date_pick() {
            Intent in=new Intent(getActivity(),time_table.class);
            in.putExtra("returnDate",true);
            activity.startActivity(in);
        }

        public int init_date_today()
        {
            calMA=Calendar.getInstance();
            return  init_date();
        }
        private static boolean is_date_set()   //rought check
        {
            if(MA_date_d>0 && MA_date_m>=0 && MA_date_y>0) return true;
            return false;

        }
        public static int init_date()
        {
            if(calMA==null) {tv_MT_sdate.setText("dd - month - yyyy");return -101;}

            MA_date_d=calMA.get(Calendar.DATE);
            MA_date_m=calMA.get(Calendar.MONTH);
            MA_date_y=calMA.get(Calendar.YEAR);
            if(!is_date_set()) {tv_MT_sdate.setText("dd - month - yyyy");return -101;}
            DateFormatSymbols dfs=new DateFormatSymbols();

            String str=String.format("%02d - %s - %d",MA_date_d,dfs.getMonths()[MA_date_m],MA_date_y);
            tv_MT_sdate.setText(str);

             return  updateList();

        }
        public static int updateList()
        {
            Log.v(LOG_TEXT,"UpdateList");
            int temp=init_listattend();

            if(calMA==null) return -101;

            final int day=calMA.get(Calendar.DAY_OF_WEEK)-1;
            //getCourseName
            /*
            List lst=new ArrayList<String>();
            for(int i=0;i<Information.data_course_tt[day].size();i++)
            {

                lst.add(Information.getCourseName((int)Information.data_course_tt[day].get(i)));
                //lst.add(Information.data_course_tt[day].get(i));
            }
            MA_listadapter=new ArrayAdapter<String>(activity,R.layout.frag_list_ma_element,R.id.id_cb_MA_element,lst);
            MA_listview.setAdapter(MA_listadapter);
            Toast.makeText(activity,"something",Toast.LENGTH_SHORT).show();

            MA_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //can be used to save data
               //         Toast.makeText(activity,"Clicked on "+((CheckBox)view).getText()+" id="+Information.data_course_tt[day].get(position),Toast.LENGTH_SHORT).show();

                }
            });
            */
            HashMap count_temp=new HashMap();
            MA_llList.removeAllViews();
            Log.v(LOG_TEXT,"MA_LLLIST ALL REMOVED");

            for(int i=0;i<Information.data_course_tt[day].size();i++)
            {
                final LinearLayout ll=new LinearLayout(activity);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv=new TextView(activity);
                tv.setTextSize(25);
                ImageView iv=new ImageView(activity);
                tv.setText(Information.getCourseName((int)Information.data_course_tt[day].get(i)));
                final int iid=(int)Information.data_course_tt[day].get(i);
                tv.setText(Information.getCourseName(iid));
                if(count_temp.containsKey(iid))
                    count_temp.put(iid,1+(int)count_temp.get(iid));
                else
                    count_temp.put(iid,0);
                final int c=(int)count_temp.get(iid);

                ll.addView(iv);
                ll.addView(tv);
                setMe(ll,iid,c);
                MA_llList.addView(ll);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleFiled(ll,iid,c);
                    }
                });
                tv.setHighlightColor(Color.YELLOW);
            }
            Log.v(LOG_TEXT,"RETURNING");

            return temp;

        }
        static void makeViewMA(View v,boolean present)
        {
            TextView tv=(TextView)((LinearLayout)v).getChildAt(1);
            ImageView iv=(ImageView)((LinearLayout)v).getChildAt(0);

            if(present)
                iv.setBackgroundResource(R.drawable.img_tick_y);//tv.setBackgroundColor(Color.GREEN);
            else
                iv.setBackgroundResource(R.drawable.img_tick_n);//tv.setBackgroundColor(Color.YELLOW);



         }
        static void setMe(View v,int id,int count)
        {
            toggleFilednSetMe(v,id,count,false);
        }
        static void toggleFiled(View v,int id,int count)
        {
            toggleFilednSetMe(v,id,count,true);
        }
        static void toggleFilednSetMe(View v,int id,int count,boolean toggle)
        {
            ///////////////////////
            ////////










            //////////////////////
            int dn=(int)(calMA.getTimeInMillis()/3600/24);//CalMa get date eq no
            if(!Information.data_attend.containsKey(id))
                Information.data_attend.put(id, new HashMap());
            if(!Information.data_attend.get(id).containsKey(dn))
                Information.data_attend.get(id).put(dn, 0);
            int temp=(int)Information.data_attend.get(id).get(dn);
            if(toggle)
            Information.data_attend.get(id).put(dn,temp^(1<<count));
            if((((int)Information.data_attend.get(id).get(dn))&(1<<count))!=0)
                makeViewMA(v,true);
            else makeViewMA(v,false);
            if(toggle)
                Information.updateDataToDB();

        }
        static int init_listattend() {
            Log.v(LOG_TEXT,"InitListAttend");

            if (Information.data_attend == null)
                Information.data_attend = new HashMap<Integer, HashMap>();
            else
                //Clear Contents
                Information.data_attend.clear();
            int temp;
            Log.v(LOG_TEXT,"CALLING LOAD DB");

            if ((temp=Information.updateDataFromDB()) != 0) {

                for (int i = 0; i < Information.data_course_listID.size(); i++)
                    Information.data_attend.put((int) Information.data_course_listID.get(i), new HashMap());
                Log.v(LOG_TEXT,"LOAD DB FAIL");

            }
            else
                Log.v(LOG_TEXT,"LOAD DB SUCCESS");
            // Toast.makeText(activity,"LOADED RET : "+temp,Toast.LENGTH_SHORT).show();


            return temp;

        }



        static void save_listattend()   //free the map
        {
           Information.updateDataToDB();

        }






    }


}
