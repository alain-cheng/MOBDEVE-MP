extends Projectile1

func _ready():
	damage = 1
	SPEED = 100
	duration.start()


# Overload
func _physics_process(delta):
	var direction = Vector2.UP.rotated(rotation)
	global_position += SPEED * direction * delta
