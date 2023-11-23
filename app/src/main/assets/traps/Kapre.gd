extends CharacterBody2D

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@onready var soundSmoke = $SmokeSound
@export var projectile: PackedScene = preload("res://traps/Projectiles/smoke_projectile_1.tscn")

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("idle")


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(_delta):
	#Auto z_index
	if player.collider.global_position.y < global_position.y:
		z_index = 1
	else:
		z_index = -1
	
	if cooldown.is_stopped():
		fire()

func fire():
	var p = projectile.instantiate()
	get_tree().current_scene.add_child(p)
	p.global_position = self.global_position
	p.damage_taken.connect(player.on_damage_taken)
	soundSmoke.play()
	cooldown.start()
