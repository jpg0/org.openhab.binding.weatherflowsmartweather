package org.openhab.binding.weatherflowsmartweather.event;

import java.util.Collection;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.smarthome.core.events.AbstractTypedEventSubscriber;
import org.eclipse.smarthome.core.events.Event;
import org.eclipse.smarthome.core.events.EventFilter;
import org.eclipse.smarthome.core.events.EventSubscriber;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { EventSubscriber.class,
        WeatherFlowEventSubscriber.class }, configurationPid = "binding.weatherflowsmartweather")
public class WeatherFlowEventSubscriberImpl extends AbstractTypedEventSubscriber<@NonNull Event>
        implements WeatherFlowEventSubscriber {

    private final Logger logger = LoggerFactory.getLogger(WeatherFlowEventSubscriberImpl.class);

    EventFilter eventFilter = new EventFilter() {
        @Override
        public boolean apply(Event event) {
         //   logger.warn("Event: " + event);
            return false;
        }
    };

    public WeatherFlowEventSubscriberImpl() {
        super(AbstractTypedEventSubscriber.ALL_EVENT_TYPES);
        logger.warn("Starting!");
    }

    @Override
    public EventFilter getEventFilter() {
        return eventFilter;
    }

    @Override
    protected void receiveTypedEvent(Event rapidWindEvent) {

    }


}
