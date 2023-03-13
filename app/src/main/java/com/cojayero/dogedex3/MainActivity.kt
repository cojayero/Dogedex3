package com.cojayero.dogedex3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.cojayero.dogedex3.api.ApiServiceInterceptor
import com.cojayero.dogedex3.auth.LoginActivity
import com.cojayero.dogedex3.auth.User
import com.cojayero.dogedex3.databinding.ActivityMainBinding
import com.cojayero.dogedex3.doglist.DogListActivity
import com.cojayero.dogedex3.settings.SettingsActivity

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityMainBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "..............Entrando en main")
        val user = User.getLoggedInUser(this)
        if (user == null) {
            openLoginActivity()
            return
        } else {
            ApiServiceInterceptor.setSessionToken(user.authenticationToken)
        }
        binding.settingsFab.setOnClickListener {
            openSettingsActivity()
        }
        binding.dogListFab.setOnClickListener {
            openDogListActivity()
        }
    }

    private fun openDogListActivity() {
        startActivity(Intent(this,DogListActivity::class.java))
    }

    private fun openSettingsActivity() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}