package com.kvk.recipeapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.data.Comment

class CommentAdapter(private var comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }


    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.tvCommentOwner)
        private val commentText: TextView = itemView.findViewById(R.id.tvComment)
        private val expandButton: ImageButton = itemView.findViewById(R.id.btnExpand)
        private val expandText: TextView = itemView.findViewById(R.id.tvExpand)
        private val cardView: CardView = itemView.findViewById(R.id.cardComment)
        private var isExpanded = false
        private var originalHeight = 0

        init {
            // This is for checking the original height of the CommentCard
            cardView.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    originalHeight = cardView.height
                    Log.d("HEIGHT", originalHeight.toString())
                    cardView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }

        fun bind(comment: Comment) {
            if(comment.comment_text.length < 44) {
                expandButton.visibility = View.GONE
                expandText.visibility = View.GONE
            }
            Log.d("HEIGHT", originalHeight.toString())
            username.text = comment.username
            commentText.text = comment.comment_text
            expandButton.setOnClickListener{ //44
                expandCollapseCard()
            }
        }

        private fun expandCollapseCard() {
            val layoutParams = cardView.layoutParams
            if (isExpanded) {
                // Collapse the card to its original height
                layoutParams.height = originalHeight
                expandButton.setImageResource(R.drawable.down_arrow)
                expandText.text = "Expand"
                isExpanded = false
            } else {
                // Expand the card to fit the entire comment
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                expandButton.setImageResource(R.drawable.up_arrow)
                expandText.text = "Collapse"
                isExpanded = true
            }
            cardView.layoutParams = layoutParams
        }
    }
}