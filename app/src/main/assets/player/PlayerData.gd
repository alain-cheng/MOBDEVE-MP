extends Node

#Contains the information pulled from the database. Change to const
var initHealth
var initBounty
var initTaboo
var initTabooBonus
var initLuck
var initBountyBonus

#Contains a copy of the above information for the game to manipulate
#Default values listed
var health = 5
var bounty = 0
var taboo = 0
var tabooBonus = 0
var luck = 0
var bountyBonus = 0

#Contains other playerData that does not come from the app
var speed = 300.0
var friction = speed #At default player stops instantly, change for slippery surfaces
var lastFloor = false #Determines if the endpoint is a win. Change at last floor.
var floorsOnRun = [] #Array that contains what floors the player will go through

#Floor arrays
#Kapre Forest
var group1 = ["res://floors/Dungeon_A/1.tscn"]
#Dragon Tower
var group2 = ["res://floors/Dungeon_A/2.tscn"]
#The Furnace
var group3 = ["res://floors/Dungeon_A/3.tscn"]
#Crimson Manor
var final1 = ["res://floors/Dungeon_A/9_1.tscn"]

var rng = RandomNumberGenerator.new()
const PHASE_1 = 6
const PHASE_2 = 24
const PHASE_3 = 36
const PHASE_4 = 51

func checkLastFloor():
	if floorsOnRun.size() <= 0:
		lastFloor = true
		
func dungeonFloorMovement():
	#Move to another scene based on floorsOnRun array
	rng.randomize()
	PlayerData.checkLastFloor() #Check if lastfloor next
	
	if lastFloor: #If so, move to last floor
		#TODO: If-else based on taboo, starting from the top e.g. taboo >= PHASE_4
		get_tree().change_scene_to_file(final1[rng.randi_range(0, final1.size()-1)])
	
	#Else, match case for next floor
	else:
		match floorsOnRun[0]:
			1:
				floorsOnRun.pop_front()
				get_tree().change_scene_to_file(group1[rng.randi_range(0,group1.size()-1)])
			2:
				floorsOnRun.pop_front()
				get_tree().change_scene_to_file(group2[rng.randi_range(0,group2.size()-1)])
			3:
				floorsOnRun.pop_front()
				get_tree().change_scene_to_file(group3[rng.randi_range(0,group3.size()-1)])

func initData():
	#Check if player_data.json exists
	if(FileAccess.file_exists("user://player_data.json")):
		var file = FileAccess.get_file_as_string("user://player_data.json")
		var player_dict = JSON.parse_string(file)
		
		#Assign initial data
		initHealth = player_dict.get("health")
		initBounty = player_dict.get("bounty")
		initTaboo = player_dict.get("taboo")
		initTabooBonus = player_dict.get("tabooBonus")
		initLuck = player_dict.get("luck")
		initBountyBonus = player_dict.get("bountyBonus")
		
		#Copy data to manipulable variables
		health = initHealth
		bounty = initBounty
		taboo = initTaboo
		tabooBonus = initTabooBonus
		luck = initLuck
		bountyBonus = initBountyBonus
