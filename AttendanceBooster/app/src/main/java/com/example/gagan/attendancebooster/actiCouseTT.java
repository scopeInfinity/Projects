package com.example.gagan.attendancebooster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class actiCouseTT extends ActionBarActivity {
    static String LOG_TEXT=actiCouseTT.class.getSimpleName();
    LinearLayout ll_course_ll;
    LinearLayout GLOBALTEMP_LL;
    LinearLayout llInner[];
    int GLOBAL_INT;
    String days[]={"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acti_couse_tt);


        Log.w(LOG_TEXT, "STARTED course Activity");
        ll_course_ll=(LinearLayout) findViewById(R.id.id_LinearL_course);

        Log.w(LOG_TEXT,"Step 1");
        ll_course_ll.removeAllViews();

        llInner=new LinearLayout[7];
        for(int i=0;i<7;i++)
        {
            final LinearLayout ll=new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            TextView tx=new TextView(this);
            tx.setText(days[i]);

            llInner[i]=new LinearLayout(this);
            llInner[i].setOrientation(LinearLayout.VERTICAL);
            final LinearLayout myLL=llInner[i];

            final int myday=i;
            Button b=new Button(this);
            b.setText("Add");
            b.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {

                    addCourseMenu(myday,llInner[myday]);



                }
            });
            ll.addView(b);
            ll.addView(tx);
            ScrollView sv=new ScrollView(this);
            sv.addView(llInner[i]);
            ll.addView(sv);
            ll_course_ll.addView(ll);

        }


        Button bsave=(Button) findViewById(R.id.id_b_actiSave);
        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveActiCourse();
            }
        });
        initActiCourse();
    }

    public void addCourseMenu(int day,LinearLayout llist)
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(this);
        alert.setTitle(days[day]).setMessage("Choose Lecture");
        final LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        final int l=Information.data_course_list.size();
        final CheckBox rb[]=new CheckBox[l];
        for(int i=0;i<l;i++) {
            rb[i]=new CheckBox(this);
            rb[i].setText((String)Information.data_course_list.get(i));
            ll.addView(rb[i]);
        }
        final int d=day;
        final LinearLayout lll=llist;
        alert.setPositiveButton("Add",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i=0;i<l;i++)
                if(rb[i].isChecked())
                {
                        addCourse(d,(int)Information.data_course_listID.get(i),lll);   //idth couse to add
                }
            }
        });
        alert.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alert.setView(ll);
        alert.show();

    }
    public void addCourse(int day,int id,final LinearLayout ll)
    {
        final FrameLayout fl=new FrameLayout(this);
        final Button b=new Button(this);
        boolean flag=false;
        for(int i=0;i<Information.data_course_listID.size();i++)
        if(id==(int)Information.data_course_listID.get(i)){
            b.setText((String) Information.data_course_list.get(i));
            flag=true;
            break;
        }

        b.setPadding(3,1,3,3);
        fl.addView(b);
        fl.setBackgroundColor(Color.argb(255,50,255,50));

        if(!flag) return;
        b.setTag(id);
        ll.addView(fl);
        final actiCouseTT acti=this;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert=new AlertDialog.Builder(acti);
                alert.setTitle("Remove Course").setMessage((String)b.getText().toString());
                alert.setPositiveButton("Remove",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ll.removeView(fl);
                    }
                });
                alert.show();

            }
        });
     }

    public void makeDataCourseNotNull()
    {
        Log.w(LOG_TEXT, "ll_couse linked");
        if(Information.data_course_tt==null)
        {
            Information.data_course_tt=  new List[7];
            for(int i=0;i<7;i++)
                Information.data_course_tt[i]=new ArrayList();

        }

    }
    public void initActiCourse()
    {
        makeDataCourseNotNull();

        for(int i=0;i<7;i++)
        {
            llInner[i].removeAllViews();

          //  Toast.makeText(this,"Course on "+days[i]+" : "+Information.data_course_tt[i].size(),Toast.LENGTH_SHORT).show();
            for(int j=0;j<Information.data_course_tt[i].size();j++)
            {
                //Information.data_course_tt[i].add(((EditText)(llInner[i].getChildAt(i))).getTag());
                int id=(int)Information.data_course_tt[i].get(j);

                addCourse(i,id,llInner[i]);


            }
        }
    }
    public void saveActiCourse()
    {

        makeDataCourseNotNull();
        for(int i=0;i<7;i++)
        {
            Information.data_course_tt[i].clear();
            for(int j=0;j<llInner[i].getChildCount();j++)
                Information.data_course_tt[i].add((int)(((Button)((FrameLayout)((llInner[i].getChildAt(j)))).getChildAt(0)).getTag()));
           // Toast.makeText(this,"Course on "+days[i]+" : "+Information.data_course_tt[i].size(),Toast.LENGTH_SHORT).show();

        }
        Information.updateDataToDB();
    finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_acti_couse_tt, menu);
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
