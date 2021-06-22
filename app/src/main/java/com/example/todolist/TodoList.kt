package com.example.todolist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_todolist.*
import org.jetbrains.anko.listView
import org.jetbrains.anko.startActivity

class TodoList : AppCompatActivity() {
    private val realm = Realm.getDefaultInstance() // 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist)
        setSupportActionBar(findViewById(R.id.toolbar))


        // 새 할 일 추가

        fab.setOnClickListener {
            startActivity<EditActivity>()
        }

        // 전체 할 일 정보를 가져와서 날짜순으로 내림차순 정렬 2
        val realmResult = realm.where<Todo>()
            .findAll()
            .sort("date", Sort.DESCENDING)
        val adapter = TodoListAdapter(realmResult) // 1
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter // 2

        //데이터가 변경되면 어댑터에 적용 3
        realmResult.addChangeListener { _->adapter.notifyDataSetChanged() }

        listView.setOnItemClickListener { _,_,_, id->
            // 할 일 수정 4
            startActivity<EditActivity>("id" to id)
        }

        // 새 할 일 추가
        fab.setOnClickListener{
            startActivity<EditActivity>()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        realm.close() // 3
    }
}