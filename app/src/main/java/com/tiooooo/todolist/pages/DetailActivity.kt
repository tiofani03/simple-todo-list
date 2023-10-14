package com.tiooooo.todolist.pages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiooooo.todolist.databinding.ActivityDetailBinding
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_ID
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_MESSAGE

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val id = intent.getIntExtra(EXTRA_ID, 0)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        binding.apply {
            tvMessage.text = message
            toolbar.subtitle = "id: $id"
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
