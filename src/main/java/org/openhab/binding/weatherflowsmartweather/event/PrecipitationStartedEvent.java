package org.openhab.binding.weatherflowsmartweather.event;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.openhab.binding.weatherflowsmartweather.model.PrecipitationStartedData;

public class PrecipitationStartedEvent extends AbstractEvent {
    public static final String TYPE = PrecipitationStartedEvent.class.getSimpleName();

    private final PrecipitationStartedData precipitationStartedData;

    PrecipitationStartedEvent(String topic, String payload, PrecipitationStartedData precipitationStartedData) {
        super(topic, payload, null);
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
