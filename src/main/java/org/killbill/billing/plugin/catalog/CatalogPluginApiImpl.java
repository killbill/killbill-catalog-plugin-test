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

import org.joda.time.DateTime;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.catalog.StandaloneCatalog;
import org.killbill.billing.catalog.api.CatalogApiException;
import org.killbill.billing.catalog.api.VersionedCatalog;
import org.killbill.billing.catalog.io.VersionedCatalogLoader;
import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.catalog.models.plugin.boilerplate.StandalonePluginCatalogImp;
import org.killbill.billing.plugin.catalog.models.plugin.boilerplate.VersionedPluginCatalogImp;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.callcontext.boilerplate.TenantContextImp;
import org.killbill.billing.util.config.definition.CatalogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CatalogPluginApiImpl implements CatalogPluginApi {

    private static final Logger logger = LoggerFactory.getLogger(CatalogPluginApiImpl.class);


    private final OSGIKillbillAPI osgiKillbillAPI;

    private final CatalogConfigurationHandler configHandler;

    public CatalogPluginApiImpl(final CatalogConfigurationHandler configHandler, final OSGIKillbillAPI osgiKillbillAPI) {
        this.configHandler = configHandler;
        this.osgiKillbillAPI = osgiKillbillAPI;
    }


    @Override
    public DateTime getLatestCatalogVersion(final Iterable<PluginProperty> properties, final TenantContext context) {
        return null;
    }


    public VersionedPluginCatalog getVersionedPluginCatalog(final Iterable<PluginProperty> properties, final TenantContext tenantContext) {

        final CatalogConfiguration config = configHandler.getConfigurable(tenantContext.getTenantId());

        final String catalogPath;
        switch (config.getScheme()) {
            case RESOURCE:
                logger.info("Loading catalog from resource {}", config.getURI());
                catalogPath = config.getURI().toString();
                break;
            case FILE_BASED:
                catalogPath = config.isPerAccount() ?
                        String.format("%s/%s", config.getURI().toString(), tenantContext.getAccountId()) : config.getURI().toString();
                break;
            default:
                throw new RuntimeException(String.format("Invalid catalog config scheme %s", config.getScheme()));
        }

        if (config.isPerAccount()) {
            if (tenantContext.getAccountId() == null) {
                // TODO throw
                return null;
            }

            if (config.shouldValidateAccount()) {
                try {
                    checkAccount(tenantContext.getAccountId(), tenantContext.getTenantId());
                } catch (final AccountApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            return readCatalogFromPath(catalogPath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (CatalogApiException e) {
            throw new RuntimeException(e);
        }
    }


    VersionedPluginCatalog readCatalogFromPath(final String path) throws MalformedURLException, CatalogApiException {
        final VersionedCatalogLoader loader = new VersionedCatalogLoader(getCatalogConfig(), null, null);
        final VersionedCatalog tmp = loader.loadDefaultCatalog(path);
        final Iterable<StandalonePluginCatalog> versions = toStandalonePluginCatalogs(tmp.getVersions().stream().map(v -> (StandaloneCatalog) v).collect(Collectors.toList()));

        final VersionedPluginCatalogImp.Builder b = new VersionedPluginCatalogImp.Builder()
                .withStandalonePluginCatalogs(versions);

        return new VersionedPluginCatalogImp(b.build());
    }



    private Iterable<StandalonePluginCatalog> toStandalonePluginCatalogs(final List<StandaloneCatalog> input) {
        return input.stream()
                .map(i -> new StandalonePluginCatalogImp.Builder()
                        .withEffectiveDate(new DateTime(i.getEffectiveDate()))
                        .withUnits(Arrays.asList(i.getUnits()))
                        .withCurrencies(Arrays.asList(i.getSupportedCurrencies()))
                        .withProducts(i.getProducts())
                        .withPlans(i.getPlans())
                        .withPlanRules(i.getPlanRules())
                        .withDefaultPriceList(i.getPriceLists().getDefaultPricelist())
                        .withChildrenPriceList(Arrays.asList(i.getPriceLists().getChildPriceLists()))
                        .build())
                .collect(Collectors.toList());
    }

    private Account checkAccount(final UUID accountId, final UUID tenantId) throws AccountApiException {
        final TenantContextImp context = new TenantContextImp.Builder<>().withAccountId(accountId).withTenantId(tenantId).build();
        final Account account = osgiKillbillAPI.getAccountUserApi().getAccountById(accountId, context);
        return account;
    }


    CatalogConfig getCatalogConfig() {
        return new CatalogConfig() {
            @Override
            public String getCatalogURI() {
                return null;
            }

            @Override
            public Integer getCatalogThreadNb() {
                return 1;
            }
        };
    }


}
