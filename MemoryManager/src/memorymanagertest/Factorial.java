package memorymanagertest;

import java.util.logging.Level;
import java.util.logging.Logger;
import memorymanager.FunctionCall;

/**
 *
 * @author scopeinfinity
 */
public class Factorial extends FunctionCall{
       
    Factorial(Object... args) {
        super(args);
    }
    
    @Override
    public void execute() {
        int argc = countArguments();
        int x = getArgument(0);
        animation();
        if(x==0) {
            setResult(1);
        }
        else {
            int res = x * (int)(new Factorial(new Object[]{x-1}).getResult());
            setResult(res);
        }
        animation();
    }
    
    void animation() {
        memorymanager.MemoryManager.getInstance().printMemory();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }
    }
}

