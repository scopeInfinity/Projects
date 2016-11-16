package memorymanagertest;

import memorymanager.FunctionCall;
import memorymanager.MemoryManager;

/**
 *
 * @author scopeinfinity
 */
public class SumFunctionPointers extends FunctionCall{
    
    SumFunctionPointers(Object... args) {
        super(args);
    }
    
    @Override
    public void execute() {
        int argc = countArguments();
        int x = MemoryManager.dereference( getArgument(0));
        int y = MemoryManager.dereference( getArgument(1));
        //Using Nested 
        int result = (int)  new SumFunction(new Object[]{x,y}).getResult();
        MemoryManager.setValue(getArgument(2), (result));
    }
}
