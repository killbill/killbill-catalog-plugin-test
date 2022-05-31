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
import org.killbill.billing.catalog.api.rules.boilerplate.CaseChangePlanPolicyImp;

import java.util.Objects;

@JsonDeserialize(builder = CaseChangePlanPolicyModel.Builder.class)
public class CaseChangePlanPolicyModel extends CaseChangePlanPolicyImp {

    public CaseChangePlanPolicyModel() {
    }

    public CaseChangePlanPolicyModel(final CaseChangePlanPolicyModel that) {
        super(that);
    }

    protected CaseChangePlanPolicyModel(final CaseChangePlanPolicyModel.Builder<?> builder) {
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
        final CaseChangePlanPolicyModel that = (CaseChangePlanPolicyModel) o;
        if (!Objects.equals(this.billingActionPolicy, that.billingActionPolicy)) {
            return false;
        }
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        if (!Objects.equals(this.fromBillingPeriod, that.fromBillingPeriod)) {
            return false;
        }
        if (!Safe.equals(this.fromPriceList, that.fromPriceList)) {
            return false;
        }
        if (!Safe.equals(this.fromProduct, that.fromProduct)) {
            return false;
        }
        if (!Objects.equals(this.fromProductCategory, that.fromProductCategory)) {
            return false;
        }
        if (!Objects.equals(this.phaseType, that.phaseType)) {
            return false;
        }
        if (!Objects.equals(this.toBillingPeriod, that.toBillingPeriod)) {
            return false;
        }
        if (!Safe.equals(this.toPriceList, that.toPriceList)) {
            return false;
        }
        if (!Safe.equals(this.toProduct, that.toProduct)) {
            return false;
        }
        if (!Objects.equals(this.toProductCategory, that.toProductCategory)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(this.billingActionPolicy);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = (31 * result) + Objects.hashCode(this.fromBillingPeriod);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.fromPriceList));
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.fromProduct));
        result = (31 * result) + Objects.hashCode(this.fromProductCategory);
        result = (31 * result) + Objects.hashCode(this.phaseType);
        result = (31 * result) + Objects.hashCode(this.toBillingPeriod);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.toPriceList));
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.toProduct));
        result = (31 * result) + Objects.hashCode(this.toProductCategory);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("billingActionPolicy=").append(this.billingActionPolicy);
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("fromBillingPeriod=").append(this.fromBillingPeriod);
        sb.append(", ");
        sb.append("fromPriceList=").append(Safe.toString(this.fromPriceList));
        sb.append(", ");
        sb.append("fromProduct=").append(Safe.toString(this.fromProduct));
        sb.append(", ");
        sb.append("fromProductCategory=").append(this.fromProductCategory);
        sb.append(", ");
        sb.append("phaseType=").append(this.phaseType);
        sb.append(", ");
        sb.append("toBillingPeriod=").append(this.toBillingPeriod);
        sb.append(", ");
        sb.append("toPriceList=").append(Safe.toString(this.toPriceList));
        sb.append(", ");
        sb.append("toProduct=").append(Safe.toString(this.toProduct));
        sb.append(", ");
        sb.append("toProductCategory=").append(this.toProductCategory);
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends CaseChangePlanPolicyModel.Builder<T>>
            extends CaseChangePlanPolicyImp.Builder<T> {

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
        public CaseChangePlanPolicyModel build() {
            return new CaseChangePlanPolicyModel(validate());
        }
    }
}
