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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.catalog.api.boilerplate.PriceListImp;

import java.util.Objects;

@JsonDeserialize(builder = PriceListModel.Builder.class)
public class PriceListModel extends PriceListImp {
    public PriceListModel() {
    }

    public PriceListModel(final PriceListModel that) {
        super(that);
    }

    protected PriceListModel(final PriceListModel.Builder<?> builder) {
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
        final PriceListModel that = (PriceListModel) o;
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        if (!Objects.equals(this.name, that.name)) {
            return false;
        }
        if (!Safe.equals(this.plans, that.plans)) {
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
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = (31 * result) + Objects.hashCode(this.name);
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.plans));
        result = (31 * result) + Objects.hashCode(this.prettyName);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("name=");
        if (this.name == null) {
            sb.append(this.name);
        } else {
            sb.append("'").append(this.name).append("'");
        }
        sb.append(", ");
        sb.append("plans=").append(Safe.toString(this.plans));
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
    public static class Builder<T extends PriceListModel.Builder<T>>
            extends PriceListImp.Builder<T> {
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
        public PriceListModel build() {
            return new PriceListModel(validate());
        }
    }
}
