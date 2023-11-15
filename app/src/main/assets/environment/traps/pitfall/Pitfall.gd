extends Area2D

@onready var animation = get_node("AnimatedSprite2D")
var coveredTrap = false
signal fallen_down

# Called when the node enters the scene tree for the first time.
func _ready():
	#Maybe add a hidden animation for harder traps
	if !coveredTrap:
		animation.play("default")

func _on_body_entered(body):
	if body.name == "Player":
		fallen_down.emit()
