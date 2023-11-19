extends Node2D

const PHASE_1 = 6
const PHASE_2 = 24
const PHASE_3 = 36
const PHASE_4 = 51
const DUNGEON_A_NUM_FLOORS = 2 #Number of "Tutorial" Floors
var rng = RandomNumberGenerator.new()

# Called when the node enters the scene tree for the first time.
func _ready():
	#Use this scene to load data from database and perform logic of
	#selecting the random floor levels. Maybe use a naming convention
	#for floors like "floor_b(floor_number)_(variation)(a,b,c... for difficulty levels)"
	
	#Initialize data
	PlayerData.initData()
	#get_tree().change_scene_to_file("res://environment/interactive/win_popup/Win_Popup.tscn") #Debug
	
	#Move to another scene based on taboo
	if(true): #TODO: true is DEBUG. PHASE 0, Taboo < 6
		#Append int for all floors that will be randomized
		for i in range(DUNGEON_A_NUM_FLOORS):
			PlayerData.floorsOnRun.append(i+1)
			
		#TODO: Add code for dungeon shortening set bonus, maybe pop the first floor?
		
		#Shuffle all floors
		for i in range(DUNGEON_A_NUM_FLOORS):
			var swap_val = PlayerData.floorsOnRun[i]
			var swap_idx = rng.randi_range(i, DUNGEON_A_NUM_FLOORS-1)

			PlayerData.floorsOnRun[i] = PlayerData.floorsOnRun[swap_idx]
			PlayerData.floorsOnRun[swap_idx] = swap_val
		
		match PlayerData.floorsOnRun[0]:
			1:
				PlayerData.floorsOnRun.pop_front()
				PlayerData.checkLastFloor()
				get_tree().change_scene_to_file("res://floors/Dungeon_A/1.tscn")
			2:
				PlayerData.floorsOnRun.pop_front()
				PlayerData.checkLastFloor()
				get_tree().change_scene_to_file("res://floors/Dungeon_A/2.tscn")
