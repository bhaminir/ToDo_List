package com.example.todolist

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var listOfTask = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    var editActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // If the user comes back to this activity from EditActivity
        // with no error or cancellation
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            // Get the data passed from EditActivity
            if (data != null) {
                val editedString = data.extras!!.getString("newString")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTask.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        loadItems()

        val recyclerView = findViewById<RecyclerView>(R.id.recycleTask)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTask, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTask)

        findViewById<Button>(R.id.button).setOnClickListener {
            val userInputText = inputTextField.text.toString();
            listOfTask.add(userInputText)
            adapter.notifyItemInserted(listOfTask.size - 1)
            inputTextField.setText("")
            saveItems()
        }
    }

    fun getDataFile(): File {
        return File(filesDir, "data.txt")

    }

    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTask)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    fun loadItems() {
        try{
            listOfTask = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
    } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }


}