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
                    add(query = sub.query<RealmUser>(query = "owner_id == $0", user.id))
                }
                .log(LogLevel.ALL)
                .build()
            realm = Realm.open(config)
            Log.v("initRealm", realm.configuration.toString())
        }

        @JvmStatic
        fun testQuery() {
            realm.writeBlocking { copyToRealm(RealmUser().apply {
                owner_id = user.id
                username = "Last Order"
                email = "MISAKA20001@Level6.edu"
                password = "NetWorkAdminMisakaL@st0rder"
                playerData = null
            }) }
        }
    }
}