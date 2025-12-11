extends Node2D

func _ready():
	# Connect buttons
	$VBoxContainer/Button_EnterDungeon.pressed.connect(_on_enter_dungeon)
	$VBoxContainer/Button_Collection.pressed.connect(_on_collection)
	$VBoxContainer/Button_Profile.pressed.connect(_on_profile)
	$VBoxContainer/Button_Options.pressed.connect(_on_options)
	$VBoxContainer/Button_Exit.pressed.connect(_on_exit)


func _on_enter_dungeon():
	get_tree().change_scene_to_file("res://floors/Dungeon_A/1.tscn")


func _on_collection():
	get_tree().change_scene_to_file("res://Scenes/Collection.tscn")


func _on_profile():
	get_tree().change_scene_to_file("res://Scenes/Profile.tscn")


func _on_options():
	get_tree().change_scene_to_file("res://Scenes/Options.tscn")


func _on_exit():
	get_tree().quit()
