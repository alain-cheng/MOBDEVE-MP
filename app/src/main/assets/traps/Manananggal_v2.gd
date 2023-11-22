extends CharacterBody2D
class_name Manananggal

@onready var player
@onready var animation = get_node("AnimatedSprite2D")
@onready var detection = $PlayerDetection/CollisionShape2D
@onready var soundScream = $ScreamSound
signal damage_taken(damage)
var attack = false
var damage: int = 1
var delay = 1.0
var despawn = 3.0
var SPEED = 1000

# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(delta):
	if attack == true:
		await get_tree().create_timer(delay).timeout #Firing delay
		var direction = Vector2.DOWN.rotated(rotation)
		global_position += SPEED * direction * delta
	else:
		animation.play("idle")

	
func _on_player_detection_area_entered(area):
	if area.name == "PlayerHurtbox":
		var tween = create_tween()
		tween.tween_property($AnimatedSprite2D, "modulate:a", 0, 0.2)
		tween.tween_property($AnimatedSprite2D, "modulate:a", 255, 0.2)
		tween.tween_property($AnimatedSprite2D, "modulate:b", 0, 0.2)
		attack = true
		animation.play("attack")
		soundScream.play()
		


func _on_visible_on_screen_notifier_2d_screen_exited():
	if attack:
		await get_tree().create_timer(despawn).timeout
		queue_free()


func _on_killzone_area_entered(area):
	if area.name == "PlayerHurtbox" && attack == true:
		emit_signal("damage_taken", damage)
