extends Area2D
class_name Pitfall

@onready var animation = get_node("AnimatedSprite2D")
@onready var hole = $CollisionShape2D
signal fallen_down(fallPos)
var center_offset = Vector2(-5, -40)

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("default")

func _on_body_entered(body):
	if body.name == "Player":
		fallen_down.emit(global_position + center_offset)
