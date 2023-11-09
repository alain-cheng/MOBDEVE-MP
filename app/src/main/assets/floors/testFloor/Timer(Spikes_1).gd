extends Timer

# Called when the node enters the scene tree for the first time.
func _ready():
	wait_time = 2.0
	one_shot = false
	start()


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass
