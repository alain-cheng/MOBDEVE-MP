extends Node2D

@onready var spikes1 = get_node("Spikes")
@onready var dragon1 = get_node("DragonGargoyle")
@onready var dragon2 = get_node("DragonGargoyle2")
@onready var player = get_node("Player")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Pass player to projectile traps
	dragon1.player = player
	dragon2.player = player
	
	#Declare timers here at on ready
	#Spikes Timers
	spikes1.timer.wait_time = 2.0
	spikes1.timer.one_shot = false
	spikes1.timer.start()
	#End Spikes Timers


func _on_end_point_change_floor():
	#Move to another scene randomly
	PlayerData.lastFloor = true
	get_tree().change_scene_to_file("res://floors/testFloor/testFloor2.tscn")
