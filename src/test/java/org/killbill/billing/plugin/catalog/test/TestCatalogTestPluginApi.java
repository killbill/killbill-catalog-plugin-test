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

import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.callcontext.boilerplate.TenantContextImp;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.UUID;

@Test(groups = {"fast"})
public class TestCatalogTestPluginApi {

    private CatalogTestPluginApi api;
    private VersionedPluginCatalog rhs;
    private VersionedPluginCatalog lhs;

    public TestCatalogTestPluginApi() {
    }

    @BeforeClass
    public void setup() throws Exception {
        final UUID tenantId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        final UUID accountId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        this.api = new CatalogTestPluginApi(null);
        this.lhs = getCatalog(api, tenantId, accountId);
        this.rhs = getCatalog(api, tenantId, accountId);
    }

    @Test
    public void getLatestCatalogVersionIsNull() {
        Assert.assertTrue(null == api.getLatestCatalogVersion(null, null));
    }

    @Test
    public void versionedPluginCatalogIsNotNull() {
        Assert.assertTrue((lhs != null) && (rhs != null));
    }

    @Test
    public void versionedPluginCatalogShouldBeDifferentInstance() {
        Assert.assertTrue(lhs != rhs);
    }

    @Test
    public void versionedPluginCatalogShouldBeEqual() {
        Assert.assertTrue(lhs.equals(rhs));
    }

    @Test
    public void versionedPluginCatalogHaveEqualHashCode() {
        Assert.assertTrue(lhs.hashCode() == rhs.hashCode());
    }

    @Test
    public void versionedPluginCatalogHaveEqualToString() {
        Assert.assertTrue(lhs.toString().equals(rhs.toString()));
    }

    private static VersionedPluginCatalog getCatalog(final CatalogTestPluginApi api, final UUID tenantId, final UUID accountId) {
        VersionedPluginCatalog catalog = null;
        try {
            Iterable<PluginProperty> properties = new ArrayList<PluginProperty>();
            TenantContext tenantContext = new TenantContextImp.Builder<>()
                    .withAccountId(accountId)
                    .withTenantId(tenantId)
                    .build();
            catalog = api.getVersionedPluginCatalog(properties, tenantContext);
        } catch (Exception e) {
        }
        return catalog;
    }
}
