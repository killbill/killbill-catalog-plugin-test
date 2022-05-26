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
import org.killbill.billing.catalog.api.boilerplate.UsageImp;

import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(builder = UsageModel.Builder.class)
public class UsageModel extends UsageImp {
    public UsageModel() {
    }

    public UsageModel(final UsageModel that) {
        super(that);
    }

    protected UsageModel(final UsageModel.Builder<?> builder) {
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
        final UsageModel that = (UsageModel) o;
        if (!Objects.equals(this.billingMode, that.billingMode)) {
            return false;
        }
        if (!Objects.equals(this.billingPeriod, that.billingPeriod)) {
            return false;
        }
        if (!Arrays.deepEquals(this.blocks, that.blocks)) {
            return false;
        }
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        if (!Objects.equals(this.fixedPrice, that.fixedPrice)) {
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
        if (!Objects.equals(this.recurringPrice, that.recurringPrice)) {
            return false;
        }
        if (!Objects.equals(this.tierBlockPolicy, that.tierBlockPolicy)) {
            return false;
        }
        if (!Arrays.deepEquals(this.tiers, that.tiers)) {
            return false;
        }
        if (!Objects.equals(this.usageType, that.usageType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(this.billingMode);
        result = (31 * result) + Objects.hashCode(this.billingPeriod);
        result = (31 * result) + Arrays.deepHashCode(this.blocks);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = (31 * result) + Objects.hashCode(this.fixedPrice);
        result = (31 * result) + Arrays.deepHashCode(this.limits);
        result = (31 * result) + Objects.hashCode(this.name);
        result = (31 * result) + Objects.hashCode(this.prettyName);
        result = (31 * result) + Objects.hashCode(this.recurringPrice);
        result = (31 * result) + Objects.hashCode(this.tierBlockPolicy);
        result = (31 * result) + Arrays.deepHashCode(this.tiers);
        result = (31 * result) + Objects.hashCode(this.usageType);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("billingMode=").append(this.billingMode);
        sb.append(", ");
        sb.append("billingPeriod=").append(this.billingPeriod);
        sb.append(", ");
        sb.append("blocks=").append(Arrays.toString(this.blocks));
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("fixedPrice=").append(this.fixedPrice);
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
        sb.append(", ");
        sb.append("recurringPrice=").append(this.recurringPrice);
        sb.append(", ");
        sb.append("tierBlockPolicy=").append(this.tierBlockPolicy);
        sb.append(", ");
        sb.append("tiers=").append(Arrays.toString(this.tiers));
        sb.append(", ");
        sb.append("usageType=").append(this.usageType);
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends UsageModel.Builder<T>>
            extends UsageImp.Builder<T> {
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
        public UsageModel build() {
            return new UsageModel(validate());
        }
    }
}
