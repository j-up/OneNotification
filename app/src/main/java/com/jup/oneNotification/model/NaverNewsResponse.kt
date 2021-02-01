package com.jup.oneNotification.model

data class NaverNewsResponse(
    var lastBuildDate: String = "",
    var total: Int = 0,
    var start: Int = 0,
    var display: Int = 0,
    var items: List<Items>
)

data class Items(
    var title: String = "",
    var originalLink: String = "",
    var link: String = "",
    var description: String = "",
    var pubDate: String = ""
)