package org.openhab.binding.weatherflowsmartweather.event;

import org.openhab.binding.weatherflowsmartweather.model.LightningStrikeData;
import org.openhab.core.events.AbstractEvent;

public class LightningStrikeEvent extends AbstractEvent {
    public static final String TYPE = LightningStrikeEvent.class.getSimpleName();

    private final LightningStrikeData lightningStrikeData;

    LightningStrikeEvent(String topic, String payload, LightningStrikeData lightningStrikeData) {
        super(topic, payload, lightningStrikeData.getThingUID());
        this.lightningStrikeData = lightningStrikeData;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public LightningStrikeData getLightningStrikeData() {
        return lightningStrikeData;
    }

    @Override
    public String toString() {
        return "Lightning Strike at '" + lightningStrikeData.toString() + "'.";
    }
}
