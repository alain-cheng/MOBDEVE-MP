extends Area2D

@onready var animation = get_node("AnimatedSprite2D")
var isExtended = false
var player_in = false

#Signals
signal damage_taken

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("retracted")

func _on_timeout():
	if(!isExtended):
		animation.play("extended")
		isExtended = !isExtended
	else:
		animation.play("retracted")
		isExtended = !isExtended

func _physics_process(_delta):
	if(isExtended && player_in):
		damage_taken.emit()

func _on_body_entered(body):
	if body.name == "Player":
		player_in = true

func _on_body_exited(body):
	if body.name == "Player":
		player_in = false
