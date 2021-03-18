package com.pjs.itinterviewtrainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(supportFragmentManager.beginTransaction()){
            replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
            commit()
        }
    }
}