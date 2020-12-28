package com.example.todolist

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.realm.OrderedRealmCollection
import io.realm.RealmBaseAdapter

class TodoListAdapter(realmResult: OrderedRealmCollection<Todo>) :
    RealmBaseAdapter<Todo>(realmResult) {

    class TodoViewHolder(view: View) {
        val dateTextView: TextView = view.findViewById(R.id.text1)
        val textTextView: TextView = view.findViewById(R.id.text2)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val vh: TodoViewHolder
        val view: View

        if (convertView == null) { //1
            view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.item_todo, parent, false) //2

            vh = TodoViewHolder(view) //3
            view.tag = vh //4
        } else { // 5
            view = convertView // 6
            vh = view.tag as TodoViewHolder // 7
        }

        adapterData?.let { //8
            val item = it[position] //9
            vh.textTextView.text = item.title // 10
            vh.dateTextView.text = DateFormat.format("yyyy/MM/dd", item.date) // 11
        }

        return view // 12
    }

    override fun getItemId(position: Int): Long {
        adapterData?.let {
            return it[position].id
        }
        return super.getItemId(position)
    }
}


