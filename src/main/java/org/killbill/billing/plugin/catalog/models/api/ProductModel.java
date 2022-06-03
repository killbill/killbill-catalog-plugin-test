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
import org.killbill.billing.catalog.api.boilerplate.ProductImp;

import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(builder = ProductModel.Builder.class)
public class ProductModel extends ProductImp {
    public ProductModel() {
    }

    public ProductModel(final ProductModel that) {
        super(that);
    }

    protected ProductModel(final ProductModel.Builder<?> builder) {
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
        final ProductModel that = (ProductModel) o;
        if (!Safe.equals(this.available, that.available)) {
            return false;
        }
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        if (!Objects.equals(this.catalogName, that.catalogName)) {
            return false;
        }
        if (!Objects.equals(this.category, that.category)) {
            return false;
        }
        if (!Safe.equals(this.included, that.included)) {
            return false;
        }
        if (!Arrays.deepEquals(this.limits, that.limits)) {
            return false;
        }
        if (!Objects.equals(this.name, that.name)) {
            return false;
        }
        if (!Objects.equals(this.prettyName, that.prettyName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.available));
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = (31 * result) + Objects.hashCode(this.catalogName);
        result = (31 * result) + Objects.hashCode(this.category);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.included));
        result = (31 * result) + Arrays.deepHashCode(this.limits);
        result = (31 * result) + Objects.hashCode(this.name);
        result = (31 * result) + Objects.hashCode(this.prettyName);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("available=").append(Safe.toString(this.available));
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("catalogName=");
        if (this.catalogName == null) {
            sb.append(this.catalogName);
        } else {
            sb.append("'").append(this.catalogName).append("'");
        }
        sb.append(", ");
        sb.append("category=").append(this.category);
        sb.append(", ");
        sb.append("included=").append(Safe.toString(this.included));
        sb.append(", ");
        sb.append("limits=").append(Arrays.toString(this.limits));
        sb.append(", ");
        sb.append("name=");
        if (this.name == null) {
            sb.append(this.name);
        } else {
            sb.append("'").append(this.name).append("'");
        }
        sb.append(", ");
        sb.append("prettyName=");
        if (this.prettyName == null) {
            sb.append(this.prettyName);
        } else {
            sb.append("'").append(this.prettyName).append("'");
        }
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends ProductModel.Builder<T>>
            extends ProductImp.Builder<T> {
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
        public ProductModel build() {
            return new ProductModel(validate());
        }
    }
}
