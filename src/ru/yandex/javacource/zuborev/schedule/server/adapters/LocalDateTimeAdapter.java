package ru.yandex.javacource.zuborev.schedule.server.adapters;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDatetime) throws IOException {
        if (localDatetime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDatetime.format(timeFormatter));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String nullOrNotNull = jsonReader.nextString();
        if (nullOrNotNull.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(nullOrNotNull, timeFormatter);
    }
}
