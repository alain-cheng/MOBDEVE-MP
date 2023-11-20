extends CharacterBody2D

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var cooldown = $AttackTimer
@onready var detection = $PlayerDetection/CollisionShape2D
@export var beam: PackedScene = preload("res://traps/Projectiles/manananggal_tongue.tscn")
var attack = false
var delay = 1.0

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(_delta):
	if attack == true && !player.isDed:
		animation.play("attack")
		await get_tree().create_timer(delay).timeout #Firing delay
		if cooldown.is_stopped() &&  attack == true && !player.isDed: #Dont fire if player leaves
			fire()
	else:
		animation.play("idle")


func fire():
	if beam:
		var b = beam.instantiate()
		get_tree().current_scene.add_child(b)
		b.global_position = self.global_position
		b.damage_taken.connect(player.on_damage_taken)
		cooldown.start()


# When Player enters detection zone
func _on_player_detection_entered(area):
	if area.name == "PlayerHurtbox":
		attack = true
		

func _on_player_detection_exited(area):
	if area.name == "PlayerHurtbox":
		attack = false
