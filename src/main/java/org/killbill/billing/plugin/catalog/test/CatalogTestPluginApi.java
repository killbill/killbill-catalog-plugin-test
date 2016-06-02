/*
 * Copyright 2014-2015 The Billing Project, LLC
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

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.killbill.billing.callcontext.InternalTenantContext;
import org.killbill.billing.catalog.StandaloneCatalog;
import org.killbill.billing.catalog.StandaloneCatalogWithPriceOverride;
import org.killbill.billing.catalog.VersionedCatalog;
import org.killbill.billing.catalog.api.CatalogApiException;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.catalog.test.models.StandalonePluginCatalogModel;
import org.killbill.billing.plugin.catalog.test.models.VersionedPluginCatalogModel;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.clock.DefaultClock;
import org.killbill.xmlloader.XMLLoader;
import org.osgi.service.log.LogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CatalogTestPluginApi implements CatalogPluginApi {

    private static final String CATALOG_NAME = "WeaponsHire.xml";
    private final OSGIKillbillLogService logService;

    private List<StandaloneCatalogWithPriceOverride> versions;

    public CatalogTestPluginApi(final Properties properties, final OSGIKillbillLogService logService) throws Exception {
        this.logService = logService;
        final StandaloneCatalog inputCatalog = XMLLoader.getObjectFromString(this.getClass().getClassLoader().getResource(CATALOG_NAME).toExternalForm(), StandaloneCatalog.class);
        versions = new ArrayList<StandaloneCatalogWithPriceOverride>();
        final StandaloneCatalogWithPriceOverride standaloneCatalogWithPriceOverride = new StandaloneCatalogWithPriceOverride(inputCatalog, null, null, null);
        versions.add(standaloneCatalogWithPriceOverride);
        logService.log(LogService.LOG_INFO, "CatalogTestPluginApi : Initialized CatalogTestPluginApi with static catalog " + CATALOG_NAME);
    }

    @Override
    public VersionedPluginCatalog getVersionedPluginCatalog(Iterable<PluginProperty> properties, TenantContext tenantContext) {

        // Hack but we don't care since we don't use PriceOverride...
        final InternalTenantContext internalTenantContext = new InternalTenantContext(null, null);
        final VersionedCatalog versionedCatalog = new VersionedCatalog(new DefaultClock(), CATALOG_NAME, versions.get(0).getRecurringBillingMode(), versions, internalTenantContext);

        final VersionedPluginCatalog result = new VersionedPluginCatalogModel(versionedCatalog.getCatalogName(), versionedCatalog.getRecurringBillingMode(), toStandalonePluginCatalogs(versionedCatalog.getVersions()));
        logService.log(LogService.LOG_INFO, "CatalogTestPluginApi getVersionedPluginCatalog returns result.. ");
        return result;
    }

    private Iterable<StandalonePluginCatalog> toStandalonePluginCatalogs(final List<StandaloneCatalogWithPriceOverride> input) {
        return Iterables.transform(input, new Function<StandaloneCatalogWithPriceOverride, StandalonePluginCatalog>() {
            @Override
            public StandalonePluginCatalog apply(final StandaloneCatalogWithPriceOverride input) {
                try {

                    return new StandalonePluginCatalogModel(new DateTime(input.getEffectiveDate()),
                            ImmutableList.copyOf(input.getCurrentSupportedCurrencies()),
                            ImmutableList.<Product>copyOf(input.getCurrentProducts()),
                            ImmutableList.<Plan>copyOf(input.getCurrentPlans()),
                            input.getStandaloneCatalog().getPriceLists().getDefaultPricelist(),
                            ImmutableList.<PriceList>copyOf(input.getStandaloneCatalog().getPriceLists().getChildPriceLists()),
                            input.getStandaloneCatalog().getPlanRules(),
                            null /*ImmutableList.<Unit>copyOf(input.getStandaloneCatalog().getCurrentUnits()) */);
                } catch (CatalogApiException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }
}
