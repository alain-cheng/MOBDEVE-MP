extends CharacterBody2D

var backPressed = false

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

func _notification(what):
	if what == NOTIFICATION_WM_GO_BACK_REQUEST:
		if(!backPressed):
			backPressed = true
			ToastParty.show({
				"text": "PRESS BACK AGAIN TO QUIT",
				"bgcolor": Color(1, 1, 1, 0.7), # Background Color
				"color": Color(0, 0, 0, 1),     # Text Color
				"gravity": "bottom",               # top or bottom
				"direction": "center",           # left or center or right
			})
			await get_tree().create_timer(1.0).timeout
			backPressed = false
		elif backPressed:
			get_tree().quit()
