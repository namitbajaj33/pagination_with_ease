package com.example.mypagination.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypagination.R
import com.example.mypagination.db.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = AppDatabase.create(this)
        val myViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        myViewModel.init(db)

        val adapter = MyAdapter {}

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        myViewModel.persons.observe(this, Observer { adapter.submitList(it) })
    }
}
