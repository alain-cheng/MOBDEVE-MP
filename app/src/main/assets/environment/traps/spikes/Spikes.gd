extends Area2D
class_name Spikes

@onready var animation = get_node("AnimatedSprite2D")
@onready var timer = get_node("Timer")
@onready var soundSpike = $SpikeSound
var damage = 1
var isExtended = false
var player_in = false

#Signals
signal damage_taken(damage)

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("retracted")

func _on_timeout():
	if(!isExtended):
		animation.play("extended")
		isExtended = !isExtended
		soundSpike.play()
	else:
		animation.play("retracted")
		isExtended = !isExtended

func _physics_process(_delta):
	if(isExtended && player_in):
		damage_taken.emit(damage)

func _on_body_entered(body):
	if body.name == "Player":
		player_in = true

func _on_body_exited(body):
	if body.name == "Player":
		player_in = false
