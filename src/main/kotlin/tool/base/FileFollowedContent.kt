package tool.base

import com.intellij.openapi.vfs.VirtualFile

/**
 * Changes followingData in contentAdapter when @property followingFile changes
 * which leads to creation of a new Content
 * see: [ContentAdapter]
 */
abstract class FileFollowedContent<T>(private val contentAdapter: ContentAdapter<T>) {

    private var followingFile: VirtualFile? = null

    abstract fun getDataForFile(file: VirtualFile): T?
    open fun onFileFollowed(file: VirtualFile, data: T) {}
    open fun onFileUnfollowed(file: VirtualFile, data: T) {}

    fun getFollowingFile() = followingFile


    fun followFiles(files: List<VirtualFile>): Boolean {
        for (file in files) {
            val data = getDataForFile(file)
            if (data != null)
                return followFileImpl(file, data)
        }

        return followFileImpl(null, null)
    }


    private fun followFileImpl(file: VirtualFile?, data: T?): Boolean {
        if (followingFile == file) return true

        if (file != null && data != null) {
            followingFile?.let { onFileUnfollowed(it, contentAdapter.getFollowingData()!!) }
            followingFile = file
            contentAdapter.followDataImpl(data)
            onFileFollowed(file, data)
            return true
        }

        if (contentAdapter.isDataUnfollowedIfNull()) {
            followingFile?.let { onFileUnfollowed(it, contentAdapter.getFollowingData()!!) }
            followingFile = null
        }
        contentAdapter.followDataImpl(null)
        return false
    }

}