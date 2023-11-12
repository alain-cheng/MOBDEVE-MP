extends CharacterBody2D

@onready var animation = get_node("PlayerSprite")
@onready var buttons = get_node("PlayerCamera/DirectionalButtons")
@onready var collider = get_node("EnvironmentCollider")
@onready var hurtbox = get_node("PlayerHurtbox/HurtboxColliider")
var backPressed = false
var isDed = false

func _ready():
	#Play Idle anim on load
	animation.play("idle")

func _physics_process(_delta):
	if(!isDed):
		#MOVEMENT
		#If player is standing still, play idle
		if velocity.x == 0 && velocity.y == 0 && PlayerData.health > 0:
			animation.play("idle")
		
		#Vars for detecting movement
		var xDirection = Input.get_axis("ui_left", "ui_right")
		var yDirection = Input.get_axis("ui_up", "ui_down")
		
		#x-axis
		if xDirection && !yDirection && PlayerData.health > 0:
			#x Directional anim
			if xDirection > 0:
				animation.play("right")
			else:
				animation.play("left")
			
			velocity.x = xDirection * PlayerData.speed
		else:
			velocity.x = move_toward(velocity.x, 0, PlayerData.friction)
			
		#y-axis
		if yDirection && !xDirection && PlayerData.health > 0:
			#y Directional anim
			if yDirection > 0:
				animation.play("down")
			else:
				animation.play("up")
			
			velocity.y = yDirection * PlayerData.speed
		else:
			velocity.y = move_toward(velocity.y, 0, PlayerData.friction)
			
		#Diagonal movement
		if yDirection && xDirection && PlayerData.health > 0:
			#y Directional anim
			if yDirection > 0:
				animation.play("down")
			else:
				animation.play("up")
			
			velocity.x = xDirection * PlayerData.speed
			velocity.y = yDirection * PlayerData.speed

		move_and_slide()
		#END MOVEMENT
	elif(isDed):
		#Animation for floating ghost
		velocity.x = 0
		velocity.y = -25
		move_and_slide()

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

func on_damage_taken():
	#Toggle booleans
	isDed = true
	collider.disabled = true
	hurtbox.disabled = true
	
	PlayerData.health = PlayerData.health - 1
	if PlayerData.health <= 0: #Game over
		ded()
		await get_tree().create_timer(3.5).timeout #Base on anims
		get_tree().quit()
	elif PlayerData.health > 0: #-1 Life
		ded()
		await get_tree().create_timer(3.5).timeout #Base on anims
		get_tree().reload_current_scene()

func ded():
	buttons.hide()
	animation.play("death")
	#TODO: Add popup for dying
