package com.tiooooo.todolist.pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tiooooo.todolist.adapter.TodoAdapter
import com.tiooooo.todolist.databinding.ActivityMainBinding
import com.tiooooo.todolist.datasource.createDummyData
import com.tiooooo.todolist.model.Todo

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_EDIT = "extra_edit"
        const val EXTRA_DELETE = "extra_delete"
    }

    private lateinit var binding: ActivityMainBinding
    private val todoAdapter = TodoAdapter(handleAdapterListener())
    private var dummyData: MutableList<Todo> = mutableListOf()

    private val addTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val text = result.data?.getStringExtra(EXTRA_MESSAGE)

                text?.let {
                    if (dummyData.isNotEmpty()) {
                        val id = dummyData.last().id + 1
                        addNewData(Todo(id, text))
                    }

                    if (dummyData.isEmpty()) {
                        val newTodo = Todo(1, text)
                        addNewData(newTodo)
                    }
                }
            }
        }

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val isEdit = result.data?.getBooleanExtra(EXTRA_EDIT, false) ?: false
                val text = result.data?.getStringExtra(EXTRA_MESSAGE) ?: ""
                val id = result.data?.getIntExtra(EXTRA_ID, 0) ?: 0
                val isDelete = result.data?.getBooleanExtra(EXTRA_DELETE, false) ?: false
                val newTodo = Todo(id, text)

                if (isDelete) {
                    dummyData.remove(newTodo)
                    updateAdapter()

                    Toast.makeText(this, "$text berhasil dihapus", Toast.LENGTH_SHORT).show()
                }

                if (isEdit) {
                    dummyData.forEachIndexed { index, todo ->
                        if (todo.id == id) {
                            dummyData[index] = newTodo
                        }
                    }

                    binding.rvTodo.scrollToPosition(id - 1)
                    updateAdapter()

                    Toast.makeText(this, "$text berhasil diedit", Toast.LENGTH_SHORT).show()
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dummyData.addAll(createDummyData())
        updateAdapter()
        val lm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.rvTodo.apply {
            adapter = todoAdapter
            layoutManager = lm
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            addTodoLauncher.launch(intent)
        }

    }

    private fun updateAdapter() {
        binding.tvEmptyMessage.isVisible = dummyData.isEmpty()
        todoAdapter.setData(dummyData)
        todoAdapter.notifyDataSetChanged()
    }

    private fun addNewData(todo: Todo) {
        dummyData.add(todo)
        updateAdapter()
        Toast.makeText(this, "${todo.text} berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }

    private fun handleAdapterListener() = object : TodoAdapterListener {
        override fun onClickDetail(todo: Todo) {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra(EXTRA_ID, todo.id)
            intent.putExtra(EXTRA_MESSAGE, todo.text)

            startActivity(intent)
        }

        override fun onClickEdit(todo: Todo) {
            val intent = Intent(this@MainActivity, AddTodoActivity::class.java)
            intent.putExtra(EXTRA_ID, todo.id)
            intent.putExtra(EXTRA_EDIT, true)
            intent.putExtra(EXTRA_MESSAGE, todo.text)

            editLauncher.launch(intent)
        }

    }
}


interface TodoAdapterListener {
    fun onClickDetail(todo: Todo)
    fun onClickEdit(todo: Todo)
}
