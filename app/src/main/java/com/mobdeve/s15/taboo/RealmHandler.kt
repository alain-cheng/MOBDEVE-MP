package com.mobdeve.s15.taboo

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.Future

class RealmHandler {
    companion object statifier {
        @JvmStatic
        private var app = App.create("tabooaccountsapp-brqkx")

        @JvmStatic
        private lateinit var realm : Realm

        @JvmStatic
        private lateinit var user: User

        @JvmStatic
        fun startInit() : Future<Unit> {
            return GlobalScope.future { initRealm() }
        }

        @JvmStatic
        private suspend fun initRealm(){
            user = app.login(Credentials.anonymous())
            val config = SyncConfiguration.Builder(
                user,
                setOf(RealmUser::class, RealmUserPlayerData::class, RealmUserTreasure::class)
            )
                .initialSubscriptions {sub ->
                    add(sub.query<RealmUser>())
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
            Log.v("initRealm", realm.configuration.toString())
        }

        @JvmStatic
        fun registerAccount(playerData: PlayerData, treasury : List<Treasure>, username: String, email: String, password: String) {
            //Convert Inputs to RealmUser Objects
            var tempTreasure : RealmUserTreasure
            var playerUpload = RealmUserPlayerData()

            //Converting Treasury
            for (treasure in treasury){
                tempTreasure = RealmUserTreasure() //Create new object
                tempTreasure.itemid = treasure.id
                tempTreasure.count = treasure.count
                tempTreasure.imageid = treasure.imageid
                tempTreasure.lore = treasure.lore
                tempTreasure.name = treasure.name
                tempTreasure.itemBonus = treasure.itemBonus
                tempTreasure.rarity = treasure.rarity
                playerUpload.treasury.add(tempTreasure)
            }

            //Converting PlayerData
            playerUpload.bounty = playerData.bounty
            playerUpload.bountyBonus = playerData.bountyBonus
            playerUpload.taboo = playerData.taboo
            playerUpload.tabooBonus = playerData.tabooBonus
            playerUpload.health = playerData.health
            playerUpload.luck = playerData.luck
            playerUpload.setBonus = playerData.setBonus

            //Writing to Online Database
            realm.writeBlocking { copyToRealm(RealmUser().apply {
                this.username = username
                this.email = email
                this.password = password
                this.playerData = playerUpload
            }) }
        }

        @JvmStatic
        fun checkExistingUser(username: String) : Boolean {
            val result = realm.query<RealmUser>(query = "username == $0", username).find()
            return result.size != 0
        }
    }
}