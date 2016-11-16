package memorymanagertest;

import memorymanager.FunctionCall;
import memorymanager.MemoryManager;
import memorymanager.Utils;

public class MemoryManagerTest {
    final MemoryManager mmanager;

    public MemoryManagerTest() {
        mmanager = MemoryManager.getInstance();
    }
    
    public void test() {
       
        System.out.println("Starting Memory Manager Testing");
        System.out.println("===============================");
        System.out.println("\nSum Call 10 and 20");
        int output1 = (int)  new SumFunction(new Object[]{10,20}).getResult();
        System.out.println("Sum is : "+(output1));
        
        
        System.out.println("\nString Reverse Example");
        String data = "Hello";
        System.out.println("Original Sting : "+data);
        int datap = Utils.pushStringToHeap(data);
        int length = (int) new StringReverse(new Object[]{datap}).getResult();
        String newdata = Utils.getStringFromHeap(datap);
        System.out.println("Reverse["+length+"] : "+newdata);
        
        
        System.out.println("\nSum Pointer Call 10 and 25");
        int num1 = mmanager.malloc(1);
        int num2 = mmanager.malloc(1);
        int ret = mmanager.malloc(1);
        mmanager.setValue(num1, 10);
        mmanager.setValue(num2, 25);
        
        new SumFunctionPointers(new Object[]{num1,num2,ret});
        int output2 = MemoryManager.dereference(ret);
        System.out.println("New Sum is : "+(output2));
        
        mmanager.free(datap);
        
        mmanager.printMemory();
        
        mmanager.free(ret);
        mmanager.free(num1);
        mmanager.free(num2);
        
        
        System.out.println("\nFactorial of 5");
        int output3 = (int)  new Factorial(new Object[]{5}).getResult();
        System.out.println("\t5! = "+(output3));
        
        
        
    }
    
   
    public static void main(String[] args) {
        new MemoryManagerTest().test();
    }
}
