package com.example.gagan.attendancebooster;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Information extends ActionBarActivity {
    static Activity activity;
    static LinearLayout GLOBAL_LL;

    static int INT_TEMP;
    static TextView TextView_TEMP;
    String LOG_TEXT=Information.class.getSimpleName();
    static showDatePicker date_picker_obj;
    static int date_picker_current_id=-1;

    static EditText et_date_s_sem,et_date_e_sem,et_data_name;
    static EditText et_noL[];
    static List<EditText> alert_et_l;
    static ImageView iv_button_nS[];
    static Button b_course_add;
    static LinearLayout b_course_list_lay;



    static final String data_default_filename="db_attendancebooster";

    static String data_name="";
    static int data_date_s_sem_y=-1,data_date_s_sem_m=-1,data_date_s_sem_d=-1;
    static int data_date_e_sem_y=-1,data_date_e_sem_m=-1,data_date_e_sem_d=-1;

    static List[] data_course_tt;//course id

    static List<String> data_course_list;
    static List data_course_listID;
    static int data_course_listIDC;
    static HashMap<Integer,HashMap> data_attend;


    //Not Used Neither Saved
    static boolean data_nS[]={false,true,true,true,true,true,false};
    static int data_nS_noL[]={0,0,0,0,0,0,0};
    static List<String>[] data_nS_lec=new ArrayList[7];

    static GregorianCalendar gvS,gvE,gvS1;


    static String getCourseName(int id) {
     if(data_course_listID==null) return null;
        if(  data_course_list==null) return null;

        for (int i = 0; i < data_course_listID.size(); i++)
            if (id == (int) data_course_listID.get(i)) {
                    return data_course_list.get(i);
            }
        return  null;
    }
    static boolean isDateInit()
    {
        if(Information.data_date_s_sem_y==-1 || Information.data_date_e_sem_y==-1 || Information.data_date_s_sem_d==-1 || Information.data_date_e_sem_d==-1
                || Information.data_date_s_sem_m==-1 || Information.data_date_e_sem_m==-1)
            return false;
        return true;
    }
    static boolean checkSEdate()
    {

        if(!JustcheckSEdate())
            return false;
        int ty=data_date_s_sem_y,tm=data_date_s_sem_m-1;
        if(tm==11) {ty++;tm=1;}
        else tm++;

        if(gvS1==null)
            gvS1=new GregorianCalendar(ty,tm,data_date_s_sem_d);
        else
            gvS1.set(ty,tm,data_date_s_sem_d);

        if(gvS1.after(gvE))
            return false;


        return true;

    }
    static boolean JustcheckSEdate()
    {

        if(gvS==null)
            gvS=new GregorianCalendar(data_date_s_sem_y,data_date_s_sem_m-1,data_date_s_sem_d);
        else
            gvS.set(data_date_s_sem_y,data_date_s_sem_m-1,data_date_s_sem_d);

        if(gvE==null)
            gvE=new GregorianCalendar(data_date_e_sem_y,data_date_e_sem_m-1,data_date_e_sem_d);
        else
            gvE.set(data_date_e_sem_y,data_date_e_sem_m-1,data_date_e_sem_d);

        if(gvS.after(gvE))
            return false;




        return true;

    }
    static void updateNSImage()
    {
        View tmp;
        TextView tv;
        for(int i=0;i<7;i++) {
            switch (i)
            {
                case 0: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL0);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL0);break;
                case 1: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL1);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL1);break;
                case 2: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL2);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL2);break;
                case 3: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL3);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL3);break;
                case 4: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL4);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL4);break;
                case 5: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL5);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL5);break;
                case 6: tmp= PlaceholderFragment.root_view_global.findViewById(R.id.id_b_noL6);tv=(TextView)PlaceholderFragment.root_view_global.findViewById(R.id.id_noL6);break;
                default:tmp=null;tv=null;

            }
            if (data_nS[i]) {
                iv_button_nS[i].setBackgroundResource(R.drawable.img_tick_y);
                if(tmp!=null)
                tmp.setAlpha(1f);
            }
            else
            {
                iv_button_nS[i].setBackgroundResource(R.drawable.img_tick_n);
                if(tmp!=null)
                    tmp.setAlpha(0.5f);
            }
            if(tv!=null)
               tv.setText(data_nS_noL[i]+"");


        }


    }

    static int updateDataFromDB()   //return ERROR CODE
    {
        //Add Course



        //Give value to all courses

        int x= loadAllDataFromDB(data_default_filename);

        //Toast.makeText(MainActivity.activity,"Trying To Load DB : "+data_default_filename,Toast.LENGTH_SHORT).show();

        if(MainActivity.DB_LOADED_FT && !MainActivity.DB_LOADED)
        {
            MainActivity.activity.openIntroduction(MainActivity.activity, true);
            MainActivity.DB_LOADED_FT=false;
        }
        //if(x==0)
        //    Toast.makeText(MainActivity.activity,"DB Loaded",Toast.LENGTH_SHORT).show();
        //else Toast.makeText(MainActivity.activity,"DB NOT Loaded, Error Code : "+x,Toast.LENGTH_SHORT).show();
        return x;
    }
    static boolean updateDataToDB()
    {
       boolean x=saveAllDataToDB(data_default_filename);
       //if(x) Toast.makeText(MainActivity.activity,"DB Saved",Toast.LENGTH_SHORT).show();
        //else Toast.makeText(MainActivity.activity,"DB NOT Saved",Toast.LENGTH_SHORT).show();
        return x;

    }
    static boolean saveAllDataToDB(String filename)
    {
        try {
            FileOutputStream fos= MainActivity.activity.openFileOutput(filename, Context.MODE_PRIVATE);
            PrintStream ps=new PrintStream(fos);
            int temp;
            if((temp=data_name.indexOf('\n'))>=0) data_name=data_name.substring(0,temp);


            ps.println(data_name);
            ps.printf("%d %d %d %d %d %d\n",data_date_s_sem_y,data_date_s_sem_m,data_date_s_sem_d,data_date_e_sem_y,data_date_e_sem_m,data_date_e_sem_d);
            //data_course_tt
            if(data_course_tt==null)
                data_course_tt=new List[7];

            for(int i=0;i<7;i++)
            {
                if(data_course_tt[i]==null) data_course_tt[i]=new ArrayList();
                ps.print(data_course_tt[i].size()+" ");
                for(int j=0;j<data_course_tt[i].size();j++)
                    ps.print((int)data_course_tt[i].get(j)+" ");
                ps.println();

            }
            //data_course_listID & realted
            makedata_course_list_relatedNotNull();
            //Toast.makeText(activity,"IDC:"+data_course_listIDC,Toast.LENGTH_SHORT).show();
            ps.println(data_course_listIDC+" "+data_course_list.size()+" "+data_course_listID.size());
            for(int i=0;i<data_course_list.size();i++)
                ps.println(data_course_list.get(i));
            for(int i=0;i<data_course_listID.size();i++)
                ps.println(data_course_listID.get(i));
            ps.close();
            fos.close();

            fos= MainActivity.activity.openFileOutput(filename+"_att", Context.MODE_PRIVATE);
            ps=new PrintStream(fos);



            //data_attend    hashmap inside hashmap
            if(data_attend==null)
                data_attend=new HashMap<Integer,HashMap>();

            Set data_attend_set=data_attend.entrySet();
            ps.println(data_attend_set.size());
            /******For Each Course*******/
            {
                Iterator<Map.Entry<Integer,HashMap>> it= data_attend_set.iterator();
                while(it.hasNext())
                {
                  Map.Entry<Integer,HashMap> eihm=it.next();
                   ps.print((int)eihm.getKey()+" ");
                   HashMap hm=eihm.getValue();
                    /**************Storing Inner hashmap*******/
                   Set innerSet=hm.entrySet();
                    Iterator iti=innerSet.iterator();
                    ps.print((int)hm.size()+" ");   //No of days

                    while(iti.hasNext())
                    {
                        Map.Entry e=(Map.Entry<Integer,Integer>)iti.next();
                        ps.print(e.getKey()+" "+e.getValue()+" ");
                    }


                }
                ps.println();
            }
            ps.close();




            fos.close();
           // showDataFromDB(filename);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;//////////////////////
        }

        return true;
    }
    static void showDataFromDB(String filename)
    {
        FileInputStream fis= null;
        try {
            fis = MainActivity.activity.openFileInput(filename);
        Scanner in=new Scanner(fis);
        String str="";
        while(in.hasNext())
        {
            str+=in.nextLine()+"\n";
        }
        fis.close();
        Toast.makeText(MainActivity.activity,str,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //1 = FileNotFound
    static boolean destroyDB(String filename)
    {
        try {


            FileOutputStream fos = MainActivity.activity.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.close();
        }catch (Exception e) {
            return false;
        }
        return true;
    }
    static void resetAllData()
    {
        data_name="";
        data_date_s_sem_y=-1;
        data_date_s_sem_m=-1;
        data_date_s_sem_d=-1;
        data_date_e_sem_y=-1;
        data_date_e_sem_m=-1;
        data_date_e_sem_d=-1;


        makedata_course_list_relatedNotNull();
        for (int i = 0; i < 7; i++)
            if (data_course_tt[i] != null)  data_course_tt[i].clear();

        data_course_listIDC=0;
        data_course_list.clear();
        data_course_listID.clear();
        if (data_attend == null)
            data_attend = new HashMap<Integer, HashMap>();
        data_attend.clear();

    }
    static int loadAllDataFromDB(String filename)
    {
        //showDataFromDB(filename);

        FileInputStream fis;
        Scanner in;
        try {
            MainActivity.DB_LOADED=false;

            try {
                fis = MainActivity.activity.openFileInput(filename);
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }

            in = new Scanner(fis);
            //RESET ALL DATA
            resetAllData();



            data_name = in.nextLine();
            data_date_s_sem_y = in.nextInt();
            data_date_s_sem_m = in.nextInt();
            data_date_s_sem_d = in.nextInt();
            data_date_e_sem_y = in.nextInt();
            data_date_e_sem_m = in.nextInt();
            data_date_e_sem_d = in.nextInt();


            //data_course_tt
            if (data_course_tt == null)
                data_course_tt = new List[7];
            for (int i = 0; i < 7; i++) {

                data_course_tt[i].clear();
                int size = in.nextInt();
                for (int j = 0; j < size; j++)
                    data_course_tt[i].add(in.nextInt());

            }


            //data_course_listID & realted
            data_course_listIDC = in.nextInt();
            int s1 = in.nextInt(), s2 = in.nextInt();

            in.nextLine();      //For Ignoring Rest of blank Line
            for (int i = 0; i < s1; i++)
                data_course_list.add(in.nextLine());
            for (int i = 0; i < s2; i++)
                data_course_listID.add(in.nextInt());



            in.close();
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                return 2;
            }

           }catch (Exception e)
        {
            return  -100;   //Other Exception
        }






        /**************ATTENDANCE***********/



       try {


           try {
               fis = MainActivity.activity.openFileInput(filename + "_att");
           } catch (FileNotFoundException e) {
               e.printStackTrace();
               return 3;
           }

           in = new Scanner(fis);

           //data_attend    hashmap inside hashmap
           //Toast.makeText(activity, "Loaded FROM DB", Toast.LENGTH_SHORT).show();
           int size = in.nextInt();

           //    /For Each Course//////////

           for (int i = 0; i < size; i++) {
               int courseID = in.nextInt(), nod = in.nextInt();
               HashMap hm = new HashMap<Integer, Integer>();
               for (int j = 0; j < nod; j++) {
                   int key = in.nextInt(), val = in.nextInt();
                   // Toast.makeText(activity,"Course : "+getCourseName(courseID)+" > Day:"+key+" > Val:"+val,Toast.LENGTH_SHORT).show();
                   hm.put(key, val);
               }
               data_attend.put(courseID, hm);
           }
           // Toast.makeText(activity, "Loading Comp.", Toast.LENGTH_SHORT).show();


           in.close();
           try {
               fis.close();
           } catch (IOException e) {
               e.printStackTrace();
               return 4;
           }
       }catch (Exception e){return -200;//Some Other Error

       }
        MainActivity.DB_LOADED=true;
        return 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        activity=this;

        Log.w(LOG_TEXT, "Information Started!!");




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information, menu);
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

    public void show_date_pick(View v)
    {

        Log.w(LOG_TEXT,"Clicked!!" );
        date_picker_obj=new showDatePicker();
        DialogFragment dp=date_picker_obj;

        Bundle b=new Bundle();
        date_picker_current_id=v.getId();
        switch (date_picker_current_id) {
            case R.id.bt_date_sem_start:
                b.putInt("d", data_date_s_sem_d);
                b.putInt("m", data_date_s_sem_m);
                b.putInt("y", data_date_s_sem_y);
                break;

            default ://R.id.bt_date_sem_end :
                b.putInt("d", data_date_e_sem_d);
                b.putInt("m", data_date_e_sem_m);
                b.putInt("y", data_date_e_sem_y);
                //            break;

        }

        Log.w(LOG_TEXT, "casedone!!");

        dp.setArguments(b);
        dp.show(getFragmentManager() ,"datePicker");

    }
    public static void update_date_picker()
    {
        switch (date_picker_current_id) {
            case R.id.bt_date_sem_start:
                data_date_s_sem_m = date_picker_obj.getMonth();
                data_date_s_sem_d = date_picker_obj.getDate();
                data_date_s_sem_y = date_picker_obj.getYear();
                break;
            case R.id.bt_date_sem_end:
                data_date_e_sem_m = date_picker_obj.getMonth();
                data_date_e_sem_d = date_picker_obj.getDate();
                data_date_e_sem_y = date_picker_obj.getYear();
                         break;
        }
        updateFields();

    }
    public void edit_iv_data_lecuture(View v)
    {
        switch (v.getId()) {
            case R.id.id_b_noL0:modify_data_lecture(0,(TextView)findViewById(R.id.id_noL0));break;
            case R.id.id_b_noL1:modify_data_lecture(1,(TextView)findViewById(R.id.id_noL1));break;
            case R.id.id_b_noL2:modify_data_lecture(2,(TextView)findViewById(R.id.id_noL2));break;
            case R.id.id_b_noL3:modify_data_lecture(3,(TextView)findViewById(R.id.id_noL3));break;
            case R.id.id_b_noL4:modify_data_lecture(4,(TextView)findViewById(R.id.id_noL4));break;
            case R.id.id_b_noL5:modify_data_lecture(5,(TextView)findViewById(R.id.id_noL5));break;
            case R.id.id_b_noL6:modify_data_lecture(6,(TextView)findViewById(R.id.id_noL6));break;


        }
    }
    public boolean isPosInteger(String num)
    {
        int x=0;
        try {
            x = Integer.parseInt(num);
            if(x<0) return false;
        }catch (Exception e){;
        return false;}
        return true;
    }
    public void modify_data_lecture(int no,TextView tv)
    {
        if(!data_nS[no]) return ;//If it was disabled;



        String num;
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        String day=DateFormatSymbols.getInstance().getWeekdays()[no+1];
        alert.setTitle(day).setMessage("No. of Lectures");
        final EditText et=new EditText(this);
        et.setText(data_nS_noL[no] + "");
        et.setWidth(1000);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        INT_TEMP=no;
        TextView_TEMP=tv;
        alert.setPositiveButton("Modify",new DialogInterface.OnClickListener() {
            int id_no=INT_TEMP;
            TextView tv=TextView_TEMP;
            @Override
            public void onClick(DialogInterface dialog, int which) {
                modify_data_lecture_phase2(id_no,tv,et.getText().toString());
            }
        });
        alert.setNegativeButton("Back",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setView(et);
        alert.show();

    }
    public void modify_data_lecture_phase2(int no,TextView tv,String num)
    {
        if(isPosInteger(num))
        {
            tv.setText(num);
            data_nS_noL[no]=Integer.parseInt(num);
            if(data_nS_noL[no]==0)
                return;

            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            String day=DateFormatSymbols.getInstance().getWeekdays()[no+1];
            alert.setTitle(day).setMessage("Lecture List");
            ScrollView sv=new ScrollView(this);

            LinearLayout ll=new LinearLayout(this);
            sv.addView(ll);
            alert_et_l= new ArrayList<EditText>();

            for(int i=0;i<data_nS_noL[no];i++)
            {
                alert_et_l.add((EditText)(new EditText(this)));
                alert_et_l.get(i).setMaxLines(1);
                alert_et_l.get(i).setWidth(1000);
                Log.w(LOG_TEXT,"Working 153");
              if(i<data_nS_lec[no].size())
                   alert_et_l.get(i).setText(data_nS_lec[no].get(i));
              else
                  alert_et_l.get(i).setText("");
                alert_et_l.get(i).setHint("Lecture "+(i+1));

                Log.w(LOG_TEXT,"Working 1535");

                ll.addView(alert_et_l.get(i));
            }

            ll.setOrientation(LinearLayout.VERTICAL);
            INT_TEMP=no;
            alert.setPositiveButton("Save",new DialogInterface.OnClickListener() {
                int id_no=INT_TEMP;
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    data_nS_lec[id_no].clear();
                    for(int i=0;i<data_nS_noL[id_no];i++) {

                        data_nS_lec[id_no].add(alert_et_l.get(i).getText().toString());
                    }
                        Toast.makeText(getApplicationContext(),"Saved!",Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Nothing
                }
            });
            alert.setView(sv);
            alert.show();

        }
        else {
            tv.setText("0");
            Toast.makeText(getApplicationContext(),"Only Positive Number Allowed",Toast.LENGTH_SHORT).show();
        }

    }
    public static void init_Fields()
    {
        et_data_name.setText(data_name);
    }
    public static void updateFields()
    {
        DateFormatSymbols dfs=new DateFormatSymbols();

        if(data_date_s_sem_d<=0 || data_date_s_sem_m<1)
            et_date_s_sem.setText("Not Set");
        else
            et_date_s_sem.setText( String.format("%02d",data_date_s_sem_d)+" "+ dfs.getMonths()[data_date_s_sem_m-1] + " " +data_date_s_sem_y);
        if(data_date_e_sem_d<=0 || data_date_e_sem_m<1)
            et_date_e_sem.setText("Not Set");
        else
            et_date_e_sem.setText(String.format("%02d",data_date_e_sem_d)+" "+ dfs.getMonths()[data_date_e_sem_m-1] + " " +data_date_e_sem_y);
        if(!isDateInit())
            Toast.makeText(activity,"Date Not Init.",Toast.LENGTH_SHORT).show();
        else    if(!checkSEdate())
            Toast.makeText(activity, "Semester Duration Invalid : At least 1 Month long", Toast.LENGTH_SHORT).show();


    }

    public void save_setting(View v)
    {
        if(v.getId()!=R.id.id_intro_done) return;

        data_name=et_data_name.getText().toString();
        updateDataToDB();

        save_couse_list();
        finish();
        Intent in=new Intent(this,actiCouseTT.class);
        startActivity(in);


    }
    public static void makedata_course_list_relatedNotNull()
    {
        if(data_course_list==null)
            data_course_list=new ArrayList<String>();
        if(data_course_listID==null)
            data_course_listID=new ArrayList();
        data_course_listIDC=0;
        if (data_course_tt == null)
            data_course_tt = new List[7];
        for (int i = 0; i < 7; i++)
            if (data_course_tt[i] == null) data_course_tt[i] = new ArrayList();


    }
    public static  void init_couse_list()
    {
        makedata_course_list_relatedNotNull();
        b_course_list_lay.removeAllViews();
        data_course_listIDC=0;
       // Toast.makeText(activity,"Courses : "+data_course_list.size(),Toast.LENGTH_SHORT).show();
        for(int i=0;i<data_course_list.size();i++)
        {
            int id=(int)data_course_listID.get(i);
            b_course_list_lay.addView(makeElementAddCouse(data_course_list.get(i),id));
            if(data_course_listIDC<=id) data_course_listIDC=id+1;
        }


    }
    public void save_couse_list()
    {
        if(data_course_list==null)
            data_course_list=new ArrayList<String>();
        data_course_list.clear();
        if(data_course_listID==null)
            data_course_listID=new ArrayList();
        data_course_listID.clear();
        for(int i=0;i<b_course_list_lay.getChildCount();i++)
        {
            data_course_list.add(i,((EditText)(((LinearLayout)b_course_list_lay.getChildAt(i)).getChildAt(1))).getText().toString());
            data_course_listID.add(i,(((LinearLayout)b_course_list_lay.getChildAt(i)).getChildAt(1)).getTag());
        }
       // Toast.makeText(getApplicationContext(),"Courses : "+b_course_list_lay.getChildCount(),Toast.LENGTH_SHORT).show();

        b_course_list_lay.removeAllViews();


    }
    public static View makeElementAddCouse(String str)
    {
        return makeElementAddCouse(str,-1);
    }
    public static View makeElementAddCouse(String str,int id)
    {
        LinearLayout ll=new LinearLayout(activity);
        EditText et=new EditText(activity);
        et.setHint("Enter Name");
        et.setText(str);
        et.setWidth(1000);
        if(id==-1)
            et.setTag((int)(data_course_listIDC++));
        else
           et.setTag((int)id);

        Button b=new Button(activity);
        b.setText(" X ");
        GLOBAL_LL=ll;
        b.setOnClickListener(new View.OnClickListener() {
            LinearLayout myLay=GLOBAL_LL;
            @Override
            public void onClick(View v) {
                b_course_list_lay.removeView(myLay);
            }
        });
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.addView(b);
        ll.addView(et);
    return ll;
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        static int TMP_GLOBAL;
        static View root_view_global;
        String LOG_TEXT=PlaceholderFragment.class.getSimpleName();
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_information, container, false);
            root_view_global=rootView;
            Log.w(LOG_TEXT, "Information Fragment Started!!");
            et_date_s_sem=(EditText) rootView.findViewById(R.id.et_date_sem_start);
            et_date_e_sem=(EditText) rootView.findViewById(R.id.et_date_sem_end);
            et_data_name=(EditText) rootView.findViewById(R.id.et_info_name);

            iv_button_nS=new ImageView[7];
            iv_button_nS[0]= (ImageView) rootView.findViewById(R.id.id_nS_0);
            iv_button_nS[1]= (ImageView) rootView.findViewById(R.id.id_nS_1);
            iv_button_nS[2]= (ImageView) rootView.findViewById(R.id.id_nS_2);
            iv_button_nS[3]= (ImageView) rootView.findViewById(R.id.id_nS_3);
            iv_button_nS[4]= (ImageView) rootView.findViewById(R.id.id_nS_4);
            iv_button_nS[5]= (ImageView) rootView.findViewById(R.id.id_nS_5);
            iv_button_nS[6]= (ImageView) rootView.findViewById(R.id.id_nS_6);

            b_course_add=(Button) rootView.findViewById(R.id.id_info_couse_add);
            b_course_list_lay=(LinearLayout) rootView.findViewById(R.id.id_info_course_list);

            for(int i=0;i<7;i++)
                if(data_nS_lec[i]==null)
                    data_nS_lec[i]=new ArrayList<String>();         //Init List of Array of lecture Names



            for (int i=0;i<7;i++) {
                TMP_GLOBAL=i;
                iv_button_nS[i].setOnClickListener(new View.OnClickListener() {
                    int id_no=PlaceholderFragment.TMP_GLOBAL;

                    @Override
                    public void onClick(View v) {
                        if( data_nS[id_no])
                            data_nS[id_no]=false;
                        else
                            data_nS[id_no]=true;
                        updateNSImage();


                        Toast.makeText(getActivity(), "Clicked "+(id_no+1), Toast.LENGTH_SHORT).show();
                        //          Toast.makeText(getActivity(), "Clicked "+(id_no+1), Toast.LENGTH_SHORT);
                    }
                });
            }
            init_Fields();
            updateFields();
            updateNSImage();
            init_couse_list();


            b_course_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    b_course_list_lay.addView(makeElementAddCouse(""));

                }
            });

            return rootView;
        }
    }
}
