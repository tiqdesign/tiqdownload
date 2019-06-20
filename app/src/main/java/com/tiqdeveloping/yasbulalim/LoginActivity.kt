package com.tiqdeveloping.yasbulalim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btn_anim = AnimationUtils.loadAnimation(this,R.anim.fromtop)
        btn_login.startAnimation(btn_anim)

        val img_fade = AnimationUtils.loadAnimation(this,R.anim.fadeout)
        img_logo.startAnimation(img_fade)

        btn_login.setOnClickListener{
            val intent  = Intent(this,MainActivity::class.java)
             startActivity(intent)
        }
    }
}
