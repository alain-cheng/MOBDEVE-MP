extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@export var beam: PackedScene = preload("res://traps/Projectiles/manananggal_tongue.tscn")
var attack = false

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	if attack == true:
		animation.play("attack")
		if cooldown.is_stopped():
			fire()
	else:
		animation.play("idle")


func fire():
	if beam:
		var b = beam.instantiate()
		get_tree().current_scene.add_child(b)
		b.global_position = self.global_position
		cooldown.start()


func _on_player_detection_body_entered(body):
	if body.name == "Player":
		attack = true


func _on_player_detection_body_exited(body):
	if body.name == "Player":
		attack = false
