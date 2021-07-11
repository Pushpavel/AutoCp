package tool.base

import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

/**
 * Creates a new content on changes to @property followingData
 * and notifies contentChangeListener
 */
abstract class ContentAdapter<T> {
    enum class NullDataPolicy {
        SHOW_EMPTY_CONTENT,
        SHOW_STALE_CONTENT,
        REMOVE_CONTENT
    }

    protected val contentFactory: ContentFactory = ContentFactory.SERVICE.getInstance()
    private var contentChangeListener: ((Content?) -> Unit)? = null
    private var followingData: T? = null

    fun isDataUnfollowedIfNull() = getNullDataPolicy() != NullDataPolicy.SHOW_STALE_CONTENT

    abstract fun createContentForData(data: T, contentFactory: ContentFactory): Content
    abstract fun getEmptyContent(): Content

    open fun getNullDataPolicy(): NullDataPolicy = NullDataPolicy.SHOW_STALE_CONTENT


    fun setContentListener(listener: (Content?) -> Unit) {
        contentChangeListener = listener
    }

    fun getFollowingData() = followingData

    fun followDataImpl(data: T?) {
        if (data.hashCode() == followingData.hashCode()) return

        if (data != null) {
            followingData = data
            val content = createContentForData(data, contentFactory)
            fireContentChange(content)
            return
        }

        when (getNullDataPolicy()) {
            NullDataPolicy.SHOW_EMPTY_CONTENT -> {
                fireContentChange(getEmptyContent())
            }
            NullDataPolicy.REMOVE_CONTENT -> {
                fireContentChange(null)
            }
            NullDataPolicy.SHOW_STALE_CONTENT -> {
                // does not change content
            }
        }

        // unfollowing  data
        if (isDataUnfollowedIfNull())
            followingData = null
    }

    private fun fireContentChange(content: Content?) {
        contentChangeListener?.let { it(content) }
    }

}