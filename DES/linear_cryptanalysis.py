from  des_variation import encrypt, pbox, permutation_bitlocation, d_pemutation, s_table
import numpy as np
import sys
# bits = Bits to consider in 'number' for xor
def getxor(number, bits):
	number = number&bits
	xor = bin(number).count('1')%2
	return xor

def get_linearapprox(sbox):
	BITSIZE = 4
	out = []
	for i in range(1<<BITSIZE):
		out.append(sbox[i])
	lineartable = []
	SIZE = 1<<BITSIZE
	for i in range(SIZE):
		row = []
		for j in range(SIZE):
			c = 0
			for sample_in,sample_out in enumerate(out):
				if (getxor(sample_in,i)^getxor(sample_out,j)) == 0:
					c+=1
			row.append(c)
		lineartable.append(row)
	lineartable = np.asarray(lineartable)
	lineartable_wb = lineartable - SIZE/2
	return lineartable_wb

def getbest_s_table_single(s_lineartable,_in):
	index = np.argmax(s_lineartable[_in])
	return (index,s_lineartable[_in][index]/16.0)

def getbest_s_table(s_lineartable,_in):
	_out = 0
	p1 = 1
	for i in range(8):
		_in_i = (_in>>(4*i))&0xF
		res = getbest_s_table_single(s_lineartable, _in_i)
		_out |= res[0]<<(4*i)
		p1 *= res[1]
	return _out,p1

def attack(fname, rounds):
	print "Trying to find key"
	s_lineartable = get_linearapprox(s_table)
	# init_mxbias = (2,14) # on pain_r

	plaintextbits  = 2	# Bits of plaintext to consider
	out_bits = 2
	kbits = 0

	DA_HALFSIZE = 32
	cbits_l = out_bits>>DA_HALFSIZE
	cbits_r = out_bits&((1<<DA_HALFSIZE)-1)
	# Xor of plaintextbits in plaintext and out_bits in out of cround round and kbits of rkey is zero
	prob_l = 2.0 # only bias
	prob_r = 2.0 # only bias

	for cround in range(rounds):
		ncbits_l = cbits_r
		afterstable,bias =  getbest_s_table(s_lineartable,cbits_l)
		afterpermuation = pbox(d_pemutation, afterstable)
		ncbits_r = afterpermuation | cbits_l
		cbits_l,cbits_r = ncbits_l,ncbits_r
		prob_r = prob_r/2 * bias
		prob_r,prob_l = prob_l,prob_r
		print "Probablity Bias : %0.15f %0.15f" % (prob_l,prob_r)
	print "Probable bit flips %s %s" % (bin(cbits_l),bin(cbits_r)) 

	
	samples = []
	with open(fname,'r') as f:
		for line in f:
			samples.append([int(x) for x in line.split()])

	bkey = None
	i = 0
	while i<(1<<64):
		good = False
		for sample in samples:
			cipher = encrypt(sample[0], i, rounds=8)
			if cipher == sample[1]:
				good = True
			break	
		if good:
			bkey = i
			break
		i += 1
	if bkey is None:
		print "Unable to obtain key"
		assert False
	return bkey

def main():
	assert len(sys.argv)>1
	if sys.argv[1] == '--show-s_table':
		s_lineartable = get_linearapprox(s_table)
		print s_lineartable
		return
	fname = sys.argv[1]
	rounds = int(sys.argv[2])
	key = attack(fname, rounds)	
	print "Key found as %d" % key
	
if __name__ == '__main__':
	main()

