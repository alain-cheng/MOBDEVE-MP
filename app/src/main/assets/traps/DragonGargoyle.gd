extends CharacterBody2D

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@onready var detection = $PlayerDetection/CollisionShape2D
@onready var soundFire = $FireSound
@export var projectile: PackedScene = preload("res://traps/Projectiles/fire_projectile_1.tscn")
var attack = false
var speed = 250

# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("attack")
	cooldown.start()


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
	if projectile:
		var p = projectile.instantiate()
		get_tree().current_scene.add_child(p)
		p.global_position = self.global_position + Vector2(-60*self.scale.x, 5*self.scale.y)
		p.parent_scale = self.scale
		p.apply_scale(self.scale)
		p.SPEED = speed
		p.damage_taken.connect(player.on_damage_taken)
		soundFire.play()
		cooldown.start()


# When Player enters detection zone
func _on_player_detection_entered(area):
	if area.name == "PlayerHurtbox":
		attack = true
		


func _on_player_detection_exited(area):
	if area.name == "PlayerHurtbox":
		attack = false
