# Parent Class for all child projectiles that behave like a bullet
class_name Projectile1 extends Area2D

@onready var duration = $Duration
var damage: int = 1
var SPEED: int = 100
signal damage_taken(damage)
var parent_scale: Vector2

# Called when the node enters the scene tree for the first time.
func _ready():
	duration.start()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(delta):
	var direction = Vector2.LEFT.rotated(rotation)
	if parent_scale.x < 0.0: #If parent is flipped in x axis
		global_position -= SPEED * direction * delta
	else:
		global_position += SPEED * direction * delta


func destroy():
	despawn()


func despawn():
	var tween = create_tween()
	tween.tween_property($Sprite2D, "modulate:a", 0, 0.1)
	tween.finished.connect(queue_free)


func _on_area_entered(area):
	if area.name == "PlayerHurtbox":
		damage_taken.emit(damage)
		destroy()
	if area.name == "Solids" || area.is_in_group("Solids"):
		destroy()


func _on_visible_on_screen_notifier_2d_screen_exited():
	despawn()


func _on_duration_timeout():
	despawn()
