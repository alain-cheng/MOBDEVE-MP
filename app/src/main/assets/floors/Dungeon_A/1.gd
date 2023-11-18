extends Node2D

@onready var spikes = [get_node("Spikes"), get_node("Spikes2"), get_node("Spikes3"), get_node("Spikes4")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2"), get_node("Kapre3")]
@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(_on_change_floor)
	
	#Init Traps
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 1.25
		spike.timer.start()
	for k in kapre:
		k.player = player
		k.cooldown.wait_time = 2.8

func _on_change_floor():
	#Move to another scene based on floorsOnRun array
	PlayerData.checkLastFloor() #Check if lastfloor next
	
	if PlayerData.lastFloor: #If so, move to last floor
		pass #TODO: Fill this in later
	
	#Else, match case for next floor
	match PlayerData.floorsOnRun[0]:
		1:
			PlayerData.floorsOnRun.pop_front()
			PlayerData.checkLastFloor() #TODO: DEBUG REMOVE
			get_tree().change_scene_to_file("res://floors/Dungeon_A/1.tscn")
