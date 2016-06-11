/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathfindersolver;

/**
 *
 * @author Gagan
 */
public class generatePath {
    
    PathFinderSolver PFS;
    static boolean A[][];
    static int mDis[][],M,N;
    static boolean mDisOneTimeMin[][];
    static coordinate start,end;
   
    void init(boolean a[][],coordinate S,coordinate E,int m,int n,PathFinderSolver pfs)
    {   PFS=pfs;
        start=S;
        end=E;
        A=a;
        M=m;
        N=n;
          coordinate.minX=0;
        coordinate.minY=0;
        coordinate.maxX=N-1;
        coordinate.maxY=M-1;
      
        mDis=new int[M][N];
    mDisOneTimeMin=new boolean[M][N];
        for(int i=0;i<M;i++)
            for(int j=0;j<N;j++)
                mDis[i][j]=Integer.MAX_VALUE;
        mDis[start.getY()][start.getX()]=0;
       
        System.out.println(start+" > "+end);
        pathLastNode.init(M,N);
        
   /*        for(int i=0;i<M;i++)
        {
            for(int j=0;j<N;j++)
            {
                if(A[i][j]) System.out.print('|'); else System.out.print(' ');
                                
            }
        System.out.println();
        }
     */
    }
    void findpath()
    {

        coordinate u=new coordinate();
        while(true)
        {
            u=extractMin();
            if(!u.isValid()) break;
            RELAX(u,u.retRC(0,-1));
            RELAX(u,u.retRC(0,+1));
            RELAX(u,u.retRC(-1,0));
            RELAX(u,u.retRC(+1,0));
            
       
            mDisOneTimeMin[u.getY()][u.getX()]=true;
            
        }
        
        System.out.println("Minimum Path(REV): ");
        u=end;
        
        int map[][]=new int[M][N];
        try{
            map[u.getY()][u.getX()]=3;//end
            PFS.addPath(u);
     }catch(Exception e){System.out.println("END ERROR :"+u);}
       
        while(!u.compare(start))
        {
            System.out.print(u+" <= ");
            u=pathLastNode.getLastNode(u);
            if(!u.isValid()) break;
            map[u.getY()][u.getX()]=4;//path
            PFS.addPath(u);
    
        }
        if(u.isValid())
        {
            System.out.print(u);
        map[u.getY()][u.getX()]=2;//start
        PFS.addPath(u);
    
        }
        for(int i=0;i<M;i++)
            for(int j=0;j<N;j++)
            if(A[i][j])
            {
                map[i][j]=1;
            }//wall
        
        System.out.println("Path Found");
        /*for(int i=0;i<M;i++)
        {
            for(int j=0;j<N;j++)
            {
                switch(map[i][j])
                {
                    case 0:System.out.print(' ');break;
                    case 1:System.out.print('|');break;//\u25A0
                    case 4:System.out.print('+');break;//\u25A1
                    case 2:System.out.print('S');break;
                    case 3:System.out.print('E');break;
                        
                }
                
            }
        System.out.println();
        }
       */ 
    }
    static void RELAX(coordinate u,coordinate v)
    {       
    if(!v.isValid() ) return;
    if(A[v.getY()][v.getX()]) return;
    //System.out.println("RELAX :"+u+" "+v);
     if(mDis[v.getY()][v.getX()]>1+mDis[u.getY()][u.getX()])
        {
            mDis[v.getY()][v.getX()]=1+mDis[u.getY()][u.getX()];
            pathLastNode.setLastNode(v,u);
        }
    }
    static coordinate extractMin()
    {   
        coordinate c=new coordinate();
        int min=Integer.MAX_VALUE;
        for(int i=0;i<M;i++)
            for(int j=0;j<N;j++)
            if(!mDisOneTimeMin[i][j])
                if(min>mDis[i][j])
                {
                 min=mDis[i][j];
                 c.set(j, i);
                }
        return c;
    }
}
class coordinate
{
    static int maxX,minX,maxY,minY;
    int x,y;
    boolean isValid()
    {//System.out.println(x+" "+y+",  "+ maxX+' '+maxY );
        return (x>=minX && x<=maxX && y>=minY && y<=maxY);
    }
    coordinate()
    {
        x=-1;
        y=-1;
    }
    boolean compare(coordinate c)
    {
        if(c.getX()==x && c.getY()==y)
            return true;
        return false;
    }
    coordinate(int a,int b)
    {
        x=a;
        y=b;
    }
    void set(int a,int b)
    {
        x=a;
        y=b;
    }
    int getX()
    {
        return x;
    }
    int getY()
    {
        return y;
    }
    coordinate(coordinate c)
    {
        x=c.getX();
        y=c.getY();
    }
    coordinate retRC(int a,int b)//Return after relative change
    {   
        coordinate c2=new coordinate(this);
        c2.set(c2.getX()-a, c2.getY()-b);
        return c2;
    }
    public String toString()
    {
        return "("+x+","+y+")";
    }
}
class pathLastNode
{
    static coordinate last[][];
    static int M,N;
    
    static void init(int m,int n)
    {
        M=m;N=n;
        last=new coordinate[M][N];
    }
    static coordinate getLastNode(coordinate c)
    {
        if(c.isValid())
        return last[c.getY()][c.getX()];
        else
        return new coordinate(); 
    }
    static void setLastNode(coordinate c,coordinate c2)
    {
        last[c.getY()][c.getX()]=c2;
    }
}