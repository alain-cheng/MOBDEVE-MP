extends Node2D

@onready var pitfalls = [get_node("Pitfall")]
@onready var player = get_node("Player")

# Called when the node enters the scene tree for the first time.
func _ready():
	for p in pitfalls:
		p.fallen_down.connect(player.ive_fallen)
