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
