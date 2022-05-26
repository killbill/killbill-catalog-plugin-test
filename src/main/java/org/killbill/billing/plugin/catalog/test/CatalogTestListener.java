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
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.util.callcontext.boilerplate.TenantContextImp;
import org.osgi.service.log.LogService;


public class CatalogTestListener implements OSGIKillbillEventDispatcher.OSGIKillbillEventHandler {

    private final LogService logService;
    private final OSGIKillbillAPI osgiKillbillAPI;

    public CatalogTestListener(final OSGIKillbillLogService logService, final OSGIKillbillAPI killbillAPI) {
        this.logService = logService;
        this.osgiKillbillAPI = killbillAPI;
    }

    @Override
    public void handleKillbillEvent(final ExtBusEvent killbillEvent) {
        logService.log(LogService.LOG_INFO, "Received event " + killbillEvent.getEventType() +
                " for object id " + killbillEvent.getObjectId() +
                " of type " + killbillEvent.getObjectType());
        try {
            final Account account = osgiKillbillAPI
                    .getAccountUserApi()
                    .getAccountById(killbillEvent.getAccountId(),
                            new TenantContextImp.Builder<>()
                                    .withAccountId(killbillEvent.getAccountId())
                                    .withTenantId(killbillEvent.getTenantId())
                                    .build());
            logService.log(LogService.LOG_INFO, "Account information: " + account);
        } catch (final AccountApiException e) {
            logService.log(LogService.LOG_WARNING, "Unable to find account", e);
        }
    }
}
