extends Node2D

# Called when the node enters the scene tree for the first time.
func _ready():
	#Use this scene to load data from database and perform logic of
	#selecting the random floor levels. Maybe use a naming convention
	#for floors like "floor_b(floor_number)_(variation)(a,b,c... for difficulty levels)"
	
	#Initialize data
	#PlayerData.initData()
	
	#Move to another scene based on taboo + tabooBonus
	PlayerData.lastFloor = true #DEBUG
	get_tree().change_scene_to_file("res://floors/testFloor/testFloor.tscn")#DEBUG REMOVE LATER


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass
