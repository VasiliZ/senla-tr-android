package com.example.lesson14

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson14.databinding.ActivityMainBinding
import com.example.lesson14.model.Element
import java.io.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var listElements = mutableListOf<Element>()
    private val elementAdapter by lazy {
        ElementAdapter(layoutInflater, listElements)
    }

    companion object {
        private const val EXTRA_LIST_ELEMENT = "LIST_ELEMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        savedInstanceState?.let {
            listElements.clear()
            listElements.addAll(
                it
                    .getParcelableArrayList<Element>(EXTRA_LIST_ELEMENT)?.toList() ?: emptyList()
            )
        } ?: kotlin.run {
            val file = File(App.INSTANCE.getPathFile())

            if (file.exists()) {
                val fileInputStream = FileInputStream(file)
                val objReader = ObjectInputStream(fileInputStream)

                try {
                    val wroteData = objReader.readObject() as? Collection<Element>
                    listElements.addAll(wroteData ?: emptyList())
                } catch (e: Exception) {
                    Toast.makeText(this, R.string.write_data_error, Toast.LENGTH_LONG).show()
                } finally {
                    objReader.close()
                }
            }
        }
        binding.elementList.adapter = elementAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.menuAdd -> {
            elementAdapter.addItem(Element())
            true
        }
        R.id.menuSave -> {
            val file = File(App.INSTANCE.getPathFile())
            val fileOutputStream = FileOutputStream(file)
            val objOutputStream = ObjectOutputStream(fileOutputStream)

            try {
                objOutputStream.writeObject(elementAdapter.getListWithData())
            } catch (e: Exception) {
                Toast.makeText(this, R.string.read_data_error, Toast.LENGTH_LONG).show()
            } finally {
                objOutputStream.close()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(
            EXTRA_LIST_ELEMENT,
            ArrayList(elementAdapter.getListWithData())
        )
    }
}