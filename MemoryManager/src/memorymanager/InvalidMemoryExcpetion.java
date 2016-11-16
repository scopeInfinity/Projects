package memorymanager;

public class InvalidMemoryExcpetion extends RuntimeException {

    public InvalidMemoryExcpetion() {
        super("Tring to Access Invalid Memory Location");
    }

    public InvalidMemoryExcpetion(String msg) {
       super(msg);
    }
}
