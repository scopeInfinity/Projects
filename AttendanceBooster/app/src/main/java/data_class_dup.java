import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gagan on 11/5/15.
 */
public class data_class_dup {
    String name;
    int sy=-1,sm=-1,sd=-1;
    int ey=-1,em=-1,ed=-1;

    List[] data_course_tt;//course id

    List<String> data_course_list;
    List data_course_listID;
    int data_course_listIDC;
    HashMap<Integer,HashMap> data_attend;

    data_class_dup(String NAME,int s_y,int s_m,int s_d,int e_y,int e_m,int e_d,List dctt[],List<String> dcl,List dclID,int dclIDC)
    {
        name=new String(NAME);
        sy=s_y;sm=s_m;sd=s_d;
        ey=e_y;em=e_m;ed=e_d;
        if(dctt!=null) {
            data_course_tt = new List[7];
            for (int i = 0; i <7;i++)
            {
                data_course_tt[i]=new ArrayList();
                /////Trying to do using hash
            }


        }
    }
}
