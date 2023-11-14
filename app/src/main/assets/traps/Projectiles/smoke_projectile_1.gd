extends Area2D

@onready var duration = $Duration
var damage = 1
var SPEED = 100


func _ready():
	duration.start()


func _physics_process(delta):
	var direction = Vector2.UP.rotated(rotation)
	global_position += SPEED * direction * delta


func hurt():
	# damages player
	pass


func _on_area_entered(area):
	pass


func _on_body_entered(body):
	if body.name == "Player":
		print("Collided with Player")
		hurt()
	#if body.is_in_group("Mob") == true:
		#print("Collided with a Mob")
		#destroy()


func _on_visible_on_screen_notifier_2d_screen_exited():
	queue_free()


func _on_duration_timeout():
	queue_free()
