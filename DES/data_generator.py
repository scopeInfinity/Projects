from  des_variation import encrypt
import random
import sys

key = int(sys.argv[1])
rounds = 8
samples = 100000
with open(str(key)+'_'+str(rounds)+'.txt','w') as f:
	for i in range(samples):
		msg = random.randint(0,(1<<63)-1)
		cipher = encrypt(msg, key, rounds = rounds)
		f.write("%d %d\n" % (msg,cipher))

