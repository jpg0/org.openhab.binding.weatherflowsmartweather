package org.openhab.binding.weatherflowsmartweather.event;

import com.google.common.collect.Sets;
import com.google.gson.*;
import org.eclipse.smarthome.core.events.AbstractEventFactory;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.library.types.QuantityType;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.openhab.binding.weatherflowsmartweather.model.PrecipitationStartedData;
import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class PrecipitationStartedEventFactoryImpl extends AbstractEventFactory implements RapidWindEventFactory {

    static final String PRECIPITATION_STARTED_EVENT_TOPIC = "smarthome/things/{thingUID}/precipitation_started";

    private static final Logger log = LoggerFactory.getLogger(PrecipitationStartedEventFactoryImpl.class);
    public PrecipitationStartedEventFactoryImpl() {
        super(Sets.newHashSet(RapidWindEvent.TYPE));
    }

    public static PrecipitationStartedEvent createPrecipitionEvent(PrecipitationStartedData precipitation_started) {
        String topic = PrecipitationStartedEventFactoryImpl.buildTopic(PrecipitationStartedEventFactoryImpl.PRECIPITATION_STARTED_EVENT_TOPIC, precipitation_started.getThingUID());
        log.debug("Topic: " + topic);
        String payload = null;

        try {
            payload = mySerializePayload(precipitation_started);
        } catch (Throwable e) {
            log.error("Error serializing payload.", e);
        }
        log.debug("Payload: " + payload);
        return new PrecipitationStartedEvent(topic, payload, precipitation_started);
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
       if (PrecipitationStartedEvent.TYPE.equals(eventType)) {
           log.debug("creating event " + eventType + " topic=" + topic + ", payload=" + payload + ", source=" + source);
           return createPrecipitionEvent(topic, payload);
        }
        return null;
    }

    protected static String buildTopic(String topic, String thingUID) {
        return topic.replace("{thingUID}", thingUID);
    }

    private Event createPrecipitionEvent(String topic, String payload) {
        PrecipitationStartedData rapidWindData = myDeserializePayload(payload, PrecipitationStartedData.class);
        return new PrecipitationStartedEvent(topic, payload, rapidWindData);
    }

    protected static <T> T myDeserializePayload(String payload, Class<T> classOfPayload) {
        return gsonDateTime().fromJson(payload, classOfPayload);
    }

    protected static String mySerializePayload(PrecipitationStartedData payloadObject) {
        return gsonDateTime().toJson(payloadObject);
    }

    public static Gson gsonDateTime() {
        Gson gson = //new Gson();
        new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
                    @Override
                    public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
                        log.debug("Serializing " + json);
                        JsonPrimitive p = new JsonPrimitive(ISODateTimeFormat.dateTime().print(json));
                        log.debug("Serialized to " + p);
                        return p;
                    }
                })
                .registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
                    @Override
                    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(json.getAsString());
                        return dt;
                    }
                })
                .registerTypeAdapter(QuantityType.class, new JsonSerializer<QuantityType>() {
                    @Override
                    public JsonElement serialize(QuantityType quantityType, Type typeOfSrc, JsonSerializationContext context) {
                        log.debug("Serializing " + quantityType);
                        JsonPrimitive p = new JsonPrimitive(quantityType.toFullString());
                        log.debug("Serialized to " + p);
                        return p;
                    }
                })
                .registerTypeAdapter(QuantityType.class, new JsonDeserializer<QuantityType>() {
                    @Override
                    public QuantityType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        log.debug("Deserializing " + json.getAsString());
                        QuantityType qt = new QuantityType(json.getAsString());
                        log.debug("Deserialized to " + qt);

                        return qt;
                    }
                })
                .create();

return gson;
    }
}
