extends Area2D

@onready var duration = $Duration
var damage = 1
var SPEED = 100
signal damage_taken(damage)

func _ready():
	duration.start()


func _physics_process(delta):
	var direction = Vector2.UP.rotated(rotation)
	global_position += SPEED * direction * delta

func _on_area_entered(area):
	if area.name == "PlayerHurtbox":
		damage_taken.emit(damage)

func _on_body_entered(body):
	pass #Probably use for envitonment collisions

func _on_visible_on_screen_notifier_2d_screen_exited():
	queue_free()


func _on_duration_timeout():
	queue_free()
