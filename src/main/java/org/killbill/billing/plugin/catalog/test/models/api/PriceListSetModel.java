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
package org.killbill.billing.plugin.catalog.test.models.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.catalog.api.boilerplate.PriceListSetImp;

import java.util.Objects;

@JsonDeserialize(builder = PriceListSetModel.Builder.class)
public class PriceListSetModel extends PriceListSetImp {
    public PriceListSetModel() {
    }

    public PriceListSetModel(final PriceListSetModel that) {
        super(that);
    }

    protected PriceListSetModel(final PriceListSetModel.Builder<?> builder) {
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
        final PriceListSetModel that = (PriceListSetModel) o;
        if (!Objects.equals(this.allPriceLists, that.allPriceLists)) {
            return false;
        }
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(this.allPriceLists);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("allPriceLists=").append(this.allPriceLists);
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends PriceListSetModel.Builder<T>>
            extends PriceListSetImp.Builder<T> {
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
        public PriceListSetModel build() {
            return new PriceListSetModel(validate());
        }
    }
}
