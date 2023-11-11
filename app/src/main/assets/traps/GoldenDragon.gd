extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")
@onready var timer = get_node("Timer")
@onready var flame = $PlayerDetection/Flame
var attack = false

# Called when the node enters the scene tree for the first time.
func _ready():
	timer.set_wait_time(1)


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	if attack == true:
		timer.start()
		animation.play("attack")
		flame.visible = not flame.visible
	else:
		animation.play("idle")
		flame.visible = false


func _on_player_detection_body_entered(body):
	if body.name == "Player":
		attack = true


func _on_player_detection_body_exited(body):
	if body.name == "Player":
		attack = false


func _on_timer_timeout():
	pass # for now, since i couldnt figure out why the Timer node is not working
	if attack == true:
		animation.play("attack")
		flame.visible = not flame.visible
