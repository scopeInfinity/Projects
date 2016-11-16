package memorymanager;

public class InsufficentMemoryExpection extends RuntimeException{

    public InsufficentMemoryExpection(String task) {
        super("Memory Insufficent in Creation of "+task);
    }    
}
