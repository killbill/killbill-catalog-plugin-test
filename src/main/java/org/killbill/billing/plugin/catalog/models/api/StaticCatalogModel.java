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
package org.killbill.billing.plugin.catalog.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.catalog.api.boilerplate.StaticCatalogImp;

import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(builder = StaticCatalogModel.Builder.class)
public class StaticCatalogModel extends StaticCatalogImp {
    public StaticCatalogModel() {
    }

    public StaticCatalogModel(final StaticCatalogModel that) {
        super(that);
    }

    protected StaticCatalogModel(final StaticCatalogModel.Builder<?> builder) {
        super(builder);
    }

    protected StaticCatalogModel(final StaticCatalogModel.Builder<?> builder,
                                 final PriceListSetModel.Builder<?> deferredPriceLists,
                                 final PlanRulesModel.Builder<?> deferredPlanRules) {
        super(builder);
        if (deferredPriceLists != null) {
            this.priceLists = deferredPriceLists.withCatalog(this).build();
        }
        if (deferredPlanRules != null) {
            this.planRules = deferredPlanRules.withCatalog(this).build();
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        final StaticCatalogModel that = (StaticCatalogModel) o;
        if (!Objects.equals(this.availableBasePlanListings, that.availableBasePlanListings)) {
            return false;
        }
        if (!Objects.equals(this.catalogName, that.catalogName)) {
            return false;
        }
        if (!Objects.equals(this.effectiveDate, that.effectiveDate)) {
            return false;
        }
        if (!Objects.equals(this.planRules, that.planRules)) {
            return false;
        }
        if (!Objects.equals(this.plans, that.plans)) {
            return false;
        }
        if (!Objects.equals(this.priceLists, that.priceLists)) {
            return false;
        }
        if (!Objects.equals(this.products, that.products)) {
            return false;
        }
        if (!Arrays.deepEquals(this.supportedCurrencies, that.supportedCurrencies)) {
            return false;
        }
        if (!Arrays.deepEquals(this.units, that.units)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends StaticCatalogModel.Builder<T>>
            extends StaticCatalogImp.Builder<T> {
        public Builder() {
        }

        public Builder(final Builder that) {
            super(that);
        }

        @Override
        public StaticCatalogModel build() {
            return new StaticCatalogModel(validate());
        }

        @Override
        protected Builder validate() {
            return this;
        }

        public StaticCatalogModel build(final PriceListSetModel.Builder<?> pricelists,
                                        final PlanRulesModel.Builder<?> planRules) {
            return new StaticCatalogModel(validate(), pricelists, planRules);
        }
    }
}
