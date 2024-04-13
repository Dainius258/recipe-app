package com.kvk.recipeapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.data.Comment
import com.kvk.recipeapp.data.UpdatedComment
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CommentAdapter(private var comments: MutableList<Comment>, private val context: Context) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
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
        private val editButton: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val cardView: CardView = itemView.findViewById(R.id.cardComment)
        private var isExpanded = false
        private var originalHeight = 0

        private val tokenManager = TokenManager(context)
        val token = tokenManager.getToken()

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
            } else {
                expandButton.visibility = View.VISIBLE
                expandText.visibility = View.VISIBLE
            }
            val userId = tokenManager.getUserId()?.toInt()
            if(token != null && tokenManager.isTokenValid(token) && comment.user_id == userId) {
                editButton.visibility = View.VISIBLE
                editButton.setOnClickListener{
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_update_comment, null)
                    builder.setView(dialogLayout)
                    val dialog: AlertDialog = builder.create()
                    dialogLayout.findViewById<AppCompatEditText>(R.id.etmlComment).setText(comment.comment_text)
                    dialogLayout.findViewById<Button>(R.id.btnDeleteComment).setOnClickListener {
                        dialog.dismiss()
                        showCommentDeleteDialog(comment.id)
                    }
                    dialogLayout.findViewById<Button>(R.id.btnCancelComment).setOnClickListener {
                        dialog.dismiss()
                    }
                    dialogLayout.findViewById<Button>(R.id.btnSubmitComment).setOnClickListener {
                        val commentText = dialogLayout.findViewById<AppCompatEditText>(R.id.etmlComment).text.toString()
                        val updatedComment = UpdatedComment(commentText)
                        updateComment(comment.id, updatedComment)
                        dialog.dismiss()
                        showCommentUpdatedDialog()
                    }
                    dialog.show()
                }
            } else {
                editButton.visibility = View.GONE
            }
            Log.d("HEIGHT", originalHeight.toString())
            username.text = comment.username
            commentText.text = comment.comment_text
            expandButton.setOnClickListener{ //44
                expandCollapseCard()
            }
        }
        private fun showCommentUpdatedDialog() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Comment Updated")
            builder.setMessage("Your comment has been successfully updated.")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        private fun showCommentDeletedDialog() {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Comment Deleted")
            builder.setMessage("Your comment has been successfully deleted.")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        private fun showCommentDeleteDialog(commentId: Int) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete comment")
            builder.setMessage("Are you sure you want to delete the comment?")
            builder.setPositiveButton("OK") { dialog, _ ->
                GlobalScope.launch(Dispatchers.IO)  {
                    val response = try {
                        RetroFitInstance.api.deleteCommentById(commentId)
                    } catch (e: IOException) {
                        Log.e("Network", "IOException: ${e.message}")
                        return@launch
                    } catch (e: HttpException) {
                        Log.e("Network", "HttpException: ${e.message}")
                        return@launch
                    }
                    if(response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            Log.d("Network", response.body()!!.message)
                            showCommentDeletedDialog()
                            updateCommentsViewOnDelete(commentId)
                        }
                    } else {
                        Log.e("Network", "Response unsuccessful")
                    }
                }
                dialog.dismiss()
            }
            builder.setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
        private fun updateCommentsViewOnDelete(commentId: Int) {
            val updatedIndex = comments.indexOfFirst { it.id == commentId }
            if (updatedIndex != -1) {
                comments.removeAt(updatedIndex)
                notifyDataSetChanged()
            } else {
                Log.e("CommentAdapter", "Comment with ID $commentId not found in the list")
            }
            // Notify adapter of data change
        }
        private fun updateCommentsViewOnUpdate(commentId: Int, updatedComment: String) {
            val updatedIndex = comments.indexOfFirst { it.id == commentId }
            if (updatedIndex != -1) {
                comments[updatedIndex].comment_text = updatedComment
                notifyDataSetChanged()
            } else {
                Log.e("CommentAdapter", "Comment with ID $commentId not found in the list")
            }
            // Notify adapter of data change
        }
        private fun updateComment(commentId: Int, commentText: UpdatedComment) {
            GlobalScope.launch(Dispatchers.IO)  {
                val response = try {
                    RetroFitInstance.api.updateComment(commentId, commentText)
                } catch (e: IOException) {
                    Log.e("Network", "IOException: ${e.message}")
                    return@launch
                } catch (e: HttpException) {
                    Log.e("Network", "HttpException: ${e.message}")
                    return@launch
                }
                if(response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        Log.d("Network", response.body()!!.message)
                        updateCommentsViewOnUpdate(commentId, commentText.comment_text)
                    }
                } else {
                    Log.e("Network", "Response unsuccessful")
                }
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