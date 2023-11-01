extends TouchScreenButton


# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass


func _on_pressed():
	#Quits the game, changes generateTreasure to true
	var dict = {generateTreasure = true}
	var path = "user://signal_data.json"
	if FileAccess.file_exists(path):
		var file = FileAccess.open(path, FileAccess.WRITE)
		file.store_line(JSON.stringify(dict))
		file.close()
	get_tree().quit()
