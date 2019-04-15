package org.riseintime.ziiq.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.riseintime.ziiq.R
import org.riseintime.ziiq.model.User

class RankingAdapter(private val users: List<User>) :
    RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class RankingViewHolder(rankingItem: View) : RecyclerView.ViewHolder(rankingItem) {
        val rank: TextView = rankingItem.findViewById(R.id.ranking_user_item_rank)
        val userName: TextView = rankingItem.findViewById(R.id.ranking_user_item_user)
        val points: TextView = rankingItem.findViewById(R.id.ranking_user_item_point)
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingAdapter.RankingViewHolder {
        // create a new view
        val rankingItem = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ranking_user, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return RankingViewHolder(rankingItem)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.rank.text = (position + 1).toString()
        var username = users[position].name
        if (username.length > 16) username = username.substring(0, 16) + "..."
        holder.userName.text = username
        holder.points.text =
            users[position].points.toString() + " " + holder.itemView.context.getString(R.string.points)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = users.size
}