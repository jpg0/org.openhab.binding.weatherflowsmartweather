package org.openhab.binding.weatherflowsmartweather.automation;

import org.eclipse.smarthome.automation.ModuleHandlerCallback;
import org.eclipse.smarthome.automation.Trigger;
import org.eclipse.smarthome.automation.handler.BaseTriggerModuleHandler;
import org.eclipse.smarthome.automation.handler.TriggerHandler;
import org.eclipse.smarthome.automation.handler.TriggerHandlerCallback;
import org.eclipse.smarthome.config.core.Configuration;
import org.openhab.binding.weatherflowsmartweather.model.RapidWindData;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RapidWindTrigger extends BaseTriggerModuleHandler implements TriggerHandler, EventHandler {

    private static final Logger log = LoggerFactory.getLogger(RapidWindTrigger.class);
    /**
     * This constant is used by {@link HandlerFactory} to create a correct handler instance. It must be the same as in
     * JSON definition of the module type.
     */
    public static final String UID = "RapidWindTrigger";
    public static final String EVENT_TOPIC = "smarthome/things/*/rapidwind";

    /**
     * This constant is used to get the value of the 'skyThingUid' property from {@link Trigger}'s {@link Configuration}.
     */
    private static final String SKY_UID = "skyThingUid";


    /**
     * This constant defines the output name of this {@link Trigger} handler.
     */
    private static final String OUTPUT_NAME = "outputValue";

    /**
     * This field will contain the sky thing's uid with which this {@link Trigger} handler is subscribed for {@link Event}s.
     */
    private final String skyThingUid;

    /**
     * A bundle's execution context within the Framework.
     */
    private final BundleContext context;

    /**
     * This field stores the service registration of this {@link Trigger} handler as {@link EventHandler} in the
     * framework.
     */
    @SuppressWarnings("rawtypes")
    private ServiceRegistration registration;

    /**
     * Constructs a {@link ConsoleTrigger} instance.
     *
     * @param module - the {@link Trigger} for which the instance is created.
     * @param context - a bundle's execution context within the Framework.
     */
    public RapidWindTrigger(final Trigger module, final BundleContext context) {
        super(module);

        log.warn("Creating RapidWindTrigger.");
        if (module == null) {
            throw new IllegalArgumentException("'module' can not be null.");
        }
        final Configuration configuration = module.getConfiguration();
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration can't be null.");
        }
        skyThingUid = (String) configuration.get(SKY_UID);
        if (skyThingUid == null) {
            throw new IllegalArgumentException("'skyThingUid' can not be null.");
        }

        log.warn("Created for " + skyThingUid + ".");

        this.context = context;
    }

    /**
     * This method is called from {@link EventAdmin} service.
     * It gets the value of 'value' event property, pass it to the output of this {@link Trigger} handler and notifies
     * the RuleEngine that this handler is fired.
     *
     * @param event - {@link Event} that is passed from {@link EventAdmin} service.
     */
    @Override
    public void handleEvent(final Event event) {
        log.warn("Handle event.");
        if(!skyThingUid.equals(event.getProperty("source"))) {
            log.warn("Got rapid wind event, but not for us.");
            return;
        }

        if(!event.containsProperty("payload")) {
            log.warn("Got event without payload.");
            return;
        }

        Object data = event.getProperty("payload");

        if(data instanceof RapidWindData) {
            log.warn("Triggering rule!");
            final RapidWindData outputValue = (RapidWindData)data;
            final Map<String, Object> outputProps = new HashMap<String, Object>();
            outputProps.put(OUTPUT_NAME, outputValue);
            ((TriggerHandlerCallback) callback).triggered(module, outputProps);
        } else {
            log.warn("Got event but data not of type RapidWindData");
        }
    }

    /**
     * This method is used to set a callback object to the RuleEngine
     *
     * @param callback a callback object to the RuleEngine.
     */
    @Override
    public void setCallback(final ModuleHandlerCallback callback) {
        super.setCallback(callback);
        log.warn("setCallback(" + callback + ")");
        final Dictionary<String, Object> registrationProperties = new Hashtable<String, Object>();
        registrationProperties.put(EventConstants.EVENT_TOPIC, EVENT_TOPIC);
        registration = context.registerService(EventHandler.class, this, registrationProperties);
    }

    /**
     * This method is used to unregister this handler.
     */
    @Override
    public void dispose() {
        registration.unregister();
        registration = null;
        super.dispose();
    }}
