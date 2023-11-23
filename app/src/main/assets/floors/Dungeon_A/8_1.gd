extends Node2D

@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")
@onready var gDragons = [get_node("GoldenDragon"), get_node("GoldenDragon2"),
 get_node("GoldenDragon3"), get_node("GoldenDragon4"), get_node("GoldenDragon5")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2"), get_node("Kapre3")]
@onready var mananaggal2 = [get_node("Manananggal_v2"), get_node("Manananggal_v3")]

# Called when the node enters the scene tree for the first time.
func _ready():
	$Player/FloorNamePanel.show()
	$Player/FloorNamePanel/LabelAnim.play("FloorName_FadeInOut")
	
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(PlayerData.dungeonFloorMovement)
	endpoint.player = player
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonFloorMovement)
	
	#Init traps
	for n in self.get_children():
		if n is Pitfall:
			n.fallen_down.connect(player.ive_fallen)
			n.hole.apply_scale(Vector2(1.2, 1.0))
			n.hole.global_position = n.global_position + Vector2(-5, 10)
		if n is Spikes:
			n.damage_taken.connect(player.on_damage_taken)
			n.timer.wait_time = 1.05
			n.timer.start()
	for gDragon in gDragons:
		gDragon.player = player
		gDragon.delay = 0.95
	for k in kapre:
		k.player = player
		k.cooldown.wait_time = 4.0
	for m2 in mananaggal2:
		m2.damage_taken.connect(player.on_damage_taken)
		m2.delay = 0.1
