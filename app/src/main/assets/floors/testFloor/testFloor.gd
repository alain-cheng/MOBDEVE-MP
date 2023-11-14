extends Node2D

#Store nodes in vars
@onready var spikes1 = [get_node("Spikes")]
@onready var dragons = [get_node("DragonGargoyle"), get_node("DragonGargoyle2")] 
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2")]
@onready var mananaggal = [get_node("Manananggal")]
@onready var kapre = [get_node("Kapre")]
@onready var player = get_node("Player")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Connect damage signals programatically in a loop?
	
	#Pass player to projectile traps in loop
	for dragon in dragons:
		dragon.player = player
	for gDragon in gDragons:
		gDragon.player = player
	for m in mananaggal:
		m.player = player
	for k in kapre:
		k.player = player
	
	#Declare timers here at on ready
	#Spikes Timers
	for spike in spikes1:
		spike.timer.wait_time = 2.0
		spike.timer.one_shot = false
		spike.timer.start()
	#End Spikes Timers


func _on_end_point_change_floor():
	#Move to another scene randomly
	PlayerData.lastFloor = true
	get_tree().change_scene_to_file("res://floors/testFloor/testFloor2.tscn")
