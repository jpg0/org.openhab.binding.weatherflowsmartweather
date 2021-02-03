package org.openhab.binding.weatherflowsmartweather.event;

import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;
import org.openhab.core.events.AbstractEvent;

public class RapidWindEvent extends AbstractEvent {
    public static final String TYPE = RapidWindEvent.class.getSimpleName();

    private final RapidWindData rapidWindData;

    RapidWindEvent(String topic, String payload, RapidWindData rapidWindData) {
        super(topic, payload, rapidWindData.getThingUID());
        this.rapidWindData = rapidWindData;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public RapidWindData getRapidWindData() {
        return rapidWindData;
    }

    @Override
    public String toString() {
        return "Rapid Wind at '" + rapidWindData.toString() + "'.";
    }
}
