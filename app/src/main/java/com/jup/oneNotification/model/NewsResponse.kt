package com.jup.oneNotification.model

data class NewsResponse(val status:String, val totalResults: Int, val articles: Array<Articles>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NewsResponse

        if (status != other.status) return false
        if (totalResults != other.totalResults) return false
        if (!articles.contentEquals(other.articles)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + totalResults
        result = 31 * result + articles.contentHashCode()
        return result
    }
}

data class Articles(val source: Source, val author: String, val title: String, val description:String
                    ,val url: String, val urlToImage:String, val publishedAt:String, val content:String)

data class Source(val id:String, val name:String)