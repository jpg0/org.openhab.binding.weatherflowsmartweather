package org.openhab.binding.weatherflowsmartweather;

import java.net.InetAddress;

import org.openhab.binding.weatherflowsmartweather.model.SmartWeatherMessage;

public interface SmartWeatherEventListener {

    public void eventReceived(InetAddress source, SmartWeatherMessage data);
}
