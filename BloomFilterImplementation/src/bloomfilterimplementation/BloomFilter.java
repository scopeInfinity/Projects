package bloomfilterimplementation;

//Current Implementation for 2 Hash Functions

import java.io.Serializable;

public class BloomFilter implements Serializable {
    private static final long serialVersionUID = 484153151651315315L;
    
    private final int BloomSize = 1000000;
    public int BloomSizeByte = 31250;
    
    private int filter[];

    public BloomFilter() {
        BloomSizeByte = (BloomSize)/32+1;
    }

    public void clear() {
        filter = new int[BloomSizeByte];
    }
    
    public boolean check(int num) {
        if(getBit(getHash1(num)) && getBit(getHash2(num)))
            return true;
        return false;
    }
    
    public void add(int num) {
        setBit(getHash1(num));
        setBit(getHash2(num));
    }
    
    // BitArray Implementation
    private boolean getBit(int index) {
        int offset = index/32,bit = index%32;
        if((filter[offset]&(1<<bit))!=0)
            return true;
        return false;
    }
    
    private void setBit(int index) {
        int offset = index/32,bit = index%32;
        filter[offset]|=(1<<bit);
    }
    
    
    // Hashing
    private final long Prime1 = 269979449;
    private final long Prime2 = 104949401;
    private final long Prime3 = (long)(1e9+7);
    
    private int getHash1(int num){
        return (int)((Prime1*num+Prime2)%BloomSize);
    }
    
    private int getHash2(int num){
        return (int)((Prime2*num+Prime1)%BloomSize);
    }
}
