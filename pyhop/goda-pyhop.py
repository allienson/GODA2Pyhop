from __future__ import print_function
from pyhop import *

import goda_operators
import goda_methods

print('')
print_operators()

print('')
print_methods()

state = State('Mobee Mobile')
state.objects = {\
'T1.1:_Render_view':True,\
'T1.21:_Validate_data':True,\
'T1.22:_Post_data':True,\
'T1.2:_Process_data':False,\
'T1.3:_Update_view':True,\
'T1:_Process_modification':False,\
'G8:_Modification_is_collected':False,\
'T2.1:_Render_view':True,\
'T2.21:_Validate_data':True,\
'T2.22:_Post_data':True,\
'T2.2:_Process_data':False,\
'T2:_Process_qualification':False,\
'G9:_Qualification_is_collected':False,\
'G3:_Manual_data_is_sent':False,\
'T3.11:_Fetch_GPS':True,\
'T3.12:_Fetch_triangulation':False,\
'T3.1:_Fetch_geolocation':False,\
'T3.21:_Validate_data':True,\
'T3.22:_Post_data':True,\
'T3.2:_Process_data':False,\
'T3:_Track_line_locator':False,\
'G10:_Line_locations_tracked':False,\
'G4:_Automatic_data_is_sent':False,\
'G1:_Transport_info_is_shared':False}

pyhop(state, [\
('and_par', 'T1.2:_Process_data', 'T1.21:_Validate_data', 'T1.22:_Post_data'),\
('and_seq', 'T1:_Process_modification', 'T1.1:_Render_view', 'T1.2:_Process_data', 'T1.3:_Update_view'),\
('means_end', 'G8:_Modification_is_collected', 'T1:_Process_modification'),\
('and_seq', 'T2.2:_Process_data', 'T2.21:_Validate_data', 'T2.22:_Post_data'),\
('and_seq', 'T2:_Process_qualification', 'T2.1:_Render_view', 'T2.2:_Process_data'),\
('means_end', 'G9:_Qualification_is_collected', 'T2:_Process_qualification'),\
('and_par', 'G3:_Manual_data_is_sent', 'G8:_Modification_is_collected', 'G9:_Qualification_is_collected'),\
('xor', 'T3.1:_Fetch_geolocation', 'T3.11:_Fetch_GPS', 'T3.12:_Fetch_triangulation'),\
('and_seq', 'T3.2:_Process_data', 'T3.21:_Validate_data', 'T3.22:_Post_data'),\
('try_op', 'T3:_Track_line_locator', 'T3.1:_Fetch_geolocation', 'T3.2:_Process_data', 'skip'),\
('means_end', 'G10:_Line_locations_tracked', 'T3:_Track_line_locator'),\
('means_end', 'G4:_Automatic_data_is_sent', 'G10:_Line_locations_tracked'),\
('and_par', 'G1:_Transport_info_is_shared', 'G3:_Manual_data_is_sent', 'G4:_Automatic_data_is_sent'),\
], verbose=1)