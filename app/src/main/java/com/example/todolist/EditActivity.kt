package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.jetbrains.anko.alert
import org.jetbrains.anko.calendarView
import org.jetbrains.anko.editText
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    private var realm = Realm.getDefaultInstance()
    //추가코드
    private var calendar: Calendar = Calendar.getInstance() //날짜를 다룰 캘린더 객체생성

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //업데이트 조건 1
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertTodo()
        } else {
            updateTodo(id)
        }

        //캘린더 뷰의 날짜를 선택했을 때 Calendar 객체에 설정 2
        calendarView().setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        realm.close()
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


    private fun updateTodo(id: Long) {
        realm.beginTransaction()  //트랜잭션 시작
        val editText = findViewById<EditText>(R.id.editText)
        val updateItem = realm.where<Todo>().equalTo("id", id).findFirst()!! // 2
        //값 수정
        updateItem.title = editText.text.toString()
        updateItem.date = calendar.timeInMillis

        realm.commitTransaction() //트랜잭션 종료

        //다이얼로그 표시
        alert("내용이 변경되었습니다.") {
            yesButton { finish() }
        }.show()
    }

    // 할 일 삭제 메서드 작성 : 메서드로 전달받은 id 로 삭제할 객체를 검색하고 팢았다면 deleteFromRealm() 메서드로 삭제합니다.
    private fun deleteTodo(id: Long) {
        realm.beginTransaction() //트랜잭션 시작
        val deleteItem = realm.where<Todo>().equalTo("id", id).findFirst()!!
        // 삭제할 객체
        deleteItem.deleteFromRealm()    // 삭제
        realm.commitTransaction() //트랜잭션 종료
        alert("내용이 삭제되었습니다.") {
            yesButton { finish() }
        }.show()
    }

}
