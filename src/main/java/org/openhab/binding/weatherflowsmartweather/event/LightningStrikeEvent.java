package org.openhab.binding.weatherflowsmartweather.event;

import org.eclipse.smarthome.core.events.AbstractEvent;
import org.openhab.binding.weatherflowsmartweather.model.LightningStrikeData;

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
