package org.openhab.binding.weatherflowsmartweather.event;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.openhab.binding.weatherflowsmartweather.model.EventRapidWindMessage;
import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;

public class PrecipitationStartedEvent extends AbstractEvent {
    public static final String TYPE = PrecipitationStartedEvent.class.getSimpleName();

    private final RapidWindData rapidWindData;

    PrecipitationStartedEvent(String topic, String payload, RapidWindData rapidWindData) {
        super(topic, payload, null);
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
