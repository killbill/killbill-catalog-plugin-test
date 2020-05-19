/*
 * Copyright 2014-2018 The Billing Project, LLC
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
import org.killbill.billing.catalog.CatalogUpdater;
import org.killbill.billing.catalog.StandaloneCatalog;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.CatalogApiException;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.catalog.api.SimplePlanDescriptor;
import org.killbill.billing.catalog.api.TimeUnit;
import org.killbill.billing.catalog.api.user.DefaultSimplePlanDescriptor;
import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.catalog.test.models.StandalonePluginCatalogModel;
import org.killbill.billing.plugin.catalog.test.models.VersionedPluginCatalogModel;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.xmlloader.XMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CatalogTestPluginApi implements CatalogPluginApi {

    private static final Logger logger = LoggerFactory.getLogger(CatalogTestPluginApi.class);

    private static final String DEFAULT_CATALOG_NAME = "WeaponsHire.xml";

    private List<StandaloneCatalog> versions;

    private String catalogName;

    public CatalogTestPluginApi(final Properties properties) throws Exception {
        final StandaloneCatalog inputCatalog = buildDefaultCatalog();
        versions = new ArrayList<StandaloneCatalog>();
        versions.add(inputCatalog);
    }

    private StandaloneCatalog buildDefaultCatalog() throws Exception {
        this.catalogName = DEFAULT_CATALOG_NAME;
        return XMLLoader.getObjectFromString(this.getClass()
                                                 .getClassLoader()
                                                 .getResource(DEFAULT_CATALOG_NAME)
                                                 .toExternalForm(), StandaloneCatalog.class);
    }

    private StandaloneCatalog buildLargeCatalog() throws CatalogApiException {
        this.catalogName = "LargeOrders";

        final CatalogUpdater catalogUpdater = new CatalogUpdater(new DateTime(2011, 10, 8, 0, 0), Currency.USD);
        int MAX_PLANS = 15000;
        for (int i = 1; i <= MAX_PLANS; i++) {
            final SimplePlanDescriptor desc = new DefaultSimplePlanDescriptor("foo-monthly-" + i + "-pl",
                                                                              "Foo",
                                                                              ProductCategory.BASE,
                                                                              Currency.USD,
                                                                              BigDecimal.TEN,
                                                                              BillingPeriod.MONTHLY,
                                                                              0,
                                                                              TimeUnit.UNLIMITED,
                                                                              ImmutableList.<String>of());
            catalogUpdater.addSimplePlanDescriptor(desc);
            if (i % 1000 == 0) {
                System.err.println("++++++++++++  Iteration = " + i);
            }
        }
        logger.info("CatalogTestPluginApi : Initialized CatalogTestPluginApi with static catalog " + DEFAULT_CATALOG_NAME);
        return catalogUpdater.getCatalog();
    }

    @Override
    public DateTime getLatestCatalogVersion(Iterable<PluginProperty> properties, TenantContext context) {
        return null;
    }

    @Override
    public VersionedPluginCatalog getVersionedPluginCatalog(Iterable<PluginProperty> properties,
                                                            TenantContext tenantContext) {

        System.err.println("++++++++++++  FOUND TENANT " + tenantContext.getTenantId() + ", accountId = " + tenantContext
                .getAccountId());
        final VersionedPluginCatalog result = new VersionedPluginCatalogModel(catalogName,
                                                                              toStandalonePluginCatalogs(versions));
        logger.info("CatalogTestPluginApi getVersionedPluginCatalog returns result.. ");
        return result;
    }

    private Iterable<StandalonePluginCatalog> toStandalonePluginCatalogs(final List<StandaloneCatalog> input) {
        return Iterables.transform(input, new Function<StandaloneCatalog, StandalonePluginCatalog>() {
            @Override
            public StandalonePluginCatalog apply(final StandaloneCatalog input) {
                return new StandalonePluginCatalogModel(new DateTime(input.getEffectiveDate()),
                                                        ImmutableList.copyOf(input.getSupportedCurrencies()),
                                                        ImmutableList.<Product>copyOf(input.getProducts()),
                                                        ImmutableList.<Plan>copyOf(input.getPlans()),
                                                        input.getPriceLists().getDefaultPricelist(),
                                                        ImmutableList.<PriceList>copyOf(input.getPriceLists()
                                                                                             .getChildPriceLists()),
                                                        input.getPlanRules(),
                                                        null /* ImmutableList.<Unit>copyOf(input.getCurrentUnits()) */);
            }
        });
    }
}
