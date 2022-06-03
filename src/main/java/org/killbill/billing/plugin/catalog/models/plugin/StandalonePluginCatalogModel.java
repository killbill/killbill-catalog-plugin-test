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
package org.killbill.billing.plugin.catalog.models.plugin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.plugin.catalog.models.plugin.boilerplate.StandalonePluginCatalogImp;

import java.util.Objects;

@JsonDeserialize(builder = StandalonePluginCatalogModel.Builder.class)
public class StandalonePluginCatalogModel extends StandalonePluginCatalogImp {

    public StandalonePluginCatalogModel() {
    }

    public StandalonePluginCatalogModel(final StandalonePluginCatalogModel that) {
        super(that);
    }

    protected StandalonePluginCatalogModel(final StandalonePluginCatalogModel.Builder<?> builder) {
        super(builder);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        final StandalonePluginCatalogModel that = (StandalonePluginCatalogModel) o;
        if (!Objects.equals(this.childrenPriceList, that.childrenPriceList)) {
            return false;
        }
        if (!Objects.equals(this.currencies, that.currencies)) {
            return false;
        }
        if (!Objects.equals(this.defaultPriceList, that.defaultPriceList)) {
            return false;
        }
        if ((this.effectiveDate != null) ? (0 != this.effectiveDate.compareTo(that.effectiveDate)) : (that.effectiveDate != null)) {
            return false;
        }
        if (!Objects.equals(this.planRules, that.planRules)) {
            return false;
        }
        if (!Objects.equals(this.plans, that.plans)) {
            return false;
        }
        if (!Objects.equals(this.products, that.products)) {
            return false;
        }
        if (!Objects.equals(this.units, that.units)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends StandalonePluginCatalogModel.Builder<T>>
            extends StandalonePluginCatalogImp.Builder<T> {

        public Builder() {
        }

        public Builder(final Builder that) {
            super(that);
        }

        @Override
        protected Builder validate() {
            return this;
        }

        @Override
        public StandalonePluginCatalogModel build() {
            return new StandalonePluginCatalogModel(this.validate());
        }
    }
}
