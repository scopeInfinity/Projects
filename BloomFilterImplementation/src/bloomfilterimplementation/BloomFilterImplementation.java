package bloomfilterimplementation;

import java.io.File;

public class BloomFilterImplementation {
    private static final String FNAME_PHONE = "phone.txt";
    private static final String FNAME_ZIP = "zip.txt";
    
    
    //Zip Side is Master
    private static boolean isPhoneSide;
    
    
    private static boolean loadFiles() throws Exception {
        File file = new File(FNAME_PHONE);
        if(file.exists()) {
            isPhoneSide = true;
            loadPhoneData(file);
            return true;
        }
        file = new File(FNAME_ZIP);
        if(file.exists()) {
            isPhoneSide = false;
            loadZipData(file);
            return true;
        }
        return false;
    }
    public static void main(String[] args) throws Exception{
        if(!loadFiles())
        {
            System.err.println("File "+FNAME_ZIP+" or "+FNAME_PHONE+" not found!!");
            return;
        }
    }

    private static void loadPhoneData(File file) throws Exception{
        System.out.println("Phone : Client Node");
        new ClientNode(file).test();
    }

    private static void loadZipData(File file)  throws Exception {
        System.out.println("Zip : Master Node");
        new MasterNode(file).test();
    }
    
}
