package memorymanagertest;

import memorymanager.FunctionCall;
import memorymanager.MemoryManager;

/**
 *
 * @author scopeinfinity
 */
public class StringReverse extends FunctionCall{
    StringReverse(Object... args) {
        super(args);
    }
    
    @Override
    public void execute() {
//        System.out.println("For Working Inside String Reverse : Begin");
//        MemoryManager.getInstance().printMemory();
        int argc = countArguments();
        int address = getArgument(0);
        
        int l=0;
        while(MemoryManager.dereference(address+l)!=0)
            l++;
        for(int i=0,j=l-1;i<j;i++,j--)
        {
            int v1 = MemoryManager.dereference(address+i);
            int v2 = MemoryManager.dereference(address+j);
            MemoryManager.setValue(address+i, v2);
            MemoryManager.setValue(address+j, v1);
        }
        setResult(l);
//        System.out.println("For Working Inside String Reverse : END");
//        MemoryManager.getInstance().printMemory();
        
    }
}
