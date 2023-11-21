extends CharacterBody2D

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@onready var detection = $PlayerDetection/CollisionShape2D
@export var projectile: PackedScene = preload("res://traps/Projectiles/fire_projectile_2.tscn")
var attack = false
var delay = 1.0
var cool_time = 0.05 #Shoots a stream of fire
var speed = 500


# Called when the node enters the scene tree for the first time.
func _ready():
	animation.play("idle")
	cooldown.wait_time = cool_time
	cooldown.one_shot = false
	cooldown.timeout.connect(fire)


func _physics_process(_delta):
	if !attack:
		animation.play("idle")

func fire():
	if projectile:
		var p = projectile.instantiate()
		get_tree().current_scene.add_child(p)
		p.global_position = self.global_position + Vector2(-40*self.scale.x, -10*self.scale.y)
		p.parent_scale = self.scale
		p.apply_scale(self.scale)
		p.SPEED = speed
		p.damage_taken.connect(player.on_damage_taken)


# When Player enters detection zone
func _on_player_detection_entered(area):
	if area.name == "PlayerHurtbox":
		attack = true
		animation.play("attack")
		await get_tree().create_timer(delay).timeout #Firing delay
		cooldown.start()
		


func _on_player_detection_exited(area):
	if area.name == "PlayerHurtbox":
		await get_tree().create_timer(delay*2).timeout #Firing delay
		attack = false
		cooldown.stop()
