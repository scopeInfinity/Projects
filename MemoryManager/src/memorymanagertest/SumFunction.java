package memorymanagertest;

import memorymanager.FunctionCall;

/**
 *
 * @author scopeinfinity
 */
public class SumFunction extends FunctionCall{
    
    SumFunction(Object... args) {
        super(args);
    }
    
    @Override
    public void execute() {
        int argc = countArguments();
        int x = getArgument(0);
        int y = getArgument(1);
        setResult(x+y);
        //memorymanager.MemoryManager.getInstance().printMemory();
    }
}
