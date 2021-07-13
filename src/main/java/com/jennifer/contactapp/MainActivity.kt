package com.jennifer.contactapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.jennifer.contactapp.activities.ContactFullViewActivity
import com.jennifer.contactapp.databinding.ActivityMainBinding
import com.jennifer.contactapp.models.Contact
import com.jennifer.contactapp.models.ContactAdapter
import com.jennifer.contactapp.models.ContactDatabase
import com.jennifer.contactapp.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var database: ContactDatabase
    private lateinit var viewModel:MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database = Room.databaseBuilder(
            applicationContext,
            ContactDatabase::class.java,
            "contact_database"
        ).allowMainThreadQueries().build()


        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getContact(database)


        contactAdapter = ContactAdapter(listOf<Contact>()) {
            val intent = Intent(this@MainActivity, ContactFullViewActivity::class.java)
            intent.run {
                putExtra("id", it.id)
                putExtra("FullName", it.fName)
                putExtra("phoneNumber", it.PhoneNo).toString()
            }
            startActivity(intent)
        }
        binding.contactRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = contactAdapter
        }


        viewModel.contactLiveData.observe(this, { contact ->
            contactAdapter.phone = contact
            contactAdapter.notifyDataSetChanged()
        })


        binding.clickBtn.setOnClickListener {
            val name = binding.NameInput.editText?.text.toString()
            val number = binding.NumberInput.editText?.text.toString()

            if (name != null) {
                if (number != null) {
                    saveContact(name, number)
                }
            }
        }

    }

    private fun saveContact(name: String, number: String) {
        val contact = Contact(id = 0, name, number )
        viewModel.addContact(database, contact)

    }
}