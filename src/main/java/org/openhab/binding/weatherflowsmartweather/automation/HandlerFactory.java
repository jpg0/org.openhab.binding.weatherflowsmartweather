/**
 * Copyright (c) 2014,2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.weatherflowsmartweather.automation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.automation.Module;
import org.eclipse.smarthome.automation.Trigger;
import org.eclipse.smarthome.automation.handler.BaseModuleHandlerFactory;
import org.eclipse.smarthome.automation.handler.ModuleHandler;
import org.eclipse.smarthome.automation.handler.ModuleHandlerFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is a factory for creating {@link RapidWindTrigger}
 * objects.
 *
 * @author William Welliver - Initial contribution
 */
public class HandlerFactory extends BaseModuleHandlerFactory implements ModuleHandlerFactory {

    /**
     * This field contains the name of this factory
     */
    public static final String MODULE_HANDLER_FACTORY_NAME = "[SmartWeatherAutomationFactory]";

    /**
     * This field contains the types that are supported by this factory.
     */
    private static final Collection<String> TYPES;

    /**
     * For error logging if there is a query for a type that is not supported.
     */
    private static final Logger LOGGER;

    /**
     * This blocks fills the Collection ,which contains the types supported by this factory, with supported types and
     * creates a Logger instance for logging errors occurred during the handler creation.
     */
    static {
        final List<String> temp = new ArrayList<String>();
        temp.add(RapidWindTrigger.UID);
        temp.add(LightningStrikeTrigger.UID);
        TYPES = Collections.unmodifiableCollection(temp);

        LOGGER = LoggerFactory.getLogger(HandlerFactory.class);
        LOGGER.warn("Initialize!");
    }

    private BundleContext bundleContext;

    @Override
    public ModuleHandler getHandler(Module module, String ruleUID) {
        String id = ruleUID + module.getId();
        LOGGER.warn("getHandler");
        return super.getHandler(module, ruleUID);
    }

    /**
     * This method must deliver the correct handler if this factory can create it or log an error otherwise.
     * It recognises the correct type by {@link Module}'s UID.
     */
    @Override
    protected ModuleHandler internalCreate(Module module, String ruleUID) {
        LOGGER.warn("internalCreate " + module.getTypeUID() + " " + ruleUID);
        if (RapidWindTrigger.UID.equals(module.getTypeUID())) {
            return new RapidWindTrigger((Trigger) module, bundleContext);
        } else if (LightningStrikeTrigger.UID.equals(module.getTypeUID())) {
            return new LightningStrikeTrigger((Trigger) module, bundleContext);
        } else if (PrecipitationStartedTrigger.UID.equals(module.getTypeUID())) {
            return new PrecipitationStartedTrigger((Trigger) module, bundleContext);
        }
        else {
            LOGGER.error(MODULE_HANDLER_FACTORY_NAME + " Not supported moduleHandler: {}", module.getTypeUID());
        }

        return null;
    }

    /**
     * Returns a {@link Collection} that contains the UIDs of the module types for which this factory can create
     * handlers.
     */
    @Override
    public Collection<String> getTypes() {
        return TYPES;
    }

    /**
     * This method is called when all of the services required by this factory are available.
     *
     * @param bundleContext - the {@link ComponentContext} of the HandlerFactory component.
     */
    public void activate(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
        LOGGER.warn("activate()");
    }

    /**
     * This method is called when a service that is required from this factory becomes unavailable.
     */
    @Override
    public void deactivate() {
        super.deactivate();
    }
}
