package memorymanager;

public class MemoryManager {
    private final boolean doAssertion = true;
    
    private static final int SIZE = 20;//1024*1024*20;
    private final int RAW[];
    private final boolean ASSERT_HEAP[];
    
    private static MemoryManager instance;

    public static MemoryManager getInstance() {
        if(instance == null)
            instance = new MemoryManager();
        return instance;
    }

    public MemoryManager() {
        RAW = new int[SIZE];
        if(doAssertion)
            ASSERT_HEAP = new boolean[SIZE];
        resetStack();
        resetHeap();
    }
    
    
    //Stack
    private int sTop;
    private void resetStack() {
        sTop = SIZE;
    }
    
    /**
     * Push into Stack
     * @param val
     * @return stopTop
     */
    public int push(int val) {
        //Check for Memory Available Later
        if(sTop-1<0)
            throw new InsufficentMemoryExpection("Stack");
        sTop-=1;
        RAW[sTop] = val;
        return sTop;
    }
    
    /**
     * Peek into Stack
     * @param val
     * @return stopTop
     */
    public int peekTop() {
        //Check for Memory Available Later
        if(sTop<0 || sTop>=SIZE)
            throw new InvalidMemoryExcpetion("Stack");
        return RAW[sTop];
    }
    
    /**
     * Deference Value, offset from ESP
     * @param address
     * @return value
     */
    public int getStackValue(int offset) {
        int address = sTop+offset;
        if(address<0 || address>=SIZE)
            throw new InvalidMemoryExcpetion("Stack");
        return RAW[address];
    }
    
    /**
     * Pop from Stack
     * @return stackTopValue
     */
    public int pop() {
        //Check for Memory Available Later
        if(sTop+1>SIZE)
            throw new InvalidMemoryExcpetion();
        sTop++;
        return RAW[sTop-1];
    }
    
    
    
    
    
    //Heap
    //Free Pointer at 0th Index
    private void resetHeap() {
        //stack must be Reset Earlier
        RAW[0]=1;
        //Free Size including Size Word itself
        RAW[1]=sTop-1;
        RAW[2]=-1;
    }
    
    /**
     * Excluding Return value
     */
    private int getMaxHeapPossible() {
        return sTop;
    }
    /**
     * Free the memory
     * @param address 
     */
    public void free(int address) {
        int pointer2fp = 0;
        int freepointer = RAW[0];
        while(freepointer!=-1) {
            if(freepointer +1 >= address) {
                break;
            }
            
            pointer2fp = freepointer + 1;
            freepointer = RAW[freepointer + 1];
        }
        
        RAW[pointer2fp] = address - 1;
        RAW[address] = freepointer;
        
        //For Assestion
        if(doAssertion)
        for (int i = 0; i < RAW[address - 1] - 1; i++) {
            if(!ASSERT_HEAP[address + i]) {
                printMemory();
                throw new InvalidMemoryExcpetion("Memory Manager Fault : Already Free");
            }
            ASSERT_HEAP[address + i] = false;
        }
        
        //Join With Next Free Block
        if(freepointer == address - 1 + RAW[address-1]) {
            RAW[address-1]+=RAW[freepointer];
            RAW[address] = RAW[freepointer + 1];
        }
        
        //Join With Previous Free Block
        //Excluding last block
        int previousFreeBlockLast;
        if(pointer2fp==0)
            //Its already fine
            return;
        previousFreeBlockLast = pointer2fp - 1 + RAW[pointer2fp-1];
        if(address - 1 == previousFreeBlockLast) {
            //Continous
            RAW[pointer2fp-1]+=RAW[address - 1];
            RAW[pointer2fp] = freepointer;
        }
    }
    
     /**
     * Allocate the memory
     * @param amount In term of words, 4 byte each 
     * @return pointerOfFreeMemory
     */
    public int malloc(int amount) {
        
        //Need 1 more for storing size than required
        amount++;
        
        //We allocate in multiple of 4
        int pointer2fp = 0;
        int freepointer = RAW[0];
        while(freepointer!=-1 ||
                !(freepointer+1<getMaxHeapPossible()
                    && RAW[freepointer+1]+freepointer>getMaxHeapPossible())) {
            int canallocate = RAW[freepointer];
            //BEST FIT
            if(canallocate>=amount) {
                int remaining = canallocate-amount;
                
                //We wont leave just 4 byte in heap free,
                //We need 8byte min.. if they are free
                if(remaining==1)
                {
                    amount++;
                    remaining = 0;
                }
                RAW[freepointer] = amount;
                int nextfreeword;
                if (remaining == 0)
                    nextfreeword = RAW[freepointer+1];
                else
                {
                    nextfreeword = freepointer+amount;
                    RAW[nextfreeword] = canallocate-amount;
                    RAW[nextfreeword+1] = RAW[freepointer+1];
                }
                
                //For Assertion
                if(doAssertion)
                for (int i = 0;i<amount - 1;i++) {
                    if(ASSERT_HEAP[freepointer + i +1]) {
                        printMemory();
                        throw new InvalidMemoryExcpetion("Memory Manager Fault : Already Allocated");
                    }
                    ASSERT_HEAP[freepointer + i + 1] = true;
                }
                
                RAW[pointer2fp] = nextfreeword;
                return freepointer + 1;
                
                
            }
            
            pointer2fp = freepointer + 1;
            freepointer = RAW[freepointer + 1];
        }
        
        throw new InsufficentMemoryExpection("Heap");
    }
    
    /**
     * Check if heap address is not free
     * @param address 
     * @return value
     */
    private int assertHeap(int address) {
        if(doAssertion && !ASSERT_HEAP[address]) {
            throw new InvalidMemoryExcpetion("Heap Invalid Derefence");
        }
        return RAW[address];
    }
    
     /**
     * Check if heap address is not free
     * @param address 
     * @param value
     */
    private void assertSetHeapValue(int address, int value) {
        if(doAssertion && !ASSERT_HEAP[address]) {
            throw new InvalidMemoryExcpetion("Heap Invalid Derefence");
        }
        RAW[address] = value;
    }
    
    /**
     * Deference Value at Address allocated using Heap
     * @param address
     * @return value
     */
    public static int dereference(int address) {
        MemoryManager manager = getInstance();
        return manager.assertHeap(address);
    }
    
    /**
     * Assign value in memory allocated by heap
     * @param address
     * @param value 
     */
    public static void setValue(int address, int value) {
        MemoryManager manager = getInstance();
        manager.assertSetHeapValue(address, value);
    }
    
    /**
     * Print Memory for debugging Purpose
     */
    public void printMemory() {
        System.out.println("\n======");
        System.out.println("Memory\n");
        System.out.printf("\tAddress \t   Decimal \t     Hex\n");
        for (int i = SIZE - 1; i >= 0; i--) {
            System.out.printf("\t%08d\t%10d\t%8x", i,RAW[i],RAW[i]);
            if(i>=sTop)
                System.out.print("\tS");
            else if(doAssertion && ASSERT_HEAP[i])
                System.out.print("\tU");
            
            System.out.println();
        }
        System.out.println();
        System.out.flush();
        
    }
}
