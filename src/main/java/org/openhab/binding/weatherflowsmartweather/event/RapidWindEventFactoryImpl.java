package org.openhab.binding.weatherflowsmartweather.event;

import static org.openhab.binding.weatherflowsmartweather.util.GsonUtils.gsonDateTime;

import java.util.Set;

import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;
import org.openhab.core.events.AbstractEventFactory;
import org.openhab.core.events.Event;
import org.openhab.core.events.EventFactory;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.*;

@Component(service = { EventFactory.class,
        RapidWindEventFactory.class }, configurationPid = "binding.weatherflowsmartweather")
public class RapidWindEventFactoryImpl extends AbstractEventFactory implements RapidWindEventFactory {

    static final String RAPID_WIND_EVENT_TOPIC = "org.openhab";

    private static final Logger log = LoggerFactory.getLogger(RapidWindEventFactoryImpl.class);

    public RapidWindEventFactoryImpl() {
        super(Set.of(RapidWindEvent.TYPE));
    }

    public static RapidWindEvent createRapidWindEvent(RapidWindData rapid_wind) {
        String topic = RapidWindEventFactoryImpl.buildTopic(RapidWindEventFactoryImpl.RAPID_WIND_EVENT_TOPIC,
                rapid_wind.getThingUID());
        log.debug("Topic: " + topic);
        String payload = null;

        try {
            payload = mySerializePayload(rapid_wind);
        } catch (Throwable e) {
            log.error("Error serializing payload.", e);
        }
        log.debug("Payload: " + payload);
        return new RapidWindEvent(topic, payload, rapid_wind);
    }

    @Override
    protected Event createEventByType(String eventType, String topic, String payload, String source) throws Exception {
        log.debug("Creating event " + eventType + " topic=" + topic + ", payload=" + payload + ", source=" + source);
        if (RapidWindEvent.TYPE.equals(eventType)) {
            log.debug(
                    "Creating event " + eventType + " topic=" + topic + ", payload=" + payload + ", source=" + source);
            return createRapidWindEvent(topic, payload);
        }
        throw new IllegalArgumentException("Unsupported event type " + eventType);
    }

    protected static String buildTopic(String topic, String thingUID) {
        return topic.replace("{thingUID}", thingUID);
    }

    private Event createRapidWindEvent(String topic, String payload) {
        RapidWindData rapidWindData = myDeserializePayload(payload, RapidWindData.class);
        return new RapidWindEvent(topic, payload, rapidWindData);
    }

    protected static <T> T myDeserializePayload(String payload, Class<T> classOfPayload) {
        return gsonDateTime().fromJson(payload, classOfPayload);
    }

    protected static String mySerializePayload(RapidWindData payloadObject) {
        return gsonDateTime().toJson(payloadObject);
    }
}
