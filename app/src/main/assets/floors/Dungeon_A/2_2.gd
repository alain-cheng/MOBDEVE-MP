extends Node2D

@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")
@onready var dragons1 = [get_node("DragonGargoyle"), get_node("DragonGargoyle2")]
@onready var dragons2 = [get_node("DragonGargoyle3"), get_node("DragonGargoyle4")]
@onready var pitfalls = [get_node("Pitfall"), get_node("Pitfall2"), get_node("Pitfall3"), get_node("Pitfall4"), get_node("Pitfall5"), get_node("Pitfall6")]
@onready var spikes = [get_node("Spikes"), get_node("Spikes2"), get_node("Spikes3"), get_node("Spikes4"), get_node("Spikes5")]

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
	for d in dragons1: #faster
		d.player = player
		d.speed = 400
		d.cooldown.wait_time = 1.0
	for d in dragons2: #slower
		d.player = player
		d.cooldown.wait_time = 2.5
	for p in pitfalls:
		p.fallen_down.connect(player.ive_fallen)
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 1.25
		spike.timer.start()
