package com.example.network.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat
import java.lang.reflect.Type
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object TimestampUtil{

    private val currentTimeMillis: Long
        get() = DateTime.now().millis

    private val currentDate: LocalDate
        get() = DateTime.now().toLocalDate()

    fun exceedsTimestamp(timestamp: Long, period: Long): Boolean {
        return currentTimeMillis - timestamp > period
    }

    fun exceedsPeriod(timestamp: Long, period: Long, timeUnit: TimeUnit): Boolean {
        return exceedsTimestamp(timestamp, timeUnit.toMillis(period))
    }

    fun isBeforeToday(date: String): Boolean { return DateTime.parse(date).toLocalDate().isBefore(currentDate) }
    fun isEvening(): Boolean { return DateTime.now().hourOfDay().get() >= 18 }
}

class DateTimeDeserializer : JsonDeserializer<DateTime>, JsonSerializer<DateTime> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DateTime? =
        json
            ?.asString
            ?.takeIf { it.isNotEmpty() }
            ?.let { ISODateTimeFormat.dateTimeParser().parseDateTime(it) }

    override fun serialize(src: DateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.let { ISODateTimeFormat.dateTime().print(it) }.orEmpty())
    }
}
