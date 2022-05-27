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

package org.killbill.billing.plugin.catalog.test;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.plugin.core.config.YAMLPluginTenantConfigurationHandler;

import static org.killbill.billing.plugin.catalog.test.CatalogTestActivator.PLUGIN_NAME;

public class CatalogConfigurationHandler extends YAMLPluginTenantConfigurationHandler<CatalogYAMLConfiguration, CatalogConfiguration> {

    private final String region;


    public CatalogConfigurationHandler(final String region, final String pluginName, final OSGIKillbillAPI osgiKillbillAPI) {
        super(pluginName, osgiKillbillAPI);
        this.region = region;
    }

    public CatalogConfigurationHandler(final String region, final String pluginName, final OSGIKillbillAPI osgiKillbillAPI, final String configurationKey) {
        super(pluginName, osgiKillbillAPI, configurationKey);
        this.region = region;
    }

    protected CatalogConfiguration createConfigurable(final CatalogYAMLConfiguration configObject) {
        return new CatalogConfiguration(configObject);
    }

    static CatalogConfiguration fromYAML(final String raw) {
        final CatalogConfigurationHandler tmp = new CatalogConfigurationHandler("", PLUGIN_NAME, null);
        final CatalogYAMLConfiguration yamlConfig = tmp.parseRawConfiguration(raw);
        return new CatalogConfiguration(yamlConfig);
    }
}
