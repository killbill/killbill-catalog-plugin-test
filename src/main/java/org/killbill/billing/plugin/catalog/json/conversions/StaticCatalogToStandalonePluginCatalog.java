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

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.*;
import org.killbill.billing.catalog.api.rules.PlanRules;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.plugin.catalog.models.plugin.StandalonePluginCatalogModel;

import java.util.List;
import java.util.function.Function;


public class StaticCatalogToStandalonePluginCatalog implements Function<StaticCatalog, StandalonePluginCatalog> {
    public StaticCatalogToStandalonePluginCatalog() {
    }

    @Override
    public StandalonePluginCatalog apply(StaticCatalog input) {
        return toStandalonePluginCatalog(input);
    }

    private StandalonePluginCatalog toStandalonePluginCatalog(StaticCatalog input) {
        StandalonePluginCatalog output = null;
        if (input != null) {
            try {
                Iterable<PriceList> childrenPriceList = toChildrenPriceList(input.getPriceLists());
                Iterable<Currency> currencies = Utils.<Currency>list(input.getSupportedCurrencies());
                PriceList defaultPriceList = toDefaultPriceList(input.getPriceLists());
                DateTime effectiveDate = Utils.toDateTime(input.getEffectiveDate());
                PlanRules planRules = input.getPlanRules();
                Iterable<Plan> plans = Utils.<Plan>list(input.getPlans());
                Iterable<Product> products = Utils.<Product>list(input.getProducts());
                Iterable<Unit> units = Utils.<Unit>list(input.getUnits());
                output = new StandalonePluginCatalogModel.Builder<>()
                        .withChildrenPriceList(childrenPriceList)
                        .withCurrencies(currencies)
                        .withDefaultPriceList(defaultPriceList)
                        .withEffectiveDate(effectiveDate)
                        .withPlanRules(planRules)
                        .withPlans(plans)
                        .withProducts(products)
                        .withUnits(units)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return output;
    }

    private List<PriceList> toChildrenPriceList(PriceListSet priceLists) {
        List<PriceList> output = Utils.<PriceList>list();
        if ((priceLists != null) && (priceLists.getAllPriceLists() != null)
                && (priceLists.getAllPriceLists().size() > 1)) {
            for (int i = 1; i < priceLists.getAllPriceLists().size(); i++) {
                output.add(priceLists.getAllPriceLists().get(i));
            }
        }
        return output;
    }

    private PriceList toDefaultPriceList(PriceListSet priceLists) {
        PriceList output = null;
        if ((priceLists != null) && (priceLists.getAllPriceLists() != null)
                && (priceLists.getAllPriceLists().size() > 0)) {
            output = priceLists.getAllPriceLists().get(0);
        }
        return output;
    }
}
