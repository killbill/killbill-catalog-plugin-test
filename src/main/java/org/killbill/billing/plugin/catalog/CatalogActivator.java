/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2022 Equinix, Inc
 * Copyright 2014-2022 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.catalog;

import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillEventDispatcher;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.core.config.PluginEnvironmentConfig;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import java.util.Hashtable;

public class CatalogActivator extends KillbillActivatorBase {

    public static final String PLUGIN_NAME = "killbill-catalog-test";

    private CatalogConfigurationHandler configurationHandler;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        logService.log(LogService.LOG_INFO, "Starting " + PLUGIN_NAME);

        final String region = PluginEnvironmentConfig.getRegion(configProperties.getProperties());
        configurationHandler = new CatalogConfigurationHandler(region, PLUGIN_NAME, killbillAPI);

        final org.killbill.billing.catalog.plugin.api.CatalogPluginApi catalogPluginApi = new CatalogPluginApi(configurationHandler, killbillAPI);
        registerCatalogPluginApi(context, catalogPluginApi);

        registerEventHandlers();
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        super.stop(context);
        dispatcher.unregisterAllHandlers();
    }

    private void registerEventHandlers() {
        final PluginConfigurationEventHandler configHandler = new PluginConfigurationEventHandler(configurationHandler);
        dispatcher.registerEventHandlers(configHandler);
    }

    private void registerCatalogPluginApi(final BundleContext context, final org.killbill.billing.catalog.plugin.api.CatalogPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, org.killbill.billing.catalog.plugin.api.CatalogPluginApi.class, api, props);
    }
}
