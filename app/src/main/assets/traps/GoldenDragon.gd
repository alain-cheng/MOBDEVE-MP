extends CharacterBody2D

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@onready var detection = $PlayerDetection/CollisionShape2D
@export var projectile: PackedScene = preload("res://traps/Projectiles/fire_projectile_2.tscn")
var attack = false
var speed = 500
#var triggers = 999 # number of times trap can shoot


# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("idle")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(_delta):
	# Can only attack if player is in its kill zone and the cooldown is reached
	if attack == true && !player.isDed:
		animation.play("attack")
		if cooldown.is_stopped():
			fire()
	else:
		animation.play("idle")


func fire():
	if projectile:
		var p = projectile.instantiate()
		get_tree().current_scene.add_child(p)
		p.global_position = self.global_position
		p.parent_scale = self.scale
		p.apply_scale(self.scale)
		p.SPEED = speed
		p.damage_taken.connect(player.on_damage_taken)
		cooldown.start()


# When Player enters detection zone
func _on_player_detection_entered(area):
	if area.name == "PlayerHurtbox":
		attack = true
		


func _on_player_detection_exited(area):
	if area.name == "PlayerHurtbox":
		attack = false
