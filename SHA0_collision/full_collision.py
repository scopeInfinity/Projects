'''
Based on pertubation vector and conditions on ai and mi
find the message m.
Complexity : 0(2^32 * 80)

Parallelized to improved Runtime

Executed on 4 clusters each having 60+ cores
python full_collision.py 128  0  32
python full_collision.py 128 32  64
python full_collision.py 128 64  96
python full_collision.py 128 96 128

Execution Time ~ 1 hr
'''

import os
import json
import sys
os.system("mkdir -p states")

IV = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]
disturbance_str = '11010000101010100111011010100001001010000000110000000100101011010001100000000000'
disturbance = [1 if x == '1' else 0 for x in disturbance_str]

def print_hash(HH):
    return '%08x%08x%08x%08x%08x' % (HH[0],HH[1],HH[2],HH[3],HH[4])

def _left_rotate(n, b):
    return ((n << b) | (n >> (32 - b))) & 0xffffffff
    

def sha0_word_generation(w2):
    w=[]
    w.extend(w2)
    assert len(w)==16
    for j in range(16, 80):
        w.append( w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16] )
    return w

def sha0_singleblock_diff(w,  HH) :
    (h0, h1, h2, h3, h4) = HH
    a = h0
    b = h1
    c = h2
    d = h3
    e = h4
    all_ = []
    # print("{:34} {:34} {:34} {:34} {:34}".format(ff(a),ff(b),ff(c),ff(d),ff(e)))
    for i in range(80):
        if 0 <= i <= 19:
            # Use alternative 1 for f from FIPS PB 180-1 to avoid ~
            f = d ^ (b & (c ^ d))
            k = 0x5A827999
        elif 20 <= i <= 39:
            f = b ^ c ^ d
            k = 0x6ED9EBA1
        elif 40 <= i <= 59:
            f = (b & c) | (b & d) | (c & d) 
            k = 0x8F1BBCDC
        elif 60 <= i <= 79:
            f = b ^ c ^ d
            k = 0xCA62C1D6

        a, b, c, d, e = ((_left_rotate(a, 5) + f + e + k + w[i]) & 0xffffffff, 
                        a, _left_rotate(b, 30), c, d)
        all_.append((a,b,c,d,e))
    h0 = (h0 + a) & 0xffffffff
    h1 = (h1 + b) & 0xffffffff 
    h2 = (h2 + c) & 0xffffffff
    h3 = (h3 + d) & 0xffffffff
    h4 = (h4 + e) & 0xffffffff
    
    return (h0, h1, h2, h3, h4),all_

def test_pattern(x,pat,_a_):
    for i in range(len(pat)):
        if pat[i]=='-':
            pass
        if pat[i]=='a':
            if not ((x&(1<<i))>0 )== _a_:
                return False
        if pat[i]=='A':
            if ((x&(1<<i))>0) == _a_:
                return False
        if pat[i]=='1':
            if not (x&(1<<i))>0:
                return False  
        if pat[i]=='0':
            if (x&(1<<i))>0:
                return False
    return True

def test_bit(a_,b_, _a_):
    b0='----------------------a-----a-A-'
    if not test_pattern(b_[0], b0, _a_):
        return False

    z1=["--------","1-------","0-0-----","1-1--aaa","1----a-0","0------0","-------0","0------1","--------","1-------","--------","0-------","0-------","--------","1-------","0-------","1-------","0-------","1-------","--------","--------"]
    z2=["1-1100-1","0a0011a0","--011111","aa0-0011","11111000","-0001001","-1011110","a1011111","-1000000","---00000","---11111","--------","--------","--------","--------","--------","--------","--------","--------","--------","--------"]
    z3=["--1--1-1","aa1-10a0","111111-1","0010101-","--1111-0","00100-0-","010-100-","0100--00","00000-11","0011001-","1111111-","------0-","--------","--------","--------","--------","--------","--------","--------","--------","--------"]
    z4=["10------","11----1-","0010a---","-010100-","110011--","01-01---","-0--100-","0----10-","1---1---","----0---","------0-","----1---","0---0---","1---0-0-","----1---","----1-1-","----0---","------1-","--------","--------","--------"]
    
    for i in range(20):
        if not test_pattern(a_[i],z1[i]+z2[i]+z3[i]+z4[i]):
            return False
    return True

def test_condition_local_collision_word(w,i, j=1 ):
    print(w[i])
    print(w[i+1])

    assert (w[i]&(1<<j)>0) != (w[i+1]&(1<<(j+5))>0)
    assert (w[i]&(1<<j)>0) != (w[i+2]&(1<<j)>0)

def print_differential(a1,a2):
    for j in range(80):
        print(bin(a1[j][0]^a2[j][0])+" "+bin(a1[j][1]^a2[j][1])+" "+bin(a1[j][2]^a2[j][2])+" "+bin(a1[j][3]^a2[j][3])+" "+bin(a1[j][4]^a2[j][4])+" ")
    
def local_collision():
    j=1
    _word80 = [ 0, 1<<6, 1<<1 ]
    _word80.extend( [0] * 78)
    cc = [1<<j,1<<(j+5),1<<j,1<<(30+j),1<<(30+j),1<<(30+j)]
    pb='1'+(''.join(['0'] * 79))
    changes=[0]*(80)
    for i in range(80):
        if pb[i]=='1':
            for j in range(len(cc)):
                changes[i+j]^=cc[j]
    w1 = []
    for i,x in enumerate(range(80)):
        w1.append(_word80[i])
    w2 = []
    for i,x in enumerate(range(80)):
        w2.append(_word80[i]^changes[i])
    for i in range(80):
        if pb[i]=='1':
            test_condition_local_collision_word(w1,i)
            test_condition_local_collision_word(w2,i)
    
    result1,a1 = sha0_singleblock_diff(w1, IV)
    result2,a2 = sha0_singleblock_diff(w2, IV)
    print(result1)
    print(result2)

    print_differential(a1,a2)

def getbit(num,pos):
    return (num&(1<<pos))>0

def test_condition(m, disturbance):
    assert len(m) == 80
    skip_last = 5
    assert(sum(disturbance[-5:]) == 0)
    for i in range(80-skip_last):
        if disturbance[i]:
            if not (getbit(m[i+1], 6) != getbit(m[i], 1)): # Cond 1
                return False
            if i>=40 and i<60: # Round 3
                if not (getbit(m[i+2], 1) != getbit(m[i], 1)): # Cond 2
                    return False
    hash_,all_ = sha0_singleblock_diff(m, IV)
    # TODO: 9 conditions remaining
    # if (not test_bit(all_[0], all_[1], False)) and (not test_bit(all_[0], all_[1], True)):
    #     return False
    return True

def load_state_find_message(find_message_fname, start_i):
    results = []
    i1 = start_i
    i6 = 0
    if os.path.exists(find_message_fname):
        [results,i1,i6] = json.load(open(find_message_fname))
        print("Loaded from Cache %s" % find_message_fname)
    return (results,i1,i6)

def save_state_find_message(_in, find_message_fname):
    [results,i1,i6] = _in
    json.dump([results,i1,i6],open(find_message_fname,'w'))

def find_message(_id, start_i, end_i):
    end_i = min(end_i,1<<16)
    find_message_fname = "states/state_find_message_%d.json"%_id
    print("Find Message Executed for ID %d"%_id)
    w = [0] * 16
    (results,i1,i6) = load_state_find_message(find_message_fname, start_i)    
    print("Disturbance Vector")
    print(disturbance)
    assert len(disturbance) == 80
    counter = 0
    try:
        while i1 < end_i:
            while i6 < (1<<16):
                for j in range(16):
                    w[j] = 0
                    if getbit(i1,j):
                        w[j]|=1<<1
                    if getbit(i6,j):
                        w[j]|=1<<6
                counter += 1
                m = sha0_word_generation(w)
                if test_condition(m, disturbance):
                    results.append(w)
                    print(w)
                i6 += 1
            i6 = 0
            i1 += 1
            print("%3d: %2.4f%% done" % (_id, (i1-start_i) *100.0/(end_i - start_i)))
            save_state_find_message((results,i1,i6),find_message_fname)
    except KeyboardInterrupt as e:
        print("Saving State")
        save_state_find_message((results,i1,i6),find_message_fname)
    print("Counter: %d"%counter)
    return results

'''
# Notes
h1 has 14 conditions on a0,b0
M0 found with complexity of 2^14
M1,M1_ found with complexity of 2^39
'''

def full_collision():
    h0 = IV
    print("h0 : %s"%print_hash(h0))
    m0 =[   0xefc04714,
            0xab491d35,
            0xbd45f430,
            0xfd07cb8f,
            0xcace957f,
            0xdc873c5c,
            0x7e905ef5,
            0xb08afc59,
            0x488377ce,
            0xe9ff32fa,
            0xfc7321f5,
            0x4f9e5c9e,
            0x08dd0c3c,
            0x7884fe76,
            0x9096f489,
            0x55af0e5b,
            ]
    print("m0 : %s"%str(m0))
    h1,_ = sha0_singleblock_diff(sha0_word_generation(m0),h0)
    print("h1 : %s"%print_hash(h1))

    m1 =[   0xe2c2cfbd,
            0xbd71e031,
            0x58fc4572,
            0xd0b7df9c,
            0x34b3e503,
            0x0e358632,
            0xd96cb552,
            0x6ecbe1ee,
            0x1e88ccc6,
            0x1e1df6d6,
            0x44a312a0,
            0x76be2131,
            0x47ddf448,
            0xd5fc3baa,
            0xbf320328,
            0xc1abbeb7,
        ]
    m1_ =[  0xe2c2cfbf,
            0x3d71e033,
            0x58fc4530,
            0x50b7dfde,
            0xb4b3e503,
            0x0e358672,
            0xd96cb550,
            0x6ecbe1ee,
            0x9e88ccc6,
            0x9e1df6d4,
            0x44a312e0,
            0x76be2131,
            0xc7ddf408,
            0x55fc3baa,
            0xbf320368,
            0x41abbeb7,
        ]
    h2,_ = sha0_singleblock_diff(sha0_word_generation(m1),h1)
    print("h2  : %s"%print_hash(h2))
    h2_,_ = sha0_singleblock_diff(sha0_word_generation(m1_),h1)
    print("h2_ : %s"%print_hash(h2_))


# full_collision()

# local_collision()
import multiprocessing
def schedule_jobs(procs= 32, s_p = 0, e_p = 32):
    max_i = 1<<16
    print("Procs %d" % procs)
    print("s_p %d" % s_p)
    print("e_p %d" % e_p)

    process_size = (max_i+procs-1)/procs # Ceil
    jobs = []
    for i in range(s_p, e_p):
        out_list = list()
        process = multiprocessing.Process(target=find_message,
                                          args=(i, process_size*i, process_size*(i+1)))
        jobs.append(process)

    for j in jobs:
        j.start()

    for j in jobs:
        j.join()

    print("Program Completed")
    
def main():
    find_message(0,0,1<<16)
    # schedule_jobs()


if __name__ == '__main__':
    if len(sys.argv) == 3 and sys.argv[1] == "-t":
        import ast
        m = ast.literal_eval(sys.argv[2])
        print "Test Results"
        print test_condition(sha0_word_generation(m), disturbance)
    if len(sys.argv) == 4:
        schedule_jobs(int(sys.argv[1]), int(sys.argv[2]), int(sys.argv[3]))
    
