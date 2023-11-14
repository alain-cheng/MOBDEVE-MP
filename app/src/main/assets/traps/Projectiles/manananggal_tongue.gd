extends RayCast2D

var is_casting := false : set = set_is_casting
var damage = 1
signal damage_taken(damage)

# Called when the node enters the scene tree for the first time.
func _ready():
	set_process(true)
	$Line2D.points[1] = Vector2.ZERO


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _physics_process(_delta):
	var cast_point := target_position
	force_raycast_update()
	
	$Line2D.points[1] = Vector2.ZERO
	# If an object is intersecting with the ray's vector, set the point of impact
	if is_colliding():
		if get_collider().name == "Player":
			cast_point = to_local(get_collision_point())
			## Render the line up to the collision point
			## points[0] is the ray's point of origin, points[1] is the point of impact
			$Line2D.points[1] = cast_point
			set_is_casting(true)


func set_is_casting(cast: bool):
	is_casting = cast
	if is_casting == true:
		beam()
	#set_process(is_casting)


# Shoot
func beam():
	var tween = create_tween()
	#Interpolate
	tween.tween_property($Line2D, "width", 10, 0.2) #Appearance
	tween.tween_property($Line2D, "width", 0, 0.1)  #Disappearance
	tween.finished.connect(hit)


#Inflicts damage after tween finishes
func hit():
	damage_taken.emit(damage)
