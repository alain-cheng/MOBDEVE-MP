extends Area2D

@onready var flooranim = get_node("FloorSprite")
@onready var player
signal changeFloor
var isWin = false

# Called when the node enters the scene tree for the first time.
func _ready():
	if(!PlayerData.lastFloor):
		flooranim.play("nextfloor")
	else:
		flooranim.play("treasure")


func _physics_process(_delta):
	if isWin:
		get_tree().change_scene_to_file("res://environment/interactive/win_popup/Win_Popup.tscn")


func _on_body_entered(body):
	if body.name == "Player": #Detects if Player enters the endpoint
		if(PlayerData.lastFloor):
			player.transitions.play("Fade out")
			player.animation.play("treasureGet")
			player.falling = true
			player.ui_off()
			await get_tree().create_timer(1.1).timeout #Base on anims
			isWin = true
			#Quits the game, changes generateTreasure to true
			#var dict = {generateTreasure = true, loss = false}
			#var path = "user://signal_data.json"
			#if FileAccess.file_exists(path):
				#var file = FileAccess.open(path, FileAccess.WRITE)
				#file.store_line(JSON.stringify(dict))
				#file.close()
			#get_tree().quit()
		else:
			player.transitions.play("Fade out")
			player.falling = true
			player.ui_off()
			await get_tree().create_timer(0.9).timeout #Base on anims
			changeFloor.emit()
