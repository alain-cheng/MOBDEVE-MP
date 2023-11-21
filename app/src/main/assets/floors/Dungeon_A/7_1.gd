extends Node2D

@onready var player = get_node("Player")
@onready var endpoint = get_node("EndPoint")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Connect endpoint to change floor
	endpoint.changeFloor.connect(PlayerData.dungeonFloorMovement)
	endpoint.player = player
	#Connect player to change floor
	player.fall_to_next.connect(PlayerData.dungeonFloorMovement)
	
	#Dev Note: Man, I wish I learned this earlier...
	#Init traps
	for n in self.get_children():
		if n is Pitfall:
			n.fallen_down.connect(player.ive_fallen)
		if n is Spikes:
			n.damage_taken.connect(player.on_damage_taken)
			n.timer.wait_time = 1.05
			n.timer.start()
