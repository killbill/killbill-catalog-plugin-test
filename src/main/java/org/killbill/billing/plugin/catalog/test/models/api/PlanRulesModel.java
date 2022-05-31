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
import org.killbill.billing.catalog.api.rules.boilerplate.PlanRulesImp;

import java.util.Objects;

@JsonDeserialize(builder = PlanRulesModel.Builder.class)
public class PlanRulesModel extends PlanRulesImp {
    public PlanRulesModel() {
    }

    public PlanRulesModel(final PlanRulesModel that) {
        super(that);
    }

    protected PlanRulesModel(final PlanRulesModel.Builder<?> builder) {
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
        final PlanRulesModel that = (PlanRulesModel) o;
        if (!Objects.equals(this.caseBillingAlignment, that.caseBillingAlignment)) {
            return false;
        }
        if (!Objects.equals(this.caseCancelPolicy, that.caseCancelPolicy)) {
            return false;
        }
        if (!Objects.equals(this.caseChangePlanAlignment, that.caseChangePlanAlignment)) {
            return false;
        }
        if (!Objects.equals(this.caseChangePlanPolicy, that.caseChangePlanPolicy)) {
            return false;
        }
        if (!Objects.equals(this.caseCreateAlignment, that.caseCreateAlignment)) {
            return false;
        }
        if (!Objects.equals(this.casePriceList, that.casePriceList)) {
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
        result = (31 * result) + Objects.hashCode(this.caseBillingAlignment);
        result = (31 * result) + Objects.hashCode(this.caseCancelPolicy);
        result = (31 * result) + Objects.hashCode(this.caseChangePlanAlignment);
        result = (31 * result) + Objects.hashCode(this.caseChangePlanPolicy);
        result = (31 * result) + Objects.hashCode(this.caseCreateAlignment);
        result = (31 * result) + Objects.hashCode(this.casePriceList);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("caseBillingAlignment=").append(this.caseBillingAlignment);
        sb.append(", ");
        sb.append("caseCancelPolicy=").append(this.caseCancelPolicy);
        sb.append(", ");
        sb.append("caseChangePlanAlignment=").append(this.caseChangePlanAlignment);
        sb.append(", ");
        sb.append("caseChangePlanPolicy=").append(this.caseChangePlanPolicy);
        sb.append(", ");
        sb.append("caseCreateAlignment=").append(this.caseCreateAlignment);
        sb.append(", ");
        sb.append("casePriceList=").append(this.casePriceList);
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends PlanRulesModel.Builder<T>>
            extends PlanRulesImp.Builder<T> {
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
        public PlanRulesModel build() {
            return new PlanRulesModel(validate());
        }
    }
}
