package bloomfilterimplementation;

//Zip Side

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.effect.Blend;

public class MasterNode {
    public static final int PORT = 1976;
    private ArrayList<Integer> zip;
    private ArrayList<Integer> ids;
    
    private Socket cliSocket;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    
    
    public MasterNode(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(file);
        ids = new ArrayList<>();
        zip = new ArrayList<>();
        
        while(scanner.hasNextInt()) {
            ids.add(scanner.nextInt());
            zip.add(scanner.nextInt());
        }
        
        System.out.println("Zip File Loaded!");
        ServerSocket ss = new ServerSocket(PORT);
        cliSocket = ss.accept();
        bis = new BufferedInputStream(cliSocket.getInputStream());
        bos = new BufferedOutputStream(cliSocket.getOutputStream());
        
        System.out.println("Client Connected");
        System.out.println("> No of ZIPs : "+ids.size());
        
        
    }
    
    public void test() throws IOException, ClassNotFoundException {
        System.out.println();
        BloomFilter bf = new BloomFilter();
        bf.clear();
        
        for (Integer z : ids) {
            bf.add(z);
        }
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(bf);
        oos.flush();
        System.out.println("Bloom Filter Send");
        System.out.println("> BloomFilter Size(bytes) : "+bf.BloomSizeByte);
        System.out.println();
        
        ObjectInputStream ois = new ObjectInputStream(bis);
        int sz = ois.readInt();
        ArrayList<String> cphone = new ArrayList<>();
        ArrayList<Integer> cids = new ArrayList<>();
        System.out.println("Getting IDS");
        for (int i = 0; i < sz; i++) {
            cids.add(ois.readInt());
        }
        System.out.println("Getting Phone Number");
        for (int i = 0; i < sz; i++) {
            cphone.add((String)ois.readObject());
        }
        
        System.out.println("> Recieved After Filter");
        System.out.println("> Recieved No. Of Ids : "+sz);
        
        System.out.println();
        System.out.println("Performing Intersection");
        intersection(cids,cphone);
        
        
        
     
    }
    
    private void intersection(ArrayList<Integer> cids,ArrayList<String> cphone) throws FileNotFoundException, IOException {
        ArrayList<Integer> nzip = new ArrayList<>();
        ArrayList<String> nphone = new ArrayList<>();
        ArrayList<Integer> nids = new ArrayList<>();
        //For Simplicity
        for (int i = 0; i < cids.size(); i++) {
            for (int j = 0; j < ids.size(); j++) {
                if(ids.get(j).equals(cids.get(i))) {
                    nzip.add(zip.get(j));
                    nids.add(ids.get(j));
                    nphone.add(cphone.get(i));
                    break;
                }
            }
        }
        
        System.out.println();
        System.out.println("After Filtering");
        System.out.println("Rows after Join : "+nids.size());
        System.out.println();
        
        File file = new File("output.txt");
        BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(file));
        for (int i = 0; i < nids.size(); i++) {
            String l = nids.get(i)+ "\t"+nzip.get(i)+"\t"+nphone.get(i)+"\n";
            bos2.write(l.getBytes());
        }
        bos2.close();
    }
    
    
    
}
