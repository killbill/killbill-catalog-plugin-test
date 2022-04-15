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
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.PriceList;
import org.killbill.billing.catalog.api.Product;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.catalog.api.StaticCatalog;
import org.killbill.billing.catalog.api.rules.boilerplate.CasePriceListImp;

@JsonDeserialize( builder = CasePriceListModel.Builder.class )
public class CasePriceListModel extends CasePriceListImp {

    public CasePriceListModel() { }
    public CasePriceListModel(final CasePriceListModel that) {
        super(that);
    }
    protected CasePriceListModel(final CasePriceListModel.Builder<?> builder) {
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
        final CasePriceListModel that = (CasePriceListModel) o;
        if( !Objects.equals(this.billingPeriod, that.billingPeriod) ) {
            return false;
        }
        if( !Safe.equals(this.catalog, that.catalog) ) {
            return false;
        }
        if( !Safe.equals(this.destinationPriceList, that.destinationPriceList) ) {
            return false;
        }
        if( !Safe.equals(this.priceList, that.priceList) ) {
            return false;
        }
        if( !Safe.equals(this.product, that.product) ) {
            return false;
        }
        if( !Objects.equals(this.productCategory, that.productCategory) ) {
            return false;
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result = 1;
        result = ( 31 * result ) + Objects.hashCode(this.billingPeriod);
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.destinationPriceList));
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.priceList));
        result = ( 31 * result ) + Objects.hashCode(Safe.hashCode(this.product));
        result = ( 31 * result ) + Objects.hashCode(this.productCategory);
        return result;
    }
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("billingPeriod=").append(this.billingPeriod);
        sb.append(", ");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("destinationPriceList=").append(Safe.toString(this.destinationPriceList));
        sb.append(", ");
        sb.append("priceList=").append(Safe.toString(this.priceList));
        sb.append(", ");
        sb.append("product=").append(Safe.toString(this.product));
        sb.append(", ");
        sb.append("productCategory=").append(this.productCategory);
        sb.append("}");
        return sb.toString();
    }


    @SuppressWarnings("unchecked")
    public static class Builder<T extends CasePriceListModel.Builder<T>> 
        extends CasePriceListImp.Builder<T> {

        public Builder(){}
        public Builder(final Builder that) {
            super(that);
        }
        @Override
        protected Builder validate() {
            return this;
        }
        @Override
        public CasePriceListModel build() {
            return new CasePriceListModel(validate());
        }
    }
}
