/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bloomfilterimplementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author scopeinfinity
 */
public class ClientNode implements Serializable{
    private static final long serialVersionUID = 48415315165456451L;
    
    private  ArrayList<String> phone;
    private  ArrayList<Integer> ids;
    
    private Socket socket;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    
    int getSize() {
        return ids.size();
    }
    
    public ClientNode(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        Scanner scanner = new Scanner(file);
        ids = new ArrayList<>();
        phone = new ArrayList<>();
        
        while(scanner.hasNextInt()) {
            ids.add(scanner.nextInt());
            phone.add(scanner.nextLine());
        }
        System.out.println("Phone File Loaded! ("+ids.size()+")");
        Scanner scannerStd = new Scanner(System.in);
        System.out.print("Master IP : ");
        String  ip = scannerStd.nextLine();
        socket = new Socket(ip, MasterNode.PORT);
        
        bis = new BufferedInputStream(socket.getInputStream());
        bos = new BufferedOutputStream(socket.getOutputStream());
        
        System.out.println("Connected");
        
    }
    
    void test() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        BloomFilter bf = (BloomFilter) obj;
        System.out.println("Bloom Filter Recieved");
        System.out.println("> BloomFilter Size(bytes) : "+bf.BloomSizeByte);
        System.out.println("> No of Phone Record : "+ids.size());
        
        System.out.println("Filtering using Bloom Filter");
        
        ArrayList<String> _phone = new ArrayList<String>();
        ArrayList<Integer> _ids = new ArrayList<>();
        
        for (int i = 0; i < ids.size(); i++) {
            if(bf.check(ids.get(i))) {
                _ids.add(ids.get(i));
                _phone.add(phone.get(i));
            }
        }
        ids = _ids;
        phone = _phone;
        
        System.out.println("> No of Phone Record(Filtered) : "+ids.size());
        
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        System.out.println("Sending Filtered Records");
        oos.writeInt(ids.size());
        for (int i = 0; i < ids.size(); i++) {
            oos.writeInt(ids.get(i));
        }
        System.out.println("Sending Phone Numbers");
        for (int i = 0; i < ids.size(); i++) {
            oos.writeObject(phone.get(i));
        }
        oos.flush();
        System.out.println("Sending Data Done");
        
        
        
    }
    
    
    
}
