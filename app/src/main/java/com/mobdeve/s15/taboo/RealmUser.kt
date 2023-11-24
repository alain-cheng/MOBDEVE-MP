package com.mobdeve.s15.taboo

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class RealmUser : RealmObject {
    @PrimaryKey
    var _id : ObjectId = ObjectId()
    @Index
    var username: String = ""
    @Index
    var email: String = ""
    @Index
    var password: String = ""
    var playerData: RealmUserPlayerData? = null
}

class RealmUserPlayerData : EmbeddedRealmObject {
    var health : Int = 3
    var bounty : Int = 0
    var taboo : Int = 0
    var tabooBonus : Int = 0
    var luck : Int = 0
    var bountyBonus : Int = 0
    var setBonus : String = TreasureList.EMPTY_SET_BONUS
    var treasury : RealmList<RealmUserTreasure> = realmListOf()
}

class RealmUserTreasure : EmbeddedRealmObject {
    var itemid : String = ""
    var name : String = ""
    var imageid : Int = R.drawable.item_kaprecigar
    var itemBonus : String = ""
    var lore : String = ""
    var rarity : String = "LOST"
    var count : Int = 1
}