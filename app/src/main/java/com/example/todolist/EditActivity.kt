package com.example.todolist

import android.os.Bundle
import android.widget.CalendarView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import org.jetbrains.anko.alert
import org.jetbrains.anko.calendarView
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {

    val realm = Realm.getDefaultInstance() //인스턴스 얻기
    //추가코드
    val calendar: Calendar = Calendar.getInstance() //날짜를 다룰 캘린더 객체생성


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val calendarView = findViewById<CalendarView>(R.id.calenderView)
        //업데이트 조건 1
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertTodo()
        } else {
            updateTodo(id)
        }

        //캘린더 뷰의 날짜를 선택했을 때 Calendar 객체에 설정 2
        calendarView.setOnDateChangeListener { _ : CalendarView, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

    }

    // 추가 모드 초기화 3
    private fun insertMode() {
        // 삭제 버튼을 감추기 4
        val deleteFab = findViewById<FloatingActionButton>(R.id.deleteFab)
        deleteFab.hide()

        // 완료 버튼을 클릭하면 추가 5
        val doneFab = findViewById<FloatingActionButton>(R.id.doneFab)
        doneFab.setOnClickListener {
            insertTodo()
        }
    }

    // 수정 모드 초기화 6
    private fun updateMode(id: Long) {
        val calendarView = findViewById<CalendarView>(R.id.calenderView)
        // id에 해당하는 객체를 화면에 표시 7
        val todoEditText = findViewById<EditText>(R.id.editText)
        realm.where<Todo>().equalTo("id", id)
            .findFirst()?.apply {
                todoEditText.setText(title)
                calendarView.date = date
            }

        // 완료 버튼을 클릭하면 수정 8
        val doneFab = findViewById<FloatingActionButton>(R.id.doneFab)
        doneFab.setOnClickListener {
            updateTodo(id)
        }

        // 삭제 버튼을 클릭하면 삭제 9
        val deleteFab = findViewById<FloatingActionButton>(R.id.deleteFab)
        deleteFab.setOnClickListener {
            deleteTodo(id)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }


    //추가 코드
    private fun insertTodo() {
        realm.beginTransaction()
        val todo = realm.createObject<Todo>(nextId())
        val todoEditText = findViewById<EditText>(R.id.editText)
        todo.title = todoEditText.text.toString()
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
        val todoEditText = findViewById<EditText>(R.id.editText)
        realm.where<Todo>().equalTo("id", id)
            .findFirst()?.apply {
                realm.beginTransaction() //트랜잭션 시작
                //값 추가
                title = todoEditText.text.toString()
                date = calendar.timeInMillis
                realm.commitTransaction() //트랜잭션 종료
            }

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


