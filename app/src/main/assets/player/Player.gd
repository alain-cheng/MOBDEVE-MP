extends CharacterBody2D

@onready var animation = get_node("PlayerSprite")
@onready var buttons = get_node("PlayerCamera/DirectionalButtons")
@onready var collider = get_node("EnvironmentCollider")
@onready var hurtbox = get_node("PlayerHurtbox/HurtboxColliider")
@onready var hurtbox_area = get_node("PlayerHurtbox")
@onready var transitions = $PlayerCamera/Transitions/TransitionsPlayer
@onready var healthCounter = $PlayerCamera/LifeCounter/Lives
@onready var soundDeath =  $DeathSound
@onready var soundFalling = $FallingSound
@onready var bgm = $BGM
@onready var camera = $PlayerCamera
@onready var mutebutton = $PlayerCamera/MuteButton 
@onready var mute = $PlayerCamera/MuteButton/Mute
var _textureMuted = load("res://sprites/buttons/muted.png")
var _textureUnmuted = load("res://sprites/buttons/unmute.png")
var muted = PlayerData.muted
var zoom = false
var backPressed = false
var isDed = false
var falling = false
signal fall_to_next

func _ready():
	$PlayerCamera/Transitions.show() #Activate transitions
	falling = true #Prevent movement
	healthCounter.text = "× " + str(PlayerData.health) #Load health
	#Play Idle anim on load
	animation.play("idle")
	#Play Fade in Transition
	transitions.play("Fade in")
	await get_tree().create_timer(0.4).timeout #Base on anims
	#Enable GUI
	buttons.show()
	# init mute button
	mutebutton.show()
	print(muted)
	if muted:
		mute.texture_normal = _textureMuted
		mute.texture_pressed = _textureUnmuted
	else:
		mute.texture_normal = _textureUnmuted
		mute.texture_pressed = _textureMuted
	$PlayerCamera/LifeCounter.show()
	falling = false
	bgm.play()

func _physics_process(_delta):
	
	if(!isDed && !falling):
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
			
			velocity.x = xDirection
		else:
			velocity.x = move_toward(velocity.x, 0, PlayerData.friction)
			
		#y-axis
		if yDirection && !xDirection && PlayerData.health > 0:
			#y Directional anim
			if yDirection > 0:
				animation.play("down")
			else:
				animation.play("up")
			
			velocity.y = yDirection
		else:
			velocity.y = move_toward(velocity.y, 0, PlayerData.friction)
			
		#Diagonal movement
		if yDirection && xDirection && PlayerData.health > 0:
			#y Directional anim
			if yDirection > 0:
				animation.play("down")
			else:
				animation.play("up")
			
			velocity.x = xDirection
			velocity.y = yDirection
		
		#Normalize Velocity
		velocity = velocity.normalized()*PlayerData.speed
		move_and_slide()
		#END MOVEMENT
	elif(isDed && !falling):
		#Animation for floating ghost
		velocity.x = 0
		velocity.y = -25
		move_and_slide()
	if zoom:
		camera.zoom += Vector2(0.5, 0.5)

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
			#No need to update json, default is no loss and no treasure
			get_tree().quit()

func on_damage_taken(damage = 1): #Default damage is 1
	if !isDed:
		isDed = true
		ui_off()
		animation.play("death")
		bgm.stop()
		soundDeath.play()
		await get_tree().create_timer(3.5).timeout #Base on anims
		PlayerData.health = PlayerData.health - damage
		
		if PlayerData.health <= 0: #Game over
			transitions.play("Fade out")
			await get_tree().create_timer(0.9).timeout #Base on anims
			
			get_tree().change_scene_to_file("res://environment/interactive/gameover_popup/GameOver_Popup.tscn")

		elif PlayerData.health > 0: #-1 Life
			get_tree().reload_current_scene()
		
func ive_fallen(fallPos: Vector2):
	#Animation
	falling = true
	ui_off()
	animation.stop()
	await get_tree().create_timer(0.5).timeout #Base on anims
	animation.play("fall")
	soundFalling.play()
	await get_tree().create_timer(1.0).timeout #Base on anims
	falling = false
	
	 #take damage
	PlayerData.health = PlayerData.health - 3
	isDed = true
	#Check if is health <= 0 and !finalFloor
	if(PlayerData.health > 0 && !PlayerData.lastFloor): #fall to next floor
		camera.position_smoothing_enabled = true
		camera.position_smoothing_speed = 7.0
		global_position = fallPos + Vector2(0, 55)
		zoom = true
		await get_tree().create_timer(1.0).timeout #Base on anims
		
		zoom = false
		fall_to_next.emit()
	elif(PlayerData.health <= 0 || PlayerData.lastFloor): #Die
		bgm.stop()
		soundDeath.play()
		
		camera.position_smoothing_enabled = true
		global_position = fallPos
		
		animation.play("death")
		await get_tree().create_timer(3.5).timeout #Base on anims
		transitions.play("Fade out")
		await get_tree().create_timer(0.9).timeout #Base on anims
		
		get_tree().change_scene_to_file("res://environment/interactive/gameover_popup/GameOver_Popup.tscn")
	
func ui_off():
	#Toggle booleans
	collider.set_deferred("disabled", true)
	hurtbox.set_deferred("disabled", true)
	hurtbox_area.set_deferred("monitorable", false)
	buttons.hide()
	mutebutton.hide()
	$PlayerCamera/LifeCounter.hide()
	
func update_json(t, l):
	var dict = {generateTreasure = t, loss = l}
	var path = "user://signal_data.json"
	if FileAccess.file_exists(path):
		var file = FileAccess.open(path, FileAccess.WRITE)
		file.store_line(JSON.stringify(dict))
		file.close()


func _on_mute_pressed():
	var bus_idx = AudioServer.get_bus_index("Master")
	
	if muted:
		AudioServer.set_bus_mute(bus_idx, !muted)
		mute.texture_pressed = _textureMuted
		mute.texture_normal = _textureUnmuted
	else:
		AudioServer.set_bus_mute(bus_idx, !muted)
		mute.texture_pressed = _textureUnmuted
		mute.texture_normal = _textureMuted
		
	muted = !muted
	PlayerData.muted = muted
