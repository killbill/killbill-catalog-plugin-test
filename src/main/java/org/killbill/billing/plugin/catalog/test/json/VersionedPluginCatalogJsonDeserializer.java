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

package org.killbill.billing.plugin.catalog.test.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.plugin.catalog.test.json.conversions.CatalogJsonListToVersionedPluginCatalog;
import org.killbill.billing.plugin.catalog.test.json.formats.CatalogJson;

import java.io.IOException;
import java.util.List;

public class VersionedPluginCatalogJsonDeserializer extends StdDeserializer<VersionedPluginCatalog> {

    private final TypeReference formatType;
    private final CatalogJsonListToVersionedPluginCatalog versionedPluginCatalogConversion;

    public VersionedPluginCatalogJsonDeserializer() {
        this(null);
    }

    private VersionedPluginCatalogJsonDeserializer(Class<?> vc) {
        super(vc);
        this.formatType = new TypeReference<List<CatalogJson>>() {
        };
        this.versionedPluginCatalogConversion = new CatalogJsonListToVersionedPluginCatalog();
    }

    @Override
    public VersionedPluginCatalog deserialize(JsonParser jp, DeserializationContext ctx)
            throws IOException, JsonProcessingException {
        VersionedPluginCatalog output = null;
        List<CatalogJson> jsonCatalogs = jp.readValueAs(this.formatType);
        if (jsonCatalogs != null) {
            output = this.versionedPluginCatalogConversion.apply(jsonCatalogs);
        }
        return output;
    }
}
