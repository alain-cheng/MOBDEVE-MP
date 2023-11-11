extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")
@onready var tongue = $PlayerDetection/CollisionShape2D/Tongue
var attack = false

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	if attack == true:
		animation.play("attack")
		tongue.visible = not tongue.visible
	else:
		animation.play("idle")
		tongue.visible = false


func _on_player_detection_body_entered(body):
	if body.name == "Player":
		attack = true


func _on_player_detection_body_exited(body):
	if body.name == "Player":
		attack = false
