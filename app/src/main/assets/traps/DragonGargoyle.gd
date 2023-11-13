extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@export var projectile: PackedScene = preload("res://traps/Projectiles/fire_projectile_1.tscn")
var attack = false
var triggers = 99 # number of times trap can shoot


# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("idle")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	# Can only attack if player is in its kill zone and the cooldown is reached
	if attack == true:
		animation.play("attack")
		if cooldown.is_stopped():
			fire()
	else:
		animation.play("idle")


func fire():
	if projectile && triggers > 0:
		var p = projectile.instantiate()
		get_tree().current_scene.add_child(p)
		p.global_position = self.global_position
		triggers -= 1
		cooldown.start()
	else:
		print("Trap cannot be triggered")


# When Player enters detection zone
func _on_player_detection_body_entered(body):
	if body.name == "Player" && triggers > 0:
		attack = true
		


func _on_player_detection_body_exited(body):
	if body.name == "Player":
		attack = false
