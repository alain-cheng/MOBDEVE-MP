extends Node2D

@onready var spikes = [get_node("Spikes"), get_node("Spikes2"), get_node("Spikes3"), get_node("Spikes4")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2"), get_node("Kapre3")]
@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(PlayerData.dungeonAFloorMovement)
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonAFloorMovement)
	
	#Init Traps
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 1.25
		spike.timer.start()
	for k in kapre:
		k.player = player
		k.cooldown.wait_time = 2.8
