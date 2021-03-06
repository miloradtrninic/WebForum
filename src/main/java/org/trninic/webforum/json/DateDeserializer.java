package org.trninic.webforum.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DateDeserializer  extends StdDeserializer<Date> {
 
    private SimpleDateFormat formatter = 
      new SimpleDateFormat("dd-MM-yyyy");
 
    public DateDeserializer() {
        this(null);
    }
 
    public DateDeserializer(Class<?> vc) {
        super(vc);
    }
 
    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
      throws IOException, JsonProcessingException {
        String date = jsonparser.getText();
        try {
            return formatter.parse(date);
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        } 
    }
}