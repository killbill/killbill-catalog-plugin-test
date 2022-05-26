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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.io.Resources;
import org.joda.time.DateTime;
import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.catalog.test.json.VersionedPluginCatalogJsonDeserializer;
import org.killbill.billing.util.callcontext.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CatalogTestPluginApi implements CatalogPluginApi {

    private static final Logger logger = LoggerFactory.getLogger(CatalogTestPluginApi.class);

    private static final String DEFAULT_CATALOG_JSON = "WeaponsHire.pretty.json";
    private final ObjectMapper mapper;

    public CatalogTestPluginApi(final Properties properties) throws Exception {
        this.mapper = createMapper();
    }

    private String loadResourceAsString(String path) throws IOException {
        URL url = Resources.getResource(path);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        mapper.registerModule(new JodaModule());
        SimpleModule module = new SimpleModule();
        module.addDeserializer(VersionedPluginCatalog.class, new VersionedPluginCatalogJsonDeserializer());
        mapper.registerModule(module);
        return mapper;
    }

    private VersionedPluginCatalog readCatalogFromResource() {
        VersionedPluginCatalog catalog = null;
        try {
            String json = loadResourceAsString(DEFAULT_CATALOG_JSON);
            catalog = mapper.readValue(json, VersionedPluginCatalog.class);
        } catch (Exception e) {
        }
        return catalog;
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

        VersionedPluginCatalog result = readCatalogFromResource();

        if (result == null) {
            logger.error("CatalogTestPluginApi getVersionedPluginCatalog fails to read catalog from resources.. ");
        } else {
            logger.info("CatalogTestPluginApi getVersionedPluginCatalog returns result.. ");
        }
        return result;
    }

}
