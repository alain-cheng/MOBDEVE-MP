extends Area2D

@onready var duration = $Duration
var damage = 1
var SPEED = 250
signal damage_taken(damage)


func _ready():
	duration.start()


func _physics_process(delta):
	var direction = Vector2.LEFT.rotated(rotation)
	global_position += SPEED * direction * delta


func destroy():
	queue_free()


func _on_area_entered(area):
	if area.name == "PlayerHurtbox":
		damage_taken.emit(damage)
		destroy()
	#if body.is_in_group("Mob") == true:
		#print("Collided with a Mob")
		#destroy()


func _on_visible_on_screen_notifier_2d_screen_exited():
	queue_free()


func _on_duration_timeout():
	queue_free()
