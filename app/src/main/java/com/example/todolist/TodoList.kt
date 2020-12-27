package com.example.todolist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import org.jetbrains.anko.startActivity

class TodoList : AppCompatActivity() {
    val realm = Realm.getDefaultInstance() // 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist)
        setSupportActionBar(findViewById(R.id.toolbar))


        // 새 할 일 추가
        val fab = findViewById<FloatingActionButton >(R.id.fab)
        fab.setOnClickListener {
            startActivity<EditActivity>()
        }

        // 전체 할 일 정보를 가져와서 날짜순으로 내림차순 정렬 2
        val realmResult = realm.where<Todo>()
            .findAll()
            .sort("date", Sort.DESCENDING)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close() // 3
    }
}