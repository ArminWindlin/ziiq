package org.riseintime.ziiq.recyclerview.item

import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_question.*
import org.riseintime.ziiq.R
import org.riseintime.ziiq.model.Question

class QuestionItem (val question: Question) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.question_item_text.text = question.text
    }

    override fun getLayout() = R.layout.item_question

}