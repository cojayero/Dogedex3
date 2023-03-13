package com.cojayero.dogedex3.api.dto

import android.util.Log
import com.cojayero.dogedex3.auth.User
private val TAG = UserDTOMapper::class.java.simpleName

class UserDTOMapper {
    fun fromUserDTOtoUserDomain(user: UserDTO): User {
        Log.d(TAG,".... convirtiendo $user")
        return (User(
            user.id,
            user.email,
            user.authenticationToken
        ))
    }
}