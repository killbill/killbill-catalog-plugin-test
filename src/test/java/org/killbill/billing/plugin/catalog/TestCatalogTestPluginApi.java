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

import com.google.common.io.Resources;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.callcontext.boilerplate.TenantContextImp;
import org.killbill.billing.util.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Test(groups = {"fast"})
public class TestCatalogTestPluginApi {

    private final static DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTimeParser();
    private UUID accountId;


    public TestCatalogTestPluginApi() {
    }


    @BeforeClass
    public void setup() {
        accountId = UUID.randomUUID();
    }

    @Test
    public void testReadCatalogFromResource() throws Exception {

        final CatalogConfiguration config = createCatalogConfig("WeaponsHire.xml");
        final CatalogConfigurationHandler configHandler = new CatalogConfigurationHandler("", CatalogActivator.PLUGIN_NAME, null);
        configHandler.setDefaultConfigurable(config);

        final CatalogPluginApi api = new CatalogPluginApi(configHandler, null);

        final TenantContext tenantContext = new TenantContextImp.Builder<>()
                .withAccountId(accountId)
                .withTenantId(null)
                .build();

        final VersionedPluginCatalog catalog = api.getVersionedPluginCatalog(Collections.emptyList(), tenantContext);
        final Iterable<StandalonePluginCatalog> rawVersions = catalog.getStandalonePluginCatalogs();

        verifyCatalog(rawVersions);
    }

    @Test
    public void testReadCatalogFromFilesystem() throws Exception {

        // Use resource WeaponsHire.xml to create the file-based catalog
        final URL url = Resources.getResource("WeaponsHire.xml");
        final String catalogPath = writeCatalogToDisk(accountId, url);

        // Create catalog config pointing towards a filesystem-based catalog
        final String uri = String.format("file://%s", catalogPath);
        final CatalogConfiguration config = createCatalogConfig(uri);

        final CatalogConfigurationHandler configHandler = new CatalogConfigurationHandler("", CatalogActivator.PLUGIN_NAME, null);
        configHandler.setDefaultConfigurable(config);

        final CatalogPluginApi api = new CatalogPluginApi(configHandler, null);

        final TenantContext tenantContext = new TenantContextImp.Builder<>()
                .withAccountId(accountId)
                .withTenantId(null)
                .build();

        final VersionedPluginCatalog catalog = api.getVersionedPluginCatalog(Collections.emptyList(), tenantContext);
        final Iterable<StandalonePluginCatalog> rawVersions = catalog.getStandalonePluginCatalogs();

        verifyCatalog(rawVersions);
    }

    private void verifyCatalog(final Iterable<StandalonePluginCatalog> rawVersions) {

        final List<StandalonePluginCatalog> versions = StreamSupport.stream(rawVersions.spliterator(), false).collect(Collectors.toList());
        Assert.assertEquals(1, versions.size());

        final StandalonePluginCatalog v1 = versions.get(0);
        final DateTime expEffDt = DATE_TIME_FORMATTER.parseDateTime("2011-10-08T00:00:00.000Z");
        Assert.assertTrue(expEffDt.compareTo(v1.getEffectiveDate()) == 0);

        Assert.assertEquals(11, v1.getDefaultPriceList().getPlans().size());
    }

    private static CatalogConfiguration createCatalogConfig(final String uri) {
        final String raw = String.format("!!org.killbill.billing.plugin.catalog.CatalogYAMLConfiguration\n" +
                "  uri: %s\n" +
                "  validateAccount: false\n" +
                "  accountCatalog: true", uri);
        return CatalogConfigurationHandler.fromYAML(raw);
    }

    private static String writeCatalogToDisk(final UUID accountId, final URL url) throws IOException {
        final Path tmpRootDir = Files.createTempDirectory(accountId.toString());

        final String tmpDirName = String.format("%s/%s", tmpRootDir.toAbsolutePath(), accountId);
        final Path tmpDir = Files.createDirectory(Paths.get(tmpDirName));

        // Write resource catalog on the filesystem
        final Path tempFile = Files.createTempFile(tmpDir, "v1_", ".xml");
        final String content = IOUtils.toString(url.openStream());
        Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
        return tmpRootDir.toString();
    }

}
