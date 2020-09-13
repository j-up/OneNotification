package com.jup.oneNotification.model

import com.jup.oneNotification.core.network.NewsApi
import com.jup.oneNotification.core.network.OpenWeatherApi

data class Responses(val openWeatherApi: OpenWeatherApi, val newsApi: NewsApi)