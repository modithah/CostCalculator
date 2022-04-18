### Fixed data ######
from gekko import GEKKO
import math
def solve_me():
	m = GEKKO(remote=False)  # create GEKKO model
	#data
	dinternal=22
	idsize=12
	dmeta=6
	dB = 4096*8
	dfill=1
	dBint = 4096
	dfillint=.75
	


	#index
	iinternal=22
	imeta=30
	iB = 4096*8
	ifill=1
	iBint = 4096*4
	ifillint=.75
	primary_idx = math.floor((ifill*iB-imeta) / 15)
	secondary_idx = math.floor((ifill*iB-imeta) / 8)
	# Memory
	M=  2.1346688E8

	# Collection collection_1
	collection_1_size = 1365.0
	collection_1_count = 5000000.0
	collection_1_freq = 0.16103059589070925
	#docs per block
	collection_1_docs = math.floor((dfill*dB-dmeta) / collection_1_size)
	#data leaf and internal blocks
	collection_1_leaf  = collection_1_count / collection_1_docs
	collection_1_internal  = collection_1_leaf * dinternal / ( dfillint * dBint - dmeta)

	# Collection collection_1 Indexes #####
	# Index collection_1_index_1

	collection_1_index_1_mult = 10.0
	collection_1_index_1_distinct = 5.0E7
	collection_1_index_1_rep = collection_1_index_1_mult * collection_1_count / collection_1_index_1_distinct
	collection_1_index_1_freq = 0.00805152976482532

	# Index collection_1_index_1 leaf and internal blocks
	collection_1_index_1_leaf = collection_1_index_1_mult * collection_1_count / secondary_idx
	collection_1_index_1_internal = collection_1_index_1_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_1_index_2

	collection_1_index_2_mult = 10.0
	collection_1_index_2_distinct = 5.0E7
	collection_1_index_2_rep = collection_1_index_2_mult * collection_1_count / collection_1_index_2_distinct
	collection_1_index_2_freq = 0.004025764931929568

	# Index collection_1_index_2 leaf and internal blocks
	collection_1_index_2_leaf = collection_1_index_2_mult * collection_1_count / secondary_idx
	collection_1_index_2_internal = collection_1_index_2_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_1_index_3

	collection_1_index_3_mult = 2.0
	collection_1_index_3_distinct = 1.0E7
	collection_1_index_3_rep = collection_1_index_3_mult * collection_1_count / collection_1_index_3_distinct
	collection_1_index_3_freq = 0.020128824461580205

	# Index collection_1_index_3 leaf and internal blocks
	collection_1_index_3_leaf = collection_1_index_3_mult * collection_1_count / secondary_idx
	collection_1_index_3_internal = collection_1_index_3_leaf * iinternal / (ifillint * iBint - imeta)

	# Collection collection_2
	collection_2_size = 14.0
	collection_2_count = 4.0
	collection_2_freq = 0.002415458939350977
	#docs per block
	collection_2_docs = math.floor((dfill*dB-dmeta) / collection_2_size)
	#data leaf and internal blocks
	collection_2_leaf  = collection_2_count / collection_2_docs
	collection_2_internal  = collection_2_leaf * dinternal / ( dfillint * dBint - dmeta)

	# Collection collection_2 Indexes #####
	# Index collection_2_index_1

	collection_2_index_1_mult = 1.0
	collection_2_index_1_distinct = 4.0
	collection_2_index_1_rep = collection_2_index_1_mult * collection_2_count / collection_2_index_1_distinct
	collection_2_index_1_freq = 0.002415458939350977

	# Index collection_2_index_1 leaf and internal blocks
	collection_2_index_1_leaf = collection_2_count / primary_idx
	collection_2_index_1_internal = collection_2_index_1_leaf * iinternal / (ifillint * iBint - imeta)

	# Collection collection_3
	collection_3_size = 540.0
	collection_3_count = 7500000.0
	collection_3_freq = 0.5495169081081444
	#docs per block
	collection_3_docs = math.floor((dfill*dB-dmeta) / collection_3_size)
	#data leaf and internal blocks
	collection_3_leaf  = collection_3_count / collection_3_docs
	collection_3_internal  = collection_3_leaf * dinternal / ( dfillint * dBint - dmeta)

	# Collection collection_3 Indexes #####
	# Index collection_3_index_1

	collection_3_index_1_mult = 1.0
	collection_3_index_1_distinct = 7500000.0
	collection_3_index_1_rep = collection_3_index_1_mult * collection_3_count / collection_3_index_1_distinct
	collection_3_index_1_freq = 0.006038647348377443

	# Index collection_3_index_1 leaf and internal blocks
	collection_3_index_1_leaf = collection_3_count / primary_idx
	collection_3_index_1_internal = collection_3_index_1_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_3_index_2

	collection_3_index_2_mult = 15.0
	collection_3_index_2_distinct = 1.125E8
	collection_3_index_2_rep = collection_3_index_2_mult * collection_3_count / collection_3_index_2_distinct
	collection_3_index_2_freq = 0.03220611915833509

	# Index collection_3_index_2 leaf and internal blocks
	collection_3_index_2_leaf = collection_3_index_2_mult * collection_3_count / secondary_idx
	collection_3_index_2_internal = collection_3_index_2_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_3_index_3

	collection_3_index_3_mult = 3.0
	collection_3_index_3_distinct = 2.25E7
	collection_3_index_3_rep = collection_3_index_3_mult * collection_3_count / collection_3_index_3_distinct
	collection_3_index_3_freq = 0.020128824461580205

	# Index collection_3_index_3 leaf and internal blocks
	collection_3_index_3_leaf = collection_3_index_3_mult * collection_3_count / secondary_idx
	collection_3_index_3_internal = collection_3_index_3_leaf * iinternal / (ifillint * iBint - imeta)

	# Collection collection_4
	collection_4_size = 127.0
	collection_4_count = 500000.0
	collection_4_freq = 0.09702093399790837
	#docs per block
	collection_4_docs = math.floor((dfill*dB-dmeta) / collection_4_size)
	#data leaf and internal blocks
	collection_4_leaf  = collection_4_count / collection_4_docs
	collection_4_internal  = collection_4_leaf * dinternal / ( dfillint * dBint - dmeta)

	# Collection collection_4 Indexes #####
	# Index collection_4_index_1

	collection_4_index_1_mult = 1.0
	collection_4_index_1_distinct = 500000.0
	collection_4_index_1_rep = collection_4_index_1_mult * collection_4_count / collection_4_index_1_distinct
	collection_4_index_1_freq = 0.0422705314386421

	# Index collection_4_index_1 leaf and internal blocks
	collection_4_index_1_leaf = collection_4_count / primary_idx
	collection_4_index_1_internal = collection_4_index_1_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_4_index_2

	collection_4_index_2_mult = 1.0
	collection_4_index_2_distinct = 500000.0
	collection_4_index_2_rep = collection_4_index_2_mult * collection_4_count / collection_4_index_2_distinct
	collection_4_index_2_freq = 4.025765229031016E-4

	# Index collection_4_index_2 leaf and internal blocks
	collection_4_index_2_leaf = collection_4_index_2_mult * collection_4_count / secondary_idx
	collection_4_index_2_internal = collection_4_index_2_leaf * iinternal / (ifillint * iBint - imeta)

	# Index collection_4_index_3

	collection_4_index_3_mult = 1.0
	collection_4_index_3_distinct = 500000.0
	collection_4_index_3_rep = collection_4_index_3_mult * collection_4_count / collection_4_index_3_distinct
	collection_4_index_3_freq = 0.05434782603636317

	# Index collection_4_index_3 leaf and internal blocks
	collection_4_index_3_leaf = collection_4_index_3_mult * collection_4_count / secondary_idx
	collection_4_index_3_internal = collection_4_index_3_leaf * iinternal / (ifillint * iBint - imeta)


	# saturation memory variables
	T = m.Var(value = 5000)
	collection_1_sat = m.Var(value = 1000)
	collection_1_prob = m.Var(value = 1)
	collection_2_sat = m.Var(value = 1000)
	collection_2_prob = m.Var(value = 1)
	collection_3_sat = m.Var(value = 1000)
	collection_3_prob = m.Var(value = 1)
	collection_4_sat = m.Var(value = 1000)
	collection_4_prob = m.Var(value = 1)
	collection_2_index_1_sat = m.Var(value = 1000)
	collection_2_index_1_req = m.Var(value = 1)
	collection_2_index_1_SF = m.Var(value = 1)
	collection_2_index_1_exp = m.Var(value = 1)
	collection_2_index_1_prob = m.Var(value = 1)
	collection_3_index_1_sat = m.Var(value = 1000)
	collection_3_index_1_req = m.Var(value = 1)
	collection_3_index_1_SF = m.Var(value = 1)
	collection_3_index_1_exp = m.Var(value = 1)
	collection_3_index_1_prob = m.Var(value = 1)
	collection_4_index_1_sat = m.Var(value = 1000)
	collection_4_index_1_req = m.Var(value = 1)
	collection_4_index_1_SF = m.Var(value = 1)
	collection_4_index_1_exp = m.Var(value = 1)
	collection_4_index_1_prob = m.Var(value = 1)
	collection_1_index_1_sat = m.Var(value = 1000)
	collection_1_index_1_req = m.Var(value = 1)
	collection_1_index_1_SF = m.Var(value = 1)
	collection_1_index_1_exp = m.Var(value = 1)
	collection_1_index_1_prob = m.Var(value = 1)
	collection_1_index_2_sat = m.Var(value = 1000)
	collection_1_index_2_req = m.Var(value = 1)
	collection_1_index_2_SF = m.Var(value = 1)
	collection_1_index_2_exp = m.Var(value = 1)
	collection_1_index_2_prob = m.Var(value = 1)
	collection_1_index_3_sat = m.Var(value = 1000)
	collection_1_index_3_req = m.Var(value = 1)
	collection_1_index_3_SF = m.Var(value = 1)
	collection_1_index_3_exp = m.Var(value = 1)
	collection_1_index_3_prob = m.Var(value = 1)
	collection_3_index_2_sat = m.Var(value = 1000)
	collection_3_index_2_req = m.Var(value = 1)
	collection_3_index_2_SF = m.Var(value = 1)
	collection_3_index_2_exp = m.Var(value = 1)
	collection_3_index_2_prob = m.Var(value = 1)
	collection_3_index_3_sat = m.Var(value = 1000)
	collection_3_index_3_req = m.Var(value = 1)
	collection_3_index_3_SF = m.Var(value = 1)
	collection_3_index_3_exp = m.Var(value = 1)
	collection_3_index_3_prob = m.Var(value = 1)
	collection_4_index_2_sat = m.Var(value = 1000)
	collection_4_index_2_req = m.Var(value = 1)
	collection_4_index_2_SF = m.Var(value = 1)
	collection_4_index_2_exp = m.Var(value = 1)
	collection_4_index_2_prob = m.Var(value = 1)
	collection_4_index_3_sat = m.Var(value = 1000)
	collection_4_index_3_req = m.Var(value = 1)
	collection_4_index_3_SF = m.Var(value = 1)
	collection_4_index_3_exp = m.Var(value = 1)
	collection_4_index_3_prob = m.Var(value = 1)
	m.Equations([
	1.1 * (collection_1_sat + collection_1_internal * dBint) +
1.1 * (collection_2_sat + collection_2_internal * dBint) +
1.1 * (collection_3_sat + collection_3_internal * dBint) +
1.1 * (collection_4_sat + collection_4_internal * dBint) +
1.1 * (collection_2_index_1_sat + collection_2_index_1_internal * iBint) +
1.1 * (collection_3_index_1_sat + collection_3_index_1_internal * iBint) +
1.1 * (collection_4_index_1_sat + collection_4_index_1_internal * iBint) +
1.1 * (collection_1_index_1_sat + collection_1_index_1_internal * iBint) +
1.1 * (collection_1_index_2_sat + collection_1_index_2_internal * iBint) +
1.1 * (collection_1_index_3_sat + collection_1_index_3_internal * iBint) +
1.1 * (collection_3_index_2_sat + collection_3_index_2_internal * iBint) +
1.1 * (collection_3_index_3_sat + collection_3_index_3_internal * iBint) +
1.1 * (collection_4_index_2_sat + collection_4_index_2_internal * iBint) +
1.1 * (collection_4_index_3_sat + collection_4_index_3_internal * iBint)  == M,


	collection_1_sat == collection_1_leaf * dB * collection_1_prob,
	collection_2_sat == collection_2_leaf * dB * collection_2_prob,
	collection_3_sat == collection_3_leaf * dB * collection_3_prob,
	collection_4_sat == collection_4_leaf * dB * collection_4_prob,
	collection_2_index_1_sat == collection_2_index_1_leaf * iB * collection_2_index_1_prob,
	collection_3_index_1_sat == collection_3_index_1_leaf * iB * collection_3_index_1_prob,
	collection_4_index_1_sat == collection_4_index_1_leaf * iB * collection_4_index_1_prob,
	collection_1_index_1_sat == collection_1_index_1_leaf * iB * collection_1_index_1_prob,
	collection_1_index_2_sat == collection_1_index_2_leaf * iB * collection_1_index_2_prob,
	collection_1_index_3_sat == collection_1_index_3_leaf * iB * collection_1_index_3_prob,
	collection_3_index_2_sat == collection_3_index_2_leaf * iB * collection_3_index_2_prob,
	collection_3_index_3_sat == collection_3_index_3_leaf * iB * collection_3_index_3_prob,
	collection_4_index_2_sat == collection_4_index_2_leaf * iB * collection_4_index_2_prob,
	collection_4_index_3_sat == collection_4_index_3_leaf * iB * collection_4_index_3_prob,

	collection_1_prob == 
	1 - (1 - (((( 1 - (1 - collection_1_index_1_SF) ** collection_1_index_1_mult))
+ (( 1 - (1 - collection_1_index_2_SF) ** collection_1_index_2_mult))
+ (( 1 - (1 - collection_1_index_3_SF) ** collection_1_index_3_mult))
-(( 1 - (1 - collection_1_index_1_SF) ** collection_1_index_1_mult)*( 1 - (1 - collection_1_index_2_SF) ** collection_1_index_2_mult))
-(( 1 - (1 - collection_1_index_1_SF) ** collection_1_index_1_mult)*( 1 - (1 - collection_1_index_3_SF) ** collection_1_index_3_mult))
-(( 1 - (1 - collection_1_index_2_SF) ** collection_1_index_2_mult)*( 1 - (1 - collection_1_index_3_SF) ** collection_1_index_3_mult))
+ (( 1 - (1 - collection_1_index_1_SF) ** collection_1_index_1_mult)*( 1 - (1 - collection_1_index_2_SF) ** collection_1_index_2_mult)*( 1 - (1 - collection_1_index_3_SF) ** collection_1_index_3_mult))
)/1 )) ** collection_1_docs ,
	collection_2_prob == 
	1 - (1 - (((collection_2_index_1_SF)
)/1 )) ** collection_2_docs ,
	collection_3_prob == 
	1 - (1 - (((collection_3_index_1_SF)
+ (( 1 - (1 - collection_3_index_2_SF) ** collection_3_index_2_mult))
+ (( 1 - (1 - collection_3_index_3_SF) ** collection_3_index_3_mult))
-(collection_3_index_1_SF*( 1 - (1 - collection_3_index_2_SF) ** collection_3_index_2_mult))
-(collection_3_index_1_SF*( 1 - (1 - collection_3_index_3_SF) ** collection_3_index_3_mult))
-(( 1 - (1 - collection_3_index_2_SF) ** collection_3_index_2_mult)*( 1 - (1 - collection_3_index_3_SF) ** collection_3_index_3_mult))
+ (collection_3_index_1_SF*( 1 - (1 - collection_3_index_2_SF) ** collection_3_index_2_mult)*( 1 - (1 - collection_3_index_3_SF) ** collection_3_index_3_mult))
)/1 )) ** collection_3_docs ,
	collection_4_prob == 
	1 - (1 - (((collection_4_index_1_SF)
+ (( 1 - (1 - collection_4_index_2_SF) ** collection_4_index_2_mult))
+ (( 1 - (1 - collection_4_index_3_SF) ** collection_4_index_3_mult))
-(collection_4_index_1_SF*( 1 - (1 - collection_4_index_2_SF) ** collection_4_index_2_mult))
-(collection_4_index_1_SF*( 1 - (1 - collection_4_index_3_SF) ** collection_4_index_3_mult))
-(( 1 - (1 - collection_4_index_2_SF) ** collection_4_index_2_mult)*( 1 - (1 - collection_4_index_3_SF) ** collection_4_index_3_mult))
+ (collection_4_index_1_SF*( 1 - (1 - collection_4_index_2_SF) ** collection_4_index_2_mult)*( 1 - (1 - collection_4_index_3_SF) ** collection_4_index_3_mult))
)/1 )) ** collection_4_docs ,
	collection_2_index_1_prob == 1 - ( 1 - collection_2_index_1_SF) ** primary_idx ,
	collection_3_index_1_prob == 1 - ( 1 - collection_3_index_1_SF) ** primary_idx ,
	collection_4_index_1_prob == 1 - ( 1 - collection_4_index_1_SF) ** primary_idx ,
	collection_1_index_1_prob == 1 - ( 1 - collection_1_index_1_SF) ** (secondary_idx /collection_1_index_1_rep) ,
	collection_1_index_2_prob == 1 - ( 1 - collection_1_index_2_SF) ** (secondary_idx /collection_1_index_2_rep) ,
	collection_1_index_3_prob == 1 - ( 1 - collection_1_index_3_SF) ** (secondary_idx /collection_1_index_3_rep) ,
	collection_3_index_2_prob == 1 - ( 1 - collection_3_index_2_SF) ** (secondary_idx /collection_3_index_2_rep) ,
	collection_3_index_3_prob == 1 - ( 1 - collection_3_index_3_SF) ** (secondary_idx /collection_3_index_3_rep) ,
	collection_4_index_2_prob == 1 - ( 1 - collection_4_index_2_SF) ** (secondary_idx /collection_4_index_2_rep) ,
	collection_4_index_3_prob == 1 - ( 1 - collection_4_index_3_SF) ** (secondary_idx /collection_4_index_3_rep) ,

	collection_2_index_1_SF == collection_2_index_1_exp / collection_2_count,
	collection_3_index_1_SF == collection_3_index_1_exp / collection_3_count,
	collection_4_index_1_SF == collection_4_index_1_exp / collection_4_count,
	collection_1_index_1_SF == collection_1_index_1_exp / collection_1_count,
	collection_1_index_2_SF == collection_1_index_2_exp / collection_1_count,
	collection_1_index_3_SF == collection_1_index_3_exp / collection_1_count,
	collection_3_index_2_SF == collection_3_index_2_exp / collection_3_count,
	collection_3_index_3_SF == collection_3_index_3_exp / collection_3_count,
	collection_4_index_2_SF == collection_4_index_2_exp / collection_4_count,
	collection_4_index_3_SF == collection_4_index_3_exp / collection_4_count,

	collection_2_index_1_req == T * collection_2_index_1_freq, 
	collection_3_index_1_req == T * collection_3_index_1_freq, 
	collection_4_index_1_req == T * collection_4_index_1_freq, 
	collection_1_index_1_req == T * collection_1_index_1_freq, 
	collection_1_index_2_req == T * collection_1_index_2_freq, 
	collection_1_index_3_req == T * collection_1_index_3_freq, 
	collection_3_index_2_req == T * collection_3_index_2_freq, 
	collection_3_index_3_req == T * collection_3_index_3_freq, 
	collection_4_index_2_req == T * collection_4_index_2_freq, 
	collection_4_index_3_req == T * collection_4_index_3_freq, 

	collection_2_index_1_exp == collection_2_count * ( 1 - ((collection_2_count- 1) /collection_2_count) ** collection_2_index_1_req),
	collection_3_index_1_exp == collection_3_count * ( 1 - ((collection_3_count- 1) /collection_3_count) ** collection_3_index_1_req),
	collection_4_index_1_exp == collection_4_count * ( 1 - ((collection_4_count- 1) /collection_4_count) ** collection_4_index_1_req),
	collection_1_index_1_exp == collection_1_index_1_distinct * ( 1 - ((collection_1_index_1_distinct- 1) /collection_1_index_1_distinct) ** collection_1_index_1_req),
	collection_1_index_2_exp == collection_1_index_2_distinct * ( 1 - ((collection_1_index_2_distinct- 1) /collection_1_index_2_distinct) ** collection_1_index_2_req),
	collection_1_index_3_exp == collection_1_index_3_distinct * ( 1 - ((collection_1_index_3_distinct- 1) /collection_1_index_3_distinct) ** collection_1_index_3_req),
	collection_3_index_2_exp == collection_3_index_2_distinct * ( 1 - ((collection_3_index_2_distinct- 1) /collection_3_index_2_distinct) ** collection_3_index_2_req),
	collection_3_index_3_exp == collection_3_index_3_distinct * ( 1 - ((collection_3_index_3_distinct- 1) /collection_3_index_3_distinct) ** collection_3_index_3_req),
	collection_4_index_2_exp == collection_4_index_2_distinct * ( 1 - ((collection_4_index_2_distinct- 1) /collection_4_index_2_distinct) ** collection_4_index_2_req),
	collection_4_index_3_exp == collection_4_index_3_distinct * ( 1 - ((collection_4_index_3_distinct- 1) /collection_4_index_3_distinct) ** collection_4_index_3_req),
	]) 
	m.options.SOLVER = 1
	m.solve(disp=False,debug=False)
	


	j = GEKKO(remote=False)
	Q1=T.value[0]
	

	# Initialize variables 

	collection_1_evict = j.Var(value = collection_1_sat.value[0] / dB)
	collection_1_E = j.Var(value = collection_1_sat.value[0] / dB)
	collection_1_shots = j.Var(value=1000)
	collection_2_evict = j.Var(value = collection_2_sat.value[0] / dB)
	collection_2_E = j.Var(value = collection_2_sat.value[0] / dB)
	collection_2_shots = j.Var(value=1000)
	collection_3_evict = j.Var(value = collection_3_sat.value[0] / dB)
	collection_3_E = j.Var(value = collection_3_sat.value[0] / dB)
	collection_3_shots = j.Var(value=1000)
	collection_4_evict = j.Var(value = collection_4_sat.value[0] / dB)
	collection_4_E = j.Var(value = collection_4_sat.value[0] / dB)
	collection_4_shots = j.Var(value=1000)

	collection_2_index_1_evict = j.Var(value = collection_2_index_1_sat.value[0] / iB)
	collection_2_index_1_E = j.Var(value = collection_2_index_1_sat.value[0] / iB)
	collection_2_index_1_shots = j.Var(value=1000)
	collection_3_index_1_evict = j.Var(value = collection_3_index_1_sat.value[0] / iB)
	collection_3_index_1_E = j.Var(value = collection_3_index_1_sat.value[0] / iB)
	collection_3_index_1_shots = j.Var(value=1000)
	collection_4_index_1_evict = j.Var(value = collection_4_index_1_sat.value[0] / iB)
	collection_4_index_1_E = j.Var(value = collection_4_index_1_sat.value[0] / iB)
	collection_4_index_1_shots = j.Var(value=1000)

	collection_1_index_1_evict = j.Var(value = collection_1_index_1_sat.value[0] / iB)
	collection_1_index_1_E = j.Var(value = collection_1_index_1_sat.value[0] / iB)
	collection_1_index_1_shots = j.Var(value=1000)
	collection_1_index_2_evict = j.Var(value = collection_1_index_2_sat.value[0] / iB)
	collection_1_index_2_E = j.Var(value = collection_1_index_2_sat.value[0] / iB)
	collection_1_index_2_shots = j.Var(value=1000)
	collection_1_index_3_evict = j.Var(value = collection_1_index_3_sat.value[0] / iB)
	collection_1_index_3_E = j.Var(value = collection_1_index_3_sat.value[0] / iB)
	collection_1_index_3_shots = j.Var(value=1000)
	collection_3_index_2_evict = j.Var(value = collection_3_index_2_sat.value[0] / iB)
	collection_3_index_2_E = j.Var(value = collection_3_index_2_sat.value[0] / iB)
	collection_3_index_2_shots = j.Var(value=1000)
	collection_3_index_3_evict = j.Var(value = collection_3_index_3_sat.value[0] / iB)
	collection_3_index_3_E = j.Var(value = collection_3_index_3_sat.value[0] / iB)
	collection_3_index_3_shots = j.Var(value=1000)
	collection_4_index_2_evict = j.Var(value = collection_4_index_2_sat.value[0] / iB)
	collection_4_index_2_E = j.Var(value = collection_4_index_2_sat.value[0] / iB)
	collection_4_index_2_shots = j.Var(value=1000)
	collection_4_index_3_evict = j.Var(value = collection_4_index_3_sat.value[0] / iB)
	collection_4_index_3_E = j.Var(value = collection_4_index_3_sat.value[0] / iB)
	collection_4_index_3_shots = j.Var(value=1000)
	j.Equations([
	1.1 * (collection_1_evict*dB + collection_1_internal * dBint) +
1.1 * (collection_2_evict*dB + collection_2_internal * dBint) +
1.1 * (collection_3_evict*dB + collection_3_internal * dBint) +
1.1 * (collection_4_evict*dB + collection_4_internal * dBint) +
1.1 * (collection_2_index_1_evict*iB + collection_2_index_1_internal * iBint) +
1.1 * (collection_3_index_1_evict*iB + collection_3_index_1_internal * iBint) +
1.1 * (collection_4_index_1_evict*iB + collection_4_index_1_internal * iBint) +
1.1 * (collection_1_index_1_evict*iB + collection_1_index_1_internal * iBint) +
1.1 * (collection_1_index_2_evict*iB + collection_1_index_2_internal * iBint) +
1.1 * (collection_1_index_3_evict*iB + collection_1_index_3_internal * iBint) +
1.1 * (collection_3_index_2_evict*iB + collection_3_index_2_internal * iBint) +
1.1 * (collection_3_index_3_evict*iB + collection_3_index_3_internal * iBint) +
1.1 * (collection_4_index_2_evict*iB + collection_4_index_2_internal * iBint) +
1.1 * (collection_4_index_3_evict*iB + collection_4_index_3_internal * iBint)  == M,

	collection_1_shots == collection_1_freq * Q1 * collection_1_evict / collection_1_leaf,
	collection_2_shots == collection_2_freq * Q1 * collection_2_evict / collection_2_leaf,
	collection_3_shots == collection_3_freq * Q1 * collection_3_evict / collection_3_leaf,
	collection_4_shots == collection_4_freq * Q1 * collection_4_evict / collection_4_leaf,
	collection_2_index_1_shots == collection_2_index_1_freq * Q1 * collection_2_index_1_evict / collection_2_index_1_leaf,
	collection_3_index_1_shots == collection_3_index_1_freq * Q1 * collection_3_index_1_evict / collection_3_index_1_leaf,
	collection_4_index_1_shots == collection_4_index_1_freq * Q1 * collection_4_index_1_evict / collection_4_index_1_leaf,
	collection_1_index_1_shots == collection_1_index_1_freq * Q1 * collection_1_index_1_evict / collection_1_index_1_leaf,
	collection_1_index_2_shots == collection_1_index_2_freq * Q1 * collection_1_index_2_evict / collection_1_index_2_leaf,
	collection_1_index_3_shots == collection_1_index_3_freq * Q1 * collection_1_index_3_evict / collection_1_index_3_leaf,
	collection_3_index_2_shots == collection_3_index_2_freq * Q1 * collection_3_index_2_evict / collection_3_index_2_leaf,
	collection_3_index_3_shots == collection_3_index_3_freq * Q1 * collection_3_index_3_evict / collection_3_index_3_leaf,
	collection_4_index_2_shots == collection_4_index_2_freq * Q1 * collection_4_index_2_evict / collection_4_index_2_leaf,
	collection_4_index_3_shots == collection_4_index_3_freq * Q1 * collection_4_index_3_evict / collection_4_index_3_leaf,

	collection_1_E == collection_1_evict * dB * (1 - 1 / (collection_1_evict * dB)) ** collection_1_shots,
	collection_2_E == collection_2_evict * dB * (1 - 1 / (collection_2_evict * dB)) ** collection_2_shots,
	collection_3_E == collection_3_evict * dB * (1 - 1 / (collection_3_evict * dB)) ** collection_3_shots,
	collection_4_E == collection_4_evict * dB * (1 - 1 / (collection_4_evict * dB)) ** collection_4_shots,
	collection_2_index_1_E == collection_2_index_1_evict * iB * (1 - 1 / (collection_2_index_1_evict * iB)) ** collection_2_index_1_shots,
	collection_3_index_1_E == collection_3_index_1_evict * iB * (1 - 1 / (collection_3_index_1_evict * iB)) ** collection_3_index_1_shots,
	collection_4_index_1_E == collection_4_index_1_evict * iB * (1 - 1 / (collection_4_index_1_evict * iB)) ** collection_4_index_1_shots,
	collection_1_index_1_E == collection_1_index_1_evict * iB * (1 - 1 / (collection_1_index_1_evict * iB)) ** collection_1_index_1_shots,
	collection_1_index_2_E == collection_1_index_2_evict * iB * (1 - 1 / (collection_1_index_2_evict * iB)) ** collection_1_index_2_shots,
	collection_1_index_3_E == collection_1_index_3_evict * iB * (1 - 1 / (collection_1_index_3_evict * iB)) ** collection_1_index_3_shots,
	collection_3_index_2_E == collection_3_index_2_evict * iB * (1 - 1 / (collection_3_index_2_evict * iB)) ** collection_3_index_2_shots,
	collection_3_index_3_E == collection_3_index_3_evict * iB * (1 - 1 / (collection_3_index_3_evict * iB)) ** collection_3_index_3_shots,
	collection_4_index_2_E == collection_4_index_2_evict * iB * (1 - 1 / (collection_4_index_2_evict * iB)) ** collection_4_index_2_shots,
	collection_4_index_3_E == collection_4_index_3_evict * iB * (1 - 1 / (collection_4_index_3_evict * iB)) ** collection_4_index_3_shots,
	collection_1_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_1_freq * (1- collection_1_evict / collection_1_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_2_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_2_freq * (1- collection_2_evict / collection_2_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_3_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_3_freq * (1- collection_3_evict / collection_3_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_4_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_4_freq * (1- collection_4_evict / collection_4_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_2_index_1_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_3_index_1_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_4_index_1_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_1_index_1_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_1_index_2_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_1_index_3_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_3_index_2_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_3_index_3_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_4_index_2_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	collection_4_index_3_E / (collection_1_E+collection_2_E+collection_3_E+collection_4_E+collection_2_index_1_E+collection_3_index_1_E+collection_4_index_1_E+collection_1_index_1_E+collection_1_index_2_E+collection_1_index_3_E+collection_3_index_2_E+collection_3_index_3_E+collection_4_index_2_E+collection_4_index_3_E)==
	collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf) / (collection_1_freq * (1- collection_1_evict / collection_1_leaf)+collection_2_freq * (1- collection_2_evict / collection_2_leaf)+collection_3_freq * (1- collection_3_evict / collection_3_leaf)+collection_4_freq * (1- collection_4_evict / collection_4_leaf)+collection_2_index_1_freq * (1- collection_2_index_1_evict / collection_2_index_1_leaf)+collection_3_index_1_freq * (1- collection_3_index_1_evict / collection_3_index_1_leaf)+collection_4_index_1_freq * (1- collection_4_index_1_evict / collection_4_index_1_leaf)+collection_1_index_1_freq * (1- collection_1_index_1_evict / collection_1_index_1_leaf)+collection_1_index_2_freq * (1- collection_1_index_2_evict / collection_1_index_2_leaf)+collection_1_index_3_freq * (1- collection_1_index_3_evict / collection_1_index_3_leaf)+collection_3_index_2_freq * (1- collection_3_index_2_evict / collection_3_index_2_leaf)+collection_3_index_3_freq * (1- collection_3_index_3_evict / collection_3_index_3_leaf)+collection_4_index_2_freq * (1- collection_4_index_2_evict / collection_4_index_2_leaf)+collection_4_index_3_freq * (1- collection_4_index_3_evict / collection_4_index_3_leaf)),
	]) 
	j.options.SOLVER = 1 
	j.solve() 
	dict = {} 
	dict['collection_1'] = 1 - collection_1_evict.value[0] / collection_1_leaf
	dict['collection_2'] = 1 - collection_2_evict.value[0] / collection_2_leaf
	dict['collection_3'] = 1 - collection_3_evict.value[0] / collection_3_leaf
	dict['collection_4'] = 1 - collection_4_evict.value[0] / collection_4_leaf
	dict['collection_2_index_1'] = 1 - collection_2_index_1_evict.value[0] / collection_2_index_1_leaf
	dict['collection_3_index_1'] = 1 - collection_3_index_1_evict.value[0] / collection_3_index_1_leaf
	dict['collection_4_index_1'] = 1 - collection_4_index_1_evict.value[0] / collection_4_index_1_leaf
	dict['collection_1_index_1'] = 1 - collection_1_index_1_evict.value[0] / collection_1_index_1_leaf
	dict['collection_1_index_2'] = 1 - collection_1_index_2_evict.value[0] / collection_1_index_2_leaf
	dict['collection_1_index_3'] = 1 - collection_1_index_3_evict.value[0] / collection_1_index_3_leaf
	dict['collection_3_index_2'] = 1 - collection_3_index_2_evict.value[0] / collection_3_index_2_leaf
	dict['collection_3_index_3'] = 1 - collection_3_index_3_evict.value[0] / collection_3_index_3_leaf
	dict['collection_4_index_2'] = 1 - collection_4_index_2_evict.value[0] / collection_4_index_2_leaf
	dict['collection_4_index_3'] = 1 - collection_4_index_3_evict.value[0] / collection_4_index_3_leaf

	return dict
