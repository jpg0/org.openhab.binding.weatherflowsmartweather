package org.openhab.binding.weatherflowsmartweather.internal;

import org.openhab.binding.weatherflowsmartweather.SmartWeatherEventListener;

public interface SmartWeatherUDPListenerService {

    public void registerListener(SmartWeatherEventListener listener);

    public void unregisterListener(SmartWeatherEventListener listener);
}
