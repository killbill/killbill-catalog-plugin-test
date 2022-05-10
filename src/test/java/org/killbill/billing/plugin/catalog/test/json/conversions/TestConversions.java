/*
 * Copyright 2022-2022 The Billing Project, LLC
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
package org.killbill.billing.plugin.catalog.test.json.conversions;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.io.Resources;
import org.killbill.billing.catalog.StandaloneCatalog;
import org.killbill.billing.catalog.api.StaticCatalog;
import org.killbill.billing.catalog.plugin.StandaloneCatalogMapper;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.plugin.catalog.test.json.formats.CatalogJson;
import org.killbill.xmlloader.ValidationErrors;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Test(groups = {"fast"})
public class TestConversions {
    private final String CATALOG_RESOURCE = "WeaponsHire.pretty.json";
    private final String CATALOG_NAME = "Firearms";
    private ObjectMapper mapper;
    private CatalogJsonToStaticCatalog staticCatalogConversion;
    private StaticCatalogToStandalonePluginCatalog standalonePluginCatalogConversion;
    private CatalogJsonListToVersionedPluginCatalog versionedPluginCatalogConversion;

    @BeforeClass
    public void setup() {
        this.mapper = new ObjectMapper();
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        this.mapper.registerModule(new JodaModule());
        this.staticCatalogConversion = new CatalogJsonToStaticCatalog();
        this.standalonePluginCatalogConversion = new StaticCatalogToStandalonePluginCatalog();
        this.versionedPluginCatalogConversion = new CatalogJsonListToVersionedPluginCatalog();
    }

    @Test
    public void roundTripJson() throws Exception {
        String input = load(CATALOG_RESOURCE);
        List<CatalogJson> jsonCatalogs = parse(input);
        CatalogJsonToStaticCatalog conversion = new CatalogJsonToStaticCatalog();
        StaticCatalog staticCatalog = conversion.apply(jsonCatalogs.get(0));
        CatalogJson jsonCatalog = new CatalogJson(staticCatalog);
        List<CatalogJson> list = new ArrayList<CatalogJson>();
        list.add(jsonCatalog);
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        JsonNode lhs = mapper.readTree(input);
        JsonNode rhs = mapper.readTree(output);
        Assert.assertEquals(lhs, rhs);
    }

    @Test
    public void extendedRoundTripJson() throws Exception {
        String input = load(CATALOG_RESOURCE);
        List<CatalogJson> jsonCatalogs = parse(input);
        VersionedPluginCatalog versionedPluginCatalog = versionedPluginCatalogConversion.apply(jsonCatalogs);
        StandaloneCatalog standaloneCatalog = new StandaloneCatalogMapper(CATALOG_NAME).toStandaloneCatalog(
                versionedPluginCatalog.getStandalonePluginCatalogs().iterator().next());
        CatalogJson jsonCatalog = new CatalogJson(standaloneCatalog);
        List<CatalogJson> list = new ArrayList<CatalogJson>();
        list.add(jsonCatalog);
        String output = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        JsonNode lhs = mapper.readTree(input);
        JsonNode rhs = mapper.readTree(output);
        Assert.assertEquals(lhs, rhs);
    }

    @Test
    public void validateStandaloneCatalogMapping() throws Exception {
        String input = load(CATALOG_RESOURCE);
        List<CatalogJson> jsonCatalogs = parse(input);
        VersionedPluginCatalog versionedPluginCatalog = versionedPluginCatalogConversion.apply(jsonCatalogs);
        StandaloneCatalog standaloneCatalog = new StandaloneCatalogMapper(CATALOG_NAME).toStandaloneCatalog(
                versionedPluginCatalog.getStandalonePluginCatalogs().iterator().next());
        ValidationErrors errors = new ValidationErrors();
        standaloneCatalog.validate(standaloneCatalog, errors);
        System.out.println(errors.toString());
        Assert.assertTrue(errors.isEmpty());
    }

    private List<CatalogJson> parse(String json) throws IOException, JsonParseException, JsonMappingException {
        return mapper.readValue(json, new TypeReference<List<CatalogJson>>() {
        });
    }

    private String load(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
