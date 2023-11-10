extends Node2D

@onready var spikes1 = get_node("Spikes")

# Called when the node enters the scene tree for the first time.
func _ready():
	#Declare timers here at on ready
	#Spikes Timers
	spikes1.timer.wait_time = 2.0
	spikes1.timer.one_shot = false
	spikes1.timer.start()
	#End Spikes Timers
