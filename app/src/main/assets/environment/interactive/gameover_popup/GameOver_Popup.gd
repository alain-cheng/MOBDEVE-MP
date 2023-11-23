extends Control
#TODO: Make popup prettier
@onready var animation = $Panel/AnimationPlayer
@onready var player = $Panel/AnimatedSprite2D
@onready var animation2 = $Panel/AnimationPlayer2
@onready var bgm = $BGM
@onready var soundIdle = $IdleSound
@onready var soundConfirm = $ConfirmSound
signal confirm

func _ready():
	animation.play("popup")
	animation2.play("Blinking text")
	player.play("gameover")
	bgm.play()
	await get_tree().create_timer(4).timeout
	soundIdle.play()


func _on_button_close_popup_pressed():
	bgm.stop()
	soundIdle.stop()
	soundConfirm.play()
	await get_tree().create_timer(0.5).timeout
	emit_signal("confirm")


func _on_confirm():
	animation.play("popup_end")

func _on_animation_player_animation_finished(anim):
	if anim == "popup_end":
		#Quits the game, changes generateTreasure to true
		var dict = {generateTreasure = false, loss = true}
		var path = "user://signal_data.json"
		if FileAccess.file_exists(path):
			var file = FileAccess.open(path, FileAccess.WRITE)
			file.store_line(JSON.stringify(dict))
			file.close()
		get_tree().quit()
