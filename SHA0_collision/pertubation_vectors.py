from copy import copy
import os,json
pertubation_vectors_fname = "cache/pvf.json"
pertubation_vectorshm_fname = "cache/pvfhm.json"
def get_index_noerror(lst, ind):
	if ind<0:
		return 0
	return lst[ind]

def _left_rotate(n, b):
    return ((n << b) | (n >> (32 - b))) & 0xffffffff
    
def ff(x):
    return bin(x)[2:].zfill(32)

def print_str(disturbance_vector):
	for x in xrange(0,4):
		print("\t%2d-%2d : "%(x*20,(x+1)*20)+disturbance_vector[x*20:(x+1)*20])
def sha0_word_generation(w2):
	w=[]
	w.extend(w2)
	assert len(w)==16
	for j in range(16, 80):
		w.append( w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16] )
	return w

def sha0_singleblock_diff(w):

    IV = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]
    # Initialize variables:
    # IV = [0,0,0,0,0]
    (h0, h1, h2, h3, h4) = IV

    
    # Pre-processing:
    #original_byte_len = len(message)
    #original_bit_len = original_byte_len * 8
    # append the bit '1' to the message
    # message += b'\x80'
    
    # append 0 <= k < 512 bits '0', so that the resulting message length (in bits)
    #    is congruent to 448 (mod 512)
    # message += b'\x00' * ((56 - (original_byte_len + 1) % 64) % 64)
    
    # append length of message (before pre-processing), in bits, as 64-bit big-endian integer
    # message += struct.pack(b'>Q', original_bit_len)
    # Process the message in successive 512-bit chunks:
    # break message into 512-bit chunks
    # for i in range(0, len(message), 64):
    if True:
     #    w = [0] * 80
     #    # break chunk into sixteen 32-bit big-endian words w[i]
     #    for j in range(16):
	    # w[j] = 0 # My Message
     #        #w[j] = struct.unpack(b'>I', message[i + j*4:i + j*4 + 4])[0]
     #    # Extend the sixteen 32-bit words into eighty 32-bit words:
     #    for j in range(16, 80):
     #        w[j] = w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16]
    	# w[0]=1
        # Initialize hash value for this chunk:
        a = h0
        b = h1
        c = h2
        d = h3
        e = h4
    
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
            # print("{:34} {:34} {:34} {:34} {:34}".format(ff(a),ff(b),ff(c),ff(d),ff(e)))
    
        # sAdd this chunk's hash to result so far:
        h0 = (h0 + a) & 0xffffffff
        h1 = (h1 + b) & 0xffffffff 
        h2 = (h2 + c) & 0xffffffff
        h3 = (h3 + d) & 0xffffffff
        h4 = (h4 + e) & 0xffffffff
    
    # Produce the final hash value (big-endian):
    return '%08x%08x%08x%08x%08x' % (h0, h1, h2, h3, h4)


def sha0_singleblock_diff_xor(w):

    IV = [0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0]
    # Initialize variables:
    # IV = [0,0,0,0,0]
    (h0, h1, h2, h3, h4) = IV

    
    # Pre-processing:
    #original_byte_len = len(message)
    #original_bit_len = original_byte_len * 8
    # append the bit '1' to the message
    # message += b'\x80'
    
    # append 0 <= k < 512 bits '0', so that the resulting message length (in bits)
    #    is congruent to 448 (mod 512)
    # message += b'\x00' * ((56 - (original_byte_len + 1) % 64) % 64)
    
    # append length of message (before pre-processing), in bits, as 64-bit big-endian integer
    # message += struct.pack(b'>Q', original_bit_len)
    # Process the message in successive 512-bit chunks:
    # break message into 512-bit chunks
    # for i in range(0, len(message), 64):
    all_ = []
    if True:
     #    w = [0] * 80
     #    # break chunk into sixteen 32-bit big-endian words w[i]
     #    for j in range(16):
	    # w[j] = 0 # My Message
     #        #w[j] = struct.unpack(b'>I', message[i + j*4:i + j*4 + 4])[0]
     #    # Extend the sixteen 32-bit words into eighty 32-bit words:
     #    for j in range(16, 80):
     #        w[j] = w[j-3] ^ w[j-8] ^ w[j-14] ^ w[j-16]
    	# w[0]=1
        # Initialize hash value for this chunk:
        a = h0
        b = h1
        c = h2
        d = h3
        e = h4
    
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
    
            a, b, c, d, e = ((_left_rotate(a, 5) ^ f ^ e ^ k ^ w[i]) & 0xffffffff, 
                            a, _left_rotate(b, 30), c, d)
            all_.append((a,b,c,d,e))
            # print("{:34} {:34} {:34} {:34} {:34}".format(ff(a),ff(b),ff(c),ff(d),ff(e)))
    
        # sAdd this chunk's hash to result so far:
        h0 = (h0 ^ a) & 0xffffffff
        h1 = (h1 ^ b) & 0xffffffff 
        h2 = (h2 ^ c) & 0xffffffff
        h3 = (h3 ^ d) & 0xffffffff
        h4 = (h4 ^ e) & 0xffffffff
    
    # Produce the final hash value (big-endian):
    return ('%08x%08x%08x%08x%08x' % (h0, h1, h2, h3, h4),all_)

# Generate full disturbance vector from given 16bit head
def disturbance_vector(head, NEGATIVE_C = 0):
	x = copy(head)
	assert(len(x)==16+NEGATIVE_C)
	for j in xrange(16+NEGATIVE_C,80+NEGATIVE_C):
		x.append(get_index_noerror(x,j-3) ^ get_index_noerror(x,j-8)^ get_index_noerror(x,j-14) ^ get_index_noerror(x,j-16))
	return x


def condition_one(vector):
	return vector[-5:] == [0] * 5
		
	# return 	vector[11] == vector[3] ^ vector[8] and \
	# 		vector[12] == vector[4] ^ vector[9] and \
	# 		vector[13] == vector[5] ^ vector[10] and \
	# 		vector[14] == vector[0] ^ vector[3] ^ vector[6] ^ vector[8] and \
	# 		vector[15] == vector[1] ^ vector[4] ^ vector[7] ^ vector[9]

def condition_two(vector):
	return 	vector[6] == vector[0] + vector[1] + vector[2] + vector[4] and \
			vector[7] == vector[0] + vector[4] and \
			vector[8] == vector[0] + vector[1] + vector[5] and \
			vector[9] == vector[4] and \
			vector[10] == vector[0] + vector[5]


def find_pertubation_vector():
	# No Truncated local collision
	# No Consecutive pertubation in first 16 step
	# No Pertubation after step 74
	HBITS = 16
	for i in range(1<<HBITS):
		head = []
		for j in range(HBITS):
			head.append(1 if (i&(1<<j)) != 0 else 0)

		vector = disturbance_vector(head)
		# Test Vector
		good = True
		for j in range(HBITS):
			if vector[j] and vector[j+1]:
				good = False
				break
		if not good:
			continue
		if not condition_one(vector):
			continue
		if not condition_two(vector):
			continue
		yield vector

# print disturbance_vector([0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0])
def count_1s_after_16(vector):
	z = 0
	for x in xrange(16,80):
		z+=vector[x]
	return z

def arraytobin(vector):
	a=""
	for x in vector:
		a+=str(x)
	return a
def print_pertubation_vector_CHABAUD_JOUX():
	if os.path.exists(pertubation_vectors_fname):
		m = json.load(open(pertubation_vectors_fname))
		print("Loaded from Cache %s" % pertubation_vectors_fname)
	else:
		m = dict()
		for x in find_pertubation_vector():
			c = str(count_1s_after_16(x))
			if c not in m.keys():
				m[c]=[]
			m[c].append(arraytobin( x))
		json.dump(m,open(pertubation_vectors_fname,'w'))

	print("All Pertubation Vector according to CHABAUD and JOUX")
	for _k in m.keys():
		if _k == "0":
			continue
		print("Pertubation Vector with Hamming Distance %s"%_k)
		for j in range(len(m[_k])):
			print_str(m[_k][j])
			print ""

	# print json.dumps(m, indent=4, sort_keys=True)
	return m["27"]




# Final Display
pb1 = print_pertubation_vector_CHABAUD_JOUX()[0]
pb2 = print_pertubation_vector_CHABAUD_JOUX()[1]
pb = pb1
pb = "11110101101110001000101010000000001001000010000010000100101100000010001000010000"
pb = "00100010000000101111011000111000000101000100010010010011101100110000111110000000"
assert len(pb) == 80
assert len(pb1) == 80
assert len(pb2) == 80

print("Using %s" % pb1)

def test_pertubation_vector():
	print "Test Pertubation Vector"
	_word80 = sha0_word_generation([1]*16)
	w = []
	for i,x in enumerate(pb1):
		w.append(_word80[i]^(1 if x=='1' else 0))
	result = sha0_singleblock_diff(w)
	print result
	w = []
	for i,x in enumerate(pb2):
		w.append(_word80[i]^(1 if x=='1' else 0))
	result = sha0_singleblock_diff(w)
	print result
	# w = []
	# for i,x in enumerate(pb):
	# 	w.append(_word80[i]^(2 if x=='1' else 0))
	# result = sha0_singleblock_diff(w)
	# print result

test_pertubation_vector()

def neutral_bit():
	print ""
	print("Using Neutral Bit Technique")
	print("\tBits After first 17bits")
	print("\t%s"%pb[17:])
	print("\tor")
	pb_strip = pb[17:].lstrip('0')[:-5]
	print("\t%s"%pb_strip)
	print("\tLength : %d" % len(pb_strip))

neutral_bit()	




def find_pertubation_vector_low_hamming_weight():
	# No Truncated local collision 
	# No Consecutive pertubation in first 16 step (RELAXED NOW)
	# No Pertubation after step 74                (RELAXED NOW)
	HBITS = 16
	NEGATIVE_C = 0#5
	for i in range(1<<HBITS):
		head = []#[0,0,1,1,1]
		for j in range(HBITS):
			head.append(1 if (i&(1<<j)) != 0 else 0)

		vector = disturbance_vector(head, NEGATIVE_C = NEGATIVE_C)
		# Test Vector
		if not condition_one(vector):
			continue
		# print vector
		hamming_weight 	= sum(vector[20+NEGATIVE_C:])
		if not(17<= hamming_weight and hamming_weight<=19):
			continue
		# Round 3 , HW = 3
		if not(sum(vector[40+NEGATIVE_C:60+NEGATIVE_C])==3):
			continue
		yield vector[NEGATIVE_C:]

def findingDisturbanceVectorsWithLowHammingWeight():
	if os.path.exists(pertubation_vectorshm_fname):
		disturbance_vectors = json.load(open(pertubation_vectorshm_fname))
		print("Loaded from Cache %s" % pertubation_vectorshm_fname)
	else:
		disturbance_vectors=[]
		for x in find_pertubation_vector_low_hamming_weight():
			disturbance_vectors.append(arraytobin(x))
		json.dump(disturbance_vectors,open(pertubation_vectorshm_fname,'w'))

	# print(json.dumps(disturbance_vectors, indent=4))
	print("Disturbance Vector with 17 to 19 Hamming Distance with Condition 1 only\n and Round 3 Hamming Distance as 3, %d found" %len(disturbance_vectors))
	# print("First one of them is")
	for y in disturbance_vectors:
		print_str(disturbance_vectors[0])
		print ""
findingDisturbanceVectorsWithLowHammingWeight()

def print_differential(a1,a2):
	for j in range(80):
		print(bin(a1[j][0]^a2[j][0])+" "+bin(a1[j][1]^a2[j][1])+" "+bin(a1[j][2]^a2[j][2])+" "+bin(a1[j][3]^a2[j][3])+" "+bin(a1[j][4]^a2[j][4])+" ")
	
def do_local_collision():
	print "Do local collision"
	_word80 = [0]*80#sha0_word_generation([1]*16)
	cc = [1,1<<5,1,1<<30,1<<30,1<<30]
	changes=[0]*(80)
	for i in range(80):
		if pb[i]=='1':
			for j in range(5):
				changes[i+j]^=cc[j]
	w = []
	for i,x in enumerate(range(80)):
		w.append(_word80[i]^changes[i])
	result,a1 = sha0_singleblock_diff_xor(w)
	print result
	w = []
	for i,x in enumerate(range(80)):
		w.append(_word80[i])
	result,a2 = sha0_singleblock_diff_xor(w)
	print result
	print_differential(a1,a2)
	# w = []
	# for i,x in enumerate(pb):
	# 	w.append(_word80[i]^(2 if x=='1' else 0))
	# result = sha0_singleblock_diff(w)
	# print result

def do_local_collision2():
	print "Do local collision"
	_word80 = [0]*80#sha0_word_generation([1]*16)
	cc = [1,1<<5,1,1<<30,1<<30,1<<30]
	changes=[0]*(80)
	for i in range(80):
		if pb[i]=='1':
			for j in range(5):
				changes[i+j]^=cc[j]
	w = []
	for i,x in enumerate(range(80)):
		w.append(_word80[i]^changes[i])
	result,a1 = sha0_singleblock_diff_xor(w)
	print result
	w = []
	for i,x in enumerate(range(80)):
		w.append(_word80[i])
	result,a2 = sha0_singleblock_diff_xor(w)
	print result
	print_differential(a1,a2)
	# w = []
	# for i,x in enumerate(pb):
	# 	w.append(_word80[i]^(2 if x=='1' else 0))
	# result = sha0_singleblock_diff(w)
	# print result

# test_pertubation_vector()
# do_local_collision()
