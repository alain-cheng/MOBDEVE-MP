extends Area2D

@onready var flooranim = get_node("FloorSprite")
signal changeFloor

# Called when the node enters the scene tree for the first time.
func _ready():
	if(!PlayerData.lastFloor):
		flooranim.play("nextfloor")
	else:
		flooranim.play("treasure")

func _on_body_entered(body):
	if body.name == "Player": #Detects if Player enters the endpoint
		if(PlayerData.lastFloor):
			#TODO: Popup informing the player of win
			
			#Quits the game, changes generateTreasure to true
			var dict = {generateTreasure = true, loss = false}
			var path = "user://signal_data.json"
			if FileAccess.file_exists(path):
				var file = FileAccess.open(path, FileAccess.WRITE)
				file.store_line(JSON.stringify(dict))
				file.close()
			get_tree().quit()
		else:
			changeFloor.emit()
