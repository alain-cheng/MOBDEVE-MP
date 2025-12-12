extends Node2D

@onready var spikes = [get_node("Spikes"), get_node("Spikes2"), get_node("Spikes3"), get_node("Spikes4"), get_node("Spikes5")]
@onready var kapre = [get_node("Kapre"), get_node("Kapre2"), get_node("Kapre3")]
@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")

# Called when the node enters the scene tree for the first time.
func _ready():
	$Player/FloorNamePanel.show()
	$Player/FloorNamePanel/LabelAnim.play("FloorName_FadeInOut")
	
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(PlayerData.dungeonFloorMovement)
	endpoint.player = player
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonFloorMovement)
	
	#Init Traps
	for spike in spikes:
		spike.damage_taken.connect(player.on_damage_taken)
		spike.timer.wait_time = 1.25
		spike.timer.start()

	# Assign different intervals
	kapre[0].cooldown.wait_time = 3.5
	kapre[1].cooldown.wait_time = 2.0
	kapre[2].cooldown.wait_time = 2.0

	# Start timers with DIFFERENT OFFSETS
	kapre[0].cooldown.start( randf_range(0.0, 3.5) )
	kapre[1].cooldown.start( randf_range(0.0, 2.0) )
	kapre[2].cooldown.start( randf_range(0.0, 2.0) )


