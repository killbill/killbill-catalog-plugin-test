/*
 * Copyright 2022-2022 The Billing Project, LLC
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

import java.util.Arrays;
import java.util.Objects;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.catalog.api.Plan;
import org.killbill.billing.catalog.api.boilerplate.PlanImp;

@JsonDeserialize( builder = PlanModel.Builder.class )
public class PlanModel extends PlanImp {
    public PlanModel() { }
    public PlanModel(final PlanModel that) {
        super(that);
    }
    protected PlanModel(final PlanModel.Builder<?> builder) {
        super(builder);
    }
    @Override
    public boolean equals(final Object o) {
        if ( this == o ) {
            return true;
        }
        if ( ( o == null ) || ( this.getClass() != o.getClass() ) ) {
            return false;
        }
        final PlanModel that = (PlanModel) o;
        if( !Arrays.deepEquals(this.allPhases, that.allPhases) ) {
            return false;
        }
        if( !Safe.equals(this.catalog, that.catalog) ) {
            return false;
        }
        if( !Objects.equals(this.effectiveDateForExistingSubscriptions, that.effectiveDateForExistingSubscriptions) ) {
            return false;
        }
        if( !Objects.equals(this.finalPhase, that.finalPhase) ) {
            return false;
        }
        if( !Safe.equals(this.initialPhaseIterator, that.initialPhaseIterator) ) {
            return false;
        }
        if( !Arrays.deepEquals(this.initialPhases, that.initialPhases) ) {
            return false;
        }
        if( !Objects.equals(this.name, that.name) ) {
            return false;
        }
        if( this.plansAllowedInBundle != that.plansAllowedInBundle ) {
            return false;
        }
        if( !Objects.equals(this.prettyName, that.prettyName) ) {
            return false;
        }
        if( !Safe.equals(this.priceList, that.priceList) ) {
            return false;
        }
        if( !Safe.equals(this.product, that.product) ) {
            return false;
        }
        if( !Objects.equals(this.recurringBillingMode, that.recurringBillingMode) ) {
            return false;
        }
        if( !Objects.equals(this.recurringBillingPeriod, that.recurringBillingPeriod) ) {
            return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result = 1;
        result = ( 31 * result ) + Arrays.deepHashCode(this.allPhases);
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = ( 31 * result ) + Objects.hashCode(this.effectiveDateForExistingSubscriptions);
        result = ( 31 * result ) + Objects.hashCode(this.finalPhase);
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.initialPhaseIterator));
        result = ( 31 * result ) + Arrays.deepHashCode(this.initialPhases);
        result = ( 31 * result ) + Objects.hashCode(this.name);
        result = ( 31 * result ) + Objects.hashCode(this.plansAllowedInBundle);
        result = ( 31 * result ) + Objects.hashCode(this.prettyName);
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.priceList));
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.product));
        result = ( 31 * result ) + Objects.hashCode(this.recurringBillingMode);
        result = ( 31 * result ) + Objects.hashCode(this.recurringBillingPeriod);
        return result;
    }
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("allPhases=").append(Arrays.toString(this.allPhases));
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("effectiveDateForExistingSubscriptions=").append(this.effectiveDateForExistingSubscriptions);
        sb.append(", ");
        sb.append("finalPhase=").append(this.finalPhase);
        sb.append(", ");
        sb.append("initialPhaseIterator=").append(Safe.toString(this.initialPhaseIterator));
        sb.append(", ");
        sb.append("initialPhases=").append(Arrays.toString(this.initialPhases));
        sb.append(", ");
        sb.append("name=");
        if( this.name == null ) {
            sb.append(this.name);
        }else{
            sb.append("'").append(this.name).append("'");
        }
        sb.append(", ");
        sb.append("plansAllowedInBundle=").append(this.plansAllowedInBundle);
        sb.append(", ");
        sb.append("prettyName=");
        if( this.prettyName == null ) {
            sb.append(this.prettyName);
        }else{
            sb.append("'").append(this.prettyName).append("'");
        }
        sb.append(", ");
        sb.append("priceList=").append(Safe.toString(this.priceList));
        sb.append(", ");
        sb.append("product=").append(Safe.toString(this.product));
        sb.append(", ");
        sb.append("recurringBillingMode=").append(this.recurringBillingMode);
        sb.append(", ");
        sb.append("recurringBillingPeriod=").append(this.recurringBillingPeriod);
        sb.append("}");
        return sb.toString();
    }
    @SuppressWarnings("unchecked")
    public static class Builder<T extends PlanModel.Builder<T>> 
        extends PlanImp.Builder<T> {
        public Builder() { }
        public Builder(final Builder that) {
            super(that);
        }
        @Override
        protected Builder validate() {
            return this;
        }
        @Override
        public PlanModel build() {
            return new PlanModel(validate());
        }
    }
}
