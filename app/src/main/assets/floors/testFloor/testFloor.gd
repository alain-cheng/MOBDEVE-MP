extends Node2D

#Store nodes in vars
@onready var spikes1 = [get_node("Spikes")]
@onready var dragons = [get_node("DragonGargoyle"), get_node("DragonGargoyle2")] 
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2")]
@onready var mananaggal = [get_node("Manananggal")]
@onready var kapre = [get_node("Kapre")]
@onready var pitfalls = [get_node("Pitfall")]
@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")

# Called when the node enters the scene tree for the first time.
func _ready():
	endpoint.player = player
	#Init traps
	for dragon in dragons:
		dragon.player = player
	for gDragon in gDragons:
		gDragon.player = player
	for m in mananaggal:
		m.player = player
	for k in kapre:
		k.player = player
	for p in pitfalls:
		p.fallen_down.connect(player.ive_fallen)
	for spike in spikes1:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 2.0
		spike.timer.one_shot = false
		spike.timer.start()

func _on_change_floor():
	#Move to another scene randomly
	PlayerData.lastFloor = true
	get_tree().change_scene_to_file("res://floors/testFloor/testFloor2.tscn")
