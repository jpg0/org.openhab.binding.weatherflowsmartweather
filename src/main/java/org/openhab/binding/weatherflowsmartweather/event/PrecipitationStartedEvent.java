package org.openhab.binding.weatherflowsmartweather.event;

import org.openhab.binding.weatherflowsmartweather.model.PrecipitationStartedData;
import org.openhab.core.events.AbstractEvent;

public class PrecipitationStartedEvent extends AbstractEvent {
    public static final String TYPE = PrecipitationStartedEvent.class.getSimpleName();

    private final PrecipitationStartedData precipitationStartedData;

    PrecipitationStartedEvent(String topic, String payload, PrecipitationStartedData precipitationStartedData) {
        super(topic, payload, precipitationStartedData.getThingUID());
        this.precipitationStartedData = precipitationStartedData;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public PrecipitationStartedData getPrecipitationStartedData() {
        return precipitationStartedData;
    }

    @Override
    public String toString() {
        return "Precipitation Started at '" + precipitationStartedData.toString() + "'.";
    }
}
