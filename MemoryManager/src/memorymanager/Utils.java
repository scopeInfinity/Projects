package memorymanager;

public class Utils {
    
    public static int pushStringToHeap(String str){
        int address = MemoryManager.getInstance().malloc(str.length()+1);
        for (int i = 0; i < str.length(); i++) {
            MemoryManager.setValue(address+i, (int)str.charAt(i));
        }
        MemoryManager.setValue(address+str.length(), 0);
        return address;
    }
    
    public static String getStringFromHeap(int address){
        StringBuffer buffer = new StringBuffer();
        int i=0,val;
        while((val=MemoryManager.dereference(address+(i++)))!=0) {
            buffer.append((char)val);
        }
        return buffer.toString();
    }
}
