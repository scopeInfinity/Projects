package memorymanagertest;

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
        if(x==0) {
            setResult(1);
        }
        else {
            int res = x * (int)(new Factorial(new Object[]{x-1}).getResult());
            setResult(res);
            if(x==3) {
                //memorymanager.MemoryManager.getInstance().printMemory();
            }
        }
    }
}

