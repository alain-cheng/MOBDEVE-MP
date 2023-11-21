extends Node2D

@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")
@onready var spikes = [get_node("Spikes"), get_node("Spikes2")]
@onready var pitfalls = [get_node("Pitfall")]
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2"),
 get_node("GoldenDragon3")]
@onready var dragons = [get_node("DragonGargoyle")]
@onready var mananaggal2 = [get_node("Manananggal_v2")]

# Called when the node enters the scene tree for the first time.
func _ready():
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(PlayerData.dungeonFloorMovement)
	endpoint.player = player
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonFloorMovement)
	
	#Init Traps
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 1.0
		spike.timer.start()
	for p in pitfalls:
		p.fallen_down.connect(player.ive_fallen)
	for gDragon in gDragons:
		gDragon.player = player
		gDragon.delay = 0.5
		gDragon.speed = 750
	gDragons[2].delay = 1.0
	for d in dragons:
		d.player = player
		d.speed = 300
		d.cooldown.wait_time = 1.5
	for m2 in mananaggal2:
		m2.damage_taken.connect(player.on_damage_taken)
		m2.delay = 0.3
