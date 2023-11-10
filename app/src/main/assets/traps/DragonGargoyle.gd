extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")
var attack = false

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("idle")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	if attack == true:
		animation.play("attack")
		$PlayerDetection/Flame.visible = not $PlayerDetection/Flame.visible
	else:
		$PlayerDetection/Flame.visible = false
		animation.play("idle")


func _on_player_detection_body_entered(body):
	if body.name == "Player":
		attack = true


func _on_player_detection_body_exited(body):
	if body.name == "Player":
		attack = false
