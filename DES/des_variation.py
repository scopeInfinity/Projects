import sys
import argparse
import random
import numpy as np

DES_ROUNDS = 16

def rotatebitleft(b,cnt,bitlength):
	r = ((b<<cnt) | (b>>(bitlength-cnt))) & ((1<<bitlength)-1)
	return r


def pbox(permutation,_in):
	out = 0
	for i in range(len(permutation)):
		if (_in & (1<<(permutation[i]-1))) > 0:
			out |=  1<<i
	return out

# Key scheduler compression pbox
# 56bits to 48bits then truncate to 32 bits
def ks_compression_pbox(_in):
	permutation_bitlocation = [
			14,17,11,24, 1, 5, 3,28,
			15, 6,21,10,23,19,12, 4,
			26, 8,16, 7,27,20,13, 2,
			40,52,31,37,47,55,30,40,
			51,45,33,48,44,49,39,56,
			34,53,46,42,50,36,29,32
		]
	out_48 = pbox(permutation_bitlocation, _in)
	out_32 = out_48&((1<<32)-1)
	return out_32

def key_scheduler(key):
	# Assuming msb 8 bit to be parity bit
	# Ignoring high 8 parity bits
	cipherkey = key&((1<<56)-1)
	onebitshiftround = [0,1,8,15]

	CK_HALFSIZE = 28
	ck_l = cipherkey>>CK_HALFSIZE
	ck_r = cipherkey&((1<<CK_HALFSIZE)-1)
	keys = []
	for r in range(DES_ROUNDS):
		shamt = 2
		if r in onebitshiftround:
			shamt = 1
		ck_l = rotatebitleft(ck_l,shamt,CK_HALFSIZE)
		ck_r = rotatebitleft(ck_r,shamt,CK_HALFSIZE)
		keys.append(ks_compression_pbox( (ck_l<<CK_HALFSIZE) | ck_r ))
	return keys


def sbox(table,_in):
	return table[_in]

# pass splitinto partition of _in to sbox 4x4
def sboxs(table, _in, splitinto):
	_out = 0
	for i in range(splitinto):
		_in_i = (_in>>(4*i))&0xF
		_out |= sbox(table, _in_i)<<(4*i)
	return _out

def des_round(data_l,data_r,rkey):
	d_pemutation = [
		17,28,12,29,21,20, 7,16,
		 1,15,23,26, 5,18,31,10,
		25, 4,11,22, 6,30,13,19,
		 2, 8,24,14,32,27, 3, 9
	]
	s_table = [0xE,0x4,0xD,0x1,0x2,0xF,0xB,0x8,0x3,0xA,0x6,0xC,0x5,0x9,0x0,0x7]
	
	x_out = data_r^rkey
	s_out = sboxs(s_table, x_out, 8)
	out_p = pbox(d_pemutation, s_out)
	ndata_r = data_l ^ (out_p)
	ndata_l = data_r
	return (ndata_l,ndata_r)

def des(data,round_keys):
	DA_HALFSIZE = 32
	data_l = data>>DA_HALFSIZE
	data_r = data&((1<<DA_HALFSIZE)-1)
	for rkey in round_keys:
		data_l,data_r = des_round(data_l,data_r,rkey)
	cipher = (data_l<<DA_HALFSIZE) | data_r
	return cipher

def encrypt(data,key):
	round_keys = key_scheduler(key)
	cipher = des(data, round_keys)
	return cipher

def test_avalanche():
	bitfliped_key = []
	bitfliped_data = []
	for i in range(100):
		data = random.randint(0,(1<<64)-1)
		key = random.randint(0,(1<<64)-1)
		index = random.randint(0,63)
		cipher1 = encrypt(data,key)
		data^=1<<index
		cipher2 = encrypt(data,key)
		flips = bin(cipher1^cipher2).count('1')
		bitfliped_data.append(flips)
		
		index = random.randint(0,63)
		key^=1<<index
		cipher3 = encrypt(data,key)
		flips = bin(cipher2^cipher3).count('1')
		bitfliped_key.append(flips)

	print "Cipher bit flips on single Bit flip in Data"
	print "\tMean %f\t Std.Deviation %f" % (np.mean(bitfliped_data),np.std(bitfliped_data))
	print "Cipher bit flips on single Bit flip in Key"
	print "\tMean %f\t Std.Deviation %f" % (np.mean(bitfliped_key),np.std(bitfliped_key))

def main():
	parser = argparse.ArgumentParser(description = '64 bit DES encryption using 32 bit round keys')
	parser.add_argument('-ta', '--test_avalanche', action='store_true', help='Test avalanche property of cipher')
	parser.add_argument('-r', '--round', type=int, help='Rounds in encryption (Default 16)')
	parser.add_argument('-m', '--msg', type=int, help='Message to Encrypt (64 bit int)')
	parser.add_argument('-k', '--key', type=int, help='Key for Encryption (64 bit int)')
	args = parser.parse_args()
	global DES_ROUNDS
	if args.round:
		DES_ROUNDS = args.round
		print "DES rounds changed to %d" % DES_ROUNDS
	if args.test_avalanche:
		test_avalanche()
		return
	if args.msg and args.key:
		data = args.msg
		key  = args.key

		cipher = encrypt(data, key)
		print cipher

if __name__ == '__main__':
	main()