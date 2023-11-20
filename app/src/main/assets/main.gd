extends Node2D

const PHASE_1 = 6
const PHASE_2 = 24
const PHASE_3 = 36
const PHASE_4 = 51
var DUNGEON_NUM_FLOORS = 2 #Number of Non-Final Floors. Test only
var rng = RandomNumberGenerator.new()

# Called when the node enters the scene tree for the first time.
func _ready():
	#Use this scene to load data from database and perform logic of
	#selecting the random floor levels. Maybe use a naming convention
	#for floors like "floor_b(floor_number)_(variation)(a,b,c... for difficulty levels)"
	
	#Initialize data
	PlayerData.initData()
	
	#TODO: Remove this debugging tool later
	#PlayerData.lastFloor = true
	#get_tree().change_scene_to_file("res://floors/Dungeon_A/4.tscn")
	#return
	#---------------------------------------
	
	#TODO: Set num floors based on taboo gauge
	if(PlayerData.taboo >= PHASE_3):
		DUNGEON_NUM_FLOORS = 4
	elif(PlayerData.taboo >= PHASE_1):
		DUNGEON_NUM_FLOORS = 3

	#Append int for all floors that will be randomized
	for i in range(DUNGEON_NUM_FLOORS):
		PlayerData.floorsOnRun.append(i+1)
		
	#TODO: Add code for dungeon shortening set bonus, maybe pop the first floor?
		
	#Shuffle all floors
	for i in range(DUNGEON_NUM_FLOORS):
		var swap_val = PlayerData.floorsOnRun[i]
		var swap_idx = rng.randi_range(i, DUNGEON_NUM_FLOORS-1)

		PlayerData.floorsOnRun[i] = PlayerData.floorsOnRun[swap_idx]
		PlayerData.floorsOnRun[swap_idx] = swap_val
		
	PlayerData.dungeonFloorMovement()
