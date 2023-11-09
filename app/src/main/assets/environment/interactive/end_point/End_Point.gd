extends Area2D


# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass


func _on_body_entered(body):
	if body.name == "Player": #Detects if Player enters the endpoint
		if(PlayerData.lastFloor):
			#TODO: Popup informing the player of win
			
			#Quits the game, changes generateTreasure to true
			var dict = {generateTreasure = true}
			var path = "user://signal_data.json"
			if FileAccess.file_exists(path):
				var file = FileAccess.open(path, FileAccess.WRITE)
				file.store_line(JSON.stringify(dict))
				file.close()
			get_tree().quit()
		else:
			pass #TODO: Add logic for switching floors. Maybe a signal?
