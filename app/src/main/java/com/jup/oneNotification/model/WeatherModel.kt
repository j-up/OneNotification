package com.jup.oneNotification.model


data class WeatherResponse(val Current:Current, val daily: Array<Daily>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WeatherResponse

        if (Current != other.Current) return false
        if (!daily.contentEquals(other.daily)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Current.hashCode()
        result = 31 * result + daily.contentHashCode()
        return result
    }
}

data class Current(val dt:Long, val temp:Float, val feels_like:Float, val humidity:Int, val weather:Weather)
data class Weather(val id:String, val main:String, val description:String, val icon:String)

data class Daily(val dt:Long,val temp:Temp,val feels_like:FeelsLike,val humidity:Int, val weather: Array<Weather>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Daily

        if (dt != other.dt) return false
        if (temp != other.temp) return false
        if (feels_like != other.feels_like) return false
        if (humidity != other.humidity) return false
        if (!weather.contentEquals(other.weather)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dt.hashCode()
        result = 31 * result + temp.hashCode()
        result = 31 * result + feels_like.hashCode()
        result = 31 * result + humidity
        result = 31 * result + weather.contentHashCode()
        return result
    }
}

data class Temp(val day:Float, val min:Float, val max:Float)
data class FeelsLike(val day:Float, val night:Float)
