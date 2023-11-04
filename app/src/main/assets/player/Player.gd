extends CharacterBody2D

func _ready():
	#Play Idle anim
	get_node("AnimatedSprite2D").play("idle")

func _physics_process(delta):	
	#MOVEMENT
	#x-axis
	var xDirection = Input.get_axis("ui_left", "ui_right")
	if xDirection:
		#x Directional anim
		if xDirection > 0:
			get_node("AnimatedSprite2D").play("right")
		else:
			get_node("AnimatedSprite2D").play("left")
		
		velocity.x = xDirection * PlayerData.speed
	else:
		velocity.x = move_toward(velocity.x, 0, PlayerData.speed)
	
	#y-axis
	var yDirection = Input.get_axis("ui_up", "ui_down")
	if yDirection:
		#y Directional anim
		if yDirection > 0:
			get_node("AnimatedSprite2D").play("down")
		else:
			get_node("AnimatedSprite2D").play("up")
		
		velocity.y = yDirection * PlayerData.speed
	else:
		velocity.y = move_toward(velocity.y, 0, PlayerData.speed)

	move_and_slide()
	#END MOVEMENT
