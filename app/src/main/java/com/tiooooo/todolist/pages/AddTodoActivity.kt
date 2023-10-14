package com.tiooooo.todolist.pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.tiooooo.todolist.databinding.ActivityAddTodoBinding
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_DELETE
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_EDIT
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_ID
import com.tiooooo.todolist.pages.MainActivity.Companion.EXTRA_MESSAGE

class AddTodoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTodoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            val isEdit = intent.getBooleanExtra(EXTRA_EDIT, false)
            val id = intent.getIntExtra(EXTRA_ID, 0)
            val message = intent.getStringExtra(EXTRA_MESSAGE).toString()

            if (isEdit) {
                supportActionBar?.title = "Edit"
                edtText.setText(message)
            }

            binding.btnDelete.isVisible = isEdit

            edtText.doAfterTextChanged {
                btnSave.isEnabled = it?.toString()?.isNotEmpty() ?: false
            }

            btnDelete.setOnClickListener {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_MESSAGE, edtText.text.toString())
                    putExtra(EXTRA_DELETE, true)
                })
                finish()
            }

            btnSave.setOnClickListener {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(EXTRA_ID, id)
                    putExtra(EXTRA_MESSAGE, edtText.text.toString())
                    putExtra(EXTRA_EDIT, isEdit)
                })
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
