package memorymanager;

public abstract class FunctionCall {
    private MemoryManager manager;
    private boolean argumentsCleaned;
    
    //Arguments of Integers and Return Value Integer
    public FunctionCall(Object... args) {
        manager = MemoryManager.getInstance();
        for (int i = args.length-1; i>=0 ; i--) {
            //For now only push Integer
            manager.push((int)args[i]);
        }
        manager.push(args.length);
        argumentsCleaned = false;
        
        execute();
        cleanArguments();
    }
    
    public Object getResult() {
        Object returnValue = manager.pop();
        return returnValue;
    }
    
    public int countArguments() {
        int argc = manager.peekTop();
        return argc;
    }
    
    public int getArgument(int number) {
        int val = manager.getStackValue(number+1);
        return val;
    }
    
    public void cleanArguments() {
        if(argumentsCleaned)
            return;
        int argc = manager.pop();
        for(int i=0;i<argc;i++)
            manager.pop();
        argumentsCleaned = true;
    }
    
    public void setResult(Object result) {
        cleanArguments();
        manager.push((int) result);
    }
    
    public abstract void execute();
    
    
}
