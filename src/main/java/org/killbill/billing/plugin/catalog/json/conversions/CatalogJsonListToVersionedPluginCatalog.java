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

package org.killbill.billing.plugin.catalog.json.conversions;

import org.killbill.billing.catalog.api.StaticCatalog;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.plugin.catalog.json.formats.CatalogJson;
import org.killbill.billing.plugin.catalog.models.plugin.VersionedPluginCatalogModel;

import java.util.List;
import java.util.function.Function;

public class CatalogJsonListToVersionedPluginCatalog implements Function<List<CatalogJson>, VersionedPluginCatalog> {

    private final CatalogJsonToStaticCatalog staticCatalogConversion;
    private final StaticCatalogToStandalonePluginCatalog standalonePluginCatalogConversion;

    public CatalogJsonListToVersionedPluginCatalog() {
        this.staticCatalogConversion = new CatalogJsonToStaticCatalog();
        this.standalonePluginCatalogConversion = new StaticCatalogToStandalonePluginCatalog();
    }

    @Override
    public VersionedPluginCatalog apply(List<CatalogJson> input) {
        return toVersionedPluginCatalog(input);
    }

    private VersionedPluginCatalog toVersionedPluginCatalog(List<CatalogJson> jsonCatalogs) {
        VersionedPluginCatalog output = null;
        if (jsonCatalogs != null) {
            List<StaticCatalog> staticCatalogs = Utils.<StaticCatalog>list();
            List<StandalonePluginCatalog> standalonePluginCatalogs = Utils.<StandalonePluginCatalog>list();
            for (CatalogJson jsonCatalog : jsonCatalogs) {
                if (jsonCatalog != null) {
                    StaticCatalog staticCatalog = this.staticCatalogConversion.apply(jsonCatalog);
                    staticCatalogs.add(staticCatalog);
                    StandalonePluginCatalog standalonePluginCatalog = this.standalonePluginCatalogConversion.apply(staticCatalog);
                    standalonePluginCatalogs.add(standalonePluginCatalog);
                }
            }
            if (standalonePluginCatalogs.size() > 0) {
                output = new VersionedPluginCatalogModel.Builder<>()
                        .withCatalogName(staticCatalogs.get(0).getCatalogName())
                        .withStandalonePluginCatalogs(standalonePluginCatalogs)
                        .build();
            }
        }
        return output;
    }
}
