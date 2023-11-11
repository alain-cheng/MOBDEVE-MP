extends CharacterBody2D

@onready var animation = get_node("AnimatedSprite2D")

# Called when the node enters the scene tree for the first time.
func _ready():
	pass


# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	animation.play("idle")


func _on_death_zone_body_entered(body):
	print("Player in Kapre Death Zone")


func _on_death_zone_body_exited(body):
	print("Player exited Kapre Death Zone")
