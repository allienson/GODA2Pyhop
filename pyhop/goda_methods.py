import pyhop

def and_seq(state, *plans):
	count = 0
	for i in range(1, len(plans)):
		plan = plans[i]
		if state.objects[plan] == True:
			count += 1
	if count == len(plans)-1:
		return [('completed', plans[0])]
	else:
		return [('not_completed', plans[0])]
pyhop.declare_methods('and_seq', and_seq)

def and_par(state, *plans):
	count = 0
	for i in range(1, len(plans)):
		plan = plans[i]
		if state.objects[plan] == True:
			count += 1
	if count == len(plans)-1:
		return [('completed', plans[0])]
	else:
		return [('not_completed', plans[0])]
pyhop.declare_methods('and_par', and_par)

def or_seq(state, *plans):
	count = 0
	for i in range(1, len(plans)):
		plan = plans[i]
		if state.objects[plan] == True:
			count += 1
	if count != 0:
		return [('completed', plans[0])]
	else:
		return [('not_completed', plans[0])]
pyhop.declare_methods('or_seq', or_seq)

def or_par(state, *plans):
	count = 0
	for i in range(1, len(plans)):
		plan = plans[i]
		if state.objects[plan] == True:
			count += 1
	if count != 0:
		return [('completed', plans[0])]
	else:
		return [('not_completed', plans[0])]
pyhop.declare_methods('or_par', or_par)

def k_times(state, plan, plan1, k):
	return [('completed', plan)]
pyhop.declare_methods('k_times', k_times)

def k_times_par(state, plan, plan1, k):

	return [('completed', plan)]
pyhop.declare_methods('k_times_par', k_times_par)

def k_tries(state, plan, plan1, k):
	return [('completed', plan)]

pyhop.declare_methods('k_tries', k_tries)

def opt(state, plan, plan1):
	return [('not_completed', plan)]

pyhop.declare_methods('opt', opt)

def try_op(state, plan, plan1, plan2, plan3):	
	if state.objects[plan1] == True:
		if plan2 == 'skip':
			return [('skip', plan2), ('not_completed', plan)]
		else:
			return [('completed', plan2), ('completed', plan)] 
	elif state.objects[plan] == False:
		if plan3 == 'skip':
			return [('skip', plan3), ('not_completed', plan)]
		else:
			return [('completed', plan2), ('completed', plan)] 
	else:
		return [('not_completed', plan)]

pyhop.declare_methods('try_op',try_op)


def xor(state, plan, plan1, plan2):
	if state.objects[plan1] == True:
		if state.objects[plan2] == True:
			return [('not_completed', plan)]
		else:
			state.objects[plan2] = False
			return [('completed', plan)]
	elif state.objects[plan2] == True:
		state.objects[plan1] = False
		return [('completed', plan)]
	else:
		return [('not_completed', plan)]

pyhop.declare_methods('xor', xor)

def means_end(state, plan, plan1):
	if state.objects[plan1] == True:
		return [('completed', plan)]
	else:
		return [('not_completed', plan)]

pyhop.declare_methods('means_end', means_end)