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

import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.notification.plugin.api.ExtBusEvent;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillEventDispatcher;
import org.killbill.billing.util.callcontext.boilerplate.TenantContextImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CatalogTestListener implements OSGIKillbillEventDispatcher.OSGIKillbillEventHandler {

    private final static Logger logger = LoggerFactory.getLogger(CatalogTestListener.class);
    private final OSGIKillbillAPI osgiKillbillAPI;

    public CatalogTestListener(final OSGIKillbillAPI killbillAPI) {
        this.osgiKillbillAPI = killbillAPI;
    }

    @Override
    public void handleKillbillEvent(final ExtBusEvent killbillEvent) {
        logger.info("Received event " + killbillEvent.getEventType() + " for object id " + killbillEvent.getObjectId() + " of type " + killbillEvent.getObjectType());
        try {
            final Account account = osgiKillbillAPI.getAccountUserApi().getAccountById(killbillEvent.getAccountId(), new TenantContextImp.Builder<>().withAccountId(killbillEvent.getAccountId()).withTenantId(killbillEvent.getTenantId()).build());
            logger.info("Account information: " + account);
        } catch (final AccountApiException e) {
            logger.warn("Unable to find account", e);
        }
    }
}
