extends Node2D

#const PHASE_1 = 6
#const PHASE_2 = 24
#const PHASE_3 = 36
#const PHASE_4 = 51
#var DUNGEON_NUM_FLOORS = 2
#var rng = RandomNumberGenerator.new()

func _ready():
	PlayerData.initData()

	# MAIN MENU ENTRY
	get_tree().change_scene_to_file("res://MainMenu/MainMenu.tscn")
	return

	# ---------------------------------------
	# ORIGINAL GAME LOGIC (disabled for now)
	#if(PlayerData.taboo >= PHASE_3):
		#DUNGEON_NUM_FLOORS = 4
	#elif(PlayerData.taboo >= PHASE_1):
		#DUNGEON_NUM_FLOORS = 3
	#else:
		#DUNGEON_NUM_FLOORS = 2
#
	#for i in range(DUNGEON_NUM_FLOORS):
		#PlayerData.floorsOnRun.append(i+1)
#
	#for i in range(DUNGEON_NUM_FLOORS):
		#var swap_val = PlayerData.floorsOnRun[i]
		#var swap_idx = rng.randi_range(i, DUNGEON_NUM_FLOORS-1)
		#PlayerData.floorsOnRun[i] = PlayerData.floorsOnRun[swap_idx]
		#PlayerData.floorsOnRun[swap_idx] = swap_val
#
	#if PlayerData.setBonus[1] == '1':
		#PlayerData.floorsOnRun.pop_front()
#
	#PlayerData.dungeonFloorMovement()
	# ---------------------------------------
