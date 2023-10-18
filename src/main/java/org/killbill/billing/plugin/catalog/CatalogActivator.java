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

import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.core.config.PluginEnvironmentConfig;
import org.killbill.billing.plugin.core.resources.jooby.PluginApp;
import org.killbill.billing.plugin.core.resources.jooby.PluginAppBuilder;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

public class CatalogActivator extends KillbillActivatorBase {

    private final Logger logger = LoggerFactory.getLogger(CatalogActivator.class);

    public static final String PLUGIN_NAME = "killbill-catalog-test";

    private CatalogConfigurationHandler configurationHandler;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        logger.info("Starting {}", PLUGIN_NAME);

        final String region = PluginEnvironmentConfig.getRegion(configProperties.getProperties());
        configurationHandler = new CatalogConfigurationHandler(region, PLUGIN_NAME, killbillAPI);
        final CatalogConfiguration defaultConfiguration = createCatalogConfig("WeaponsHire.xml");
        configurationHandler.setDefaultConfigurable(defaultConfiguration);

        final CatalogPluginApi catalogPluginApi = new CatalogPluginApiImpl(configurationHandler, killbillAPI);
        registerCatalogPluginApi(context, catalogPluginApi);

        // Expose a healthcheck (optional), so other plugins can check on the plugin status
        final Healthcheck healthcheck = new CatalogTestHealthcheck();
        registerHealthcheck(context, healthcheck);

        // Register a servlet (optional)
        final PluginApp pluginApp = new PluginAppBuilder(PLUGIN_NAME,
                                                         killbillAPI,
                                                         dataSource,
                                                         super.clock,
                                                         configProperties).withRouteClass(CatalogTestHealthcheckServlet.class)
                                                                          .withService(healthcheck)
                                                                          .build();
        final HttpServlet httpServlet = PluginApp.createServlet(pluginApp);
        registerServlet(context, httpServlet);

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

    private void registerCatalogPluginApi(final BundleContext context, final CatalogPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, CatalogPluginApi.class, api, props);
    }

    private void registerServlet(final BundleContext context, final Servlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerHealthcheck(final BundleContext context, final Healthcheck healthcheck) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Healthcheck.class, healthcheck, props);
    }

    private static CatalogConfiguration createCatalogConfig(final String uri) {
        final String raw = String.format("!!org.killbill.billing.plugin.catalog.CatalogYAMLConfiguration\n" +
                "  uri: %s\n" +
                "  validateAccount: false\n" +
                "  accountCatalog: true", uri);
        return CatalogConfigurationHandler.fromYAML(raw);
    }
}
