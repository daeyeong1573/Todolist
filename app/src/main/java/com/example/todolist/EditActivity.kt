package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    private var realm = Realm.getDefaultInstance()

    //추가코드
    private var calendar: Calendar = Calendar.getInstance() //날짜를 다룰 캘린더 객체생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)


    }

    //추가 코드
        private fun insertTodo() {
            realm.beginTransaction()
            val editText = findViewById<EditText>(R.id.editText)
            val todo = realm.createObject<Todo>(nextId())
            todo.title = editText.text.toString()
            todo.date = calendar.timeInMillis
            realm.commitTransaction()

            alert("내용이 추가되었습니다.") {
                yesButton { finish() }
            }.show()
        }

    //다음 id를 반환
    private fun nextId(): Int {
        val maxId = realm.where<Todo>().max("id")
        if (maxId != null) {
            return maxId.toInt() + 1
        }
        return 0
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
