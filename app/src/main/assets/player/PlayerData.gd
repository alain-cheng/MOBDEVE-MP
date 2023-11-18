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
var health = 1
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

func checkLastFloor():
	if floorsOnRun.size() <= 0:
		lastFloor = true

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
