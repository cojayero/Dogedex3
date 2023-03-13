package com.cojayero.dogedex3.auth

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User(
    val id: Long,
    val email: String,
    val authenticationToken: String
) : Parcelable {


    companion object {
        private const val AUTH_PREFS = "auth_prefs"
        private const val ID_KEY = "id"
        private const val EMAIL_KEY = "email"
        private const val AUTH_KEY = "auth_key"
        fun setLoggedInUser(activity: Activity, userId: User) {

            activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE).also {
                it.edit().putLong(ID_KEY, userId.id).putString(EMAIL_KEY, userId.email)
                    .putString(AUTH_KEY, userId.authenticationToken)
                    .apply()
            }
        }

        fun getLoggedInUser(activity: Activity): User? {
            val prefs =
                activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE) ?: return null
            val userId = prefs.getLong(ID_KEY, 0)
            if (userId == 0L) {
                return null
            } else {
                val user = User(
                    userId, prefs.getString(EMAIL_KEY, "") ?: "",
                    prefs.getString(AUTH_KEY, "") ?: ""
                )
                return user
            }

        }

        fun logout(activity: Activity) {
            activity.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)
                .also {
                    it.edit().clear().apply()
                }
        }

    }
}
