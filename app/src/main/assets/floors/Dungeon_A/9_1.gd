extends Node2D

@onready var player = get_node("Player")
@onready var endpoints = [get_node("EndPoint"), get_node("EndPoint2")]
var rng = RandomNumberGenerator.new()
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2"),
 get_node("GoldenDragon3")]
@onready var dragons1 = [get_node("DragonGargoyle"), get_node("DragonGargoyle3"), 
get_node("DragonGargoyle4"), get_node("DragonGargoyle5")]
@onready var dragons2 = [get_node("DragonGargoyle2")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2")]

# Called when the node enters the scene tree for the first time.
func _ready():
	$Player/FloorNamePanel.show()
	$Player/FloorNamePanel/LabelAnim.play("FloorName_FadeInOut")
	
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
		gDragon.delay = 0.7
	gDragons[1].delay = 0.1 #Shorter for this one
	gDragons[1].speed = 700
	for d in dragons1:
		d.player = player
		d.cooldown.wait_time = 1.75
	for d in dragons2:
		d.player = player
		d.cooldown.wait_time = 3.25
	for k in kapre:
		k.player = player
		k.cooldown.wait_time = 6.5
	kapre[1].cooldown.wait_time = 2.5

	for n in self.get_children():
		if n is Pitfall:
			n.fallen_down.connect(player.ive_fallen)
		if n is Spikes:
			n.damage_taken.connect(player.on_damage_taken)
			n.timer.wait_time = 1.875
			n.timer.start()
