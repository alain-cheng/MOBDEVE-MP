extends Node2D

@onready var player = get_node("Player")
@onready var endpoints = [get_node("EndPoint"), get_node("EndPoint2")]
var rng = RandomNumberGenerator.new()
@onready var pitfalls = [get_node("Pitfall"), get_node("Pitfall2"), 
get_node("Pitfall3"), get_node("Pitfall4"), get_node("Pitfall5"), get_node("Pitfall6")]
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2"),
 get_node("GoldenDragon3")]
@onready var dragons1 = [get_node("DragonGargoyle"), get_node("DragonGargoyle3"), 
get_node("DragonGargoyle4"), get_node("DragonGargoyle5")]
@onready var dragons2 = [get_node("DragonGargoyle2")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2")]
@onready var spikes = [get_node("Spikes"), get_node("Spikes2"),
 get_node("Spikes3"), get_node("Spikes4"), get_node("Spikes5")]

# Called when the node enters the scene tree for the first time.
func _ready():
	rng.randomize()
	#Connect endpoint to change floor
	for e in endpoints:
		e.changeFloor.connect(PlayerData.dungeonFloorMovement)
		e.player = player
	#Remove one endpoint
	var index = rng.randi_range(0, 1)
	endpoints[index].call_deferred("queue_free")
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonFloorMovement)
	
	#Init traps
	for gDragon in gDragons:
		gDragon.player = player
		gDragon.delay = 0.9
	gDragons[1].delay = 0.1 #Shorter for this one
	for p in pitfalls:
		p.fallen_down.connect(player.ive_fallen)
	for d in dragons1:
		d.player = player
		d.cooldown.wait_time = 2.0
	for d in dragons2: #Dragons on top route
		d.player = player
		d.cooldown.wait_time = 4.0
	for k in kapre:
		k.player = player
		k.cooldown.wait_time = 8.0
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 2.5
		spike.timer.start()
