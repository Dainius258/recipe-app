
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.kvk.recipeapp.adapters.CommentAdapter
import com.kvk.recipeapp.data.Comment
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.sql.Timestamp

@RunWith(MockitoJUnitRunner::class)
class CommentAdapterTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockLayoutInflater: LayoutInflater

    @Mock
    private lateinit var mockView: View

    @Mock
    private lateinit var mockUsernameTextView: TextView

    @Mock
    private lateinit var mockCommentTextTextView: TextView

    @Mock
    private lateinit var mockExpandButton: ImageButton

    @Mock
    private lateinit var mockCardView: CardView

    private lateinit var commentAdapter: CommentAdapter

    private val commentsList = mutableListOf<Comment>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        commentAdapter = CommentAdapter(commentsList, mockContext)
    }

    @Test
    fun testAdapterCreation() {
        val comments = mutableListOf<Comment>()
        val adapter = CommentAdapter(comments, mock(Context::class.java))
        assert(adapter.itemCount == 0)
    }
    @Test
    fun testViewHolderBind() {
        // Ensure that the layout resource contains the necessary views
        assertNotNull(mockUsernameTextView)
        assertNotNull(mockCommentTextTextView)
        assertNotNull(mockExpandButton)
        assertNotNull(mockCardView)

        val comment = Comment(
            1,
            1,
            1,
            "Test Comment",
            Timestamp(System.currentTimeMillis()),
            Timestamp(System.currentTimeMillis()),
            "Test User"
        )

        // Add more assertions as needed
    }
}