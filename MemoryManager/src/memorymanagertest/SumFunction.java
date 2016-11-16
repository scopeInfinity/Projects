/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
