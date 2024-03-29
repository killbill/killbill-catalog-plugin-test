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
import org.killbill.billing.catalog.api.boilerplate.PriceImp;

@JsonDeserialize(builder = PriceModel.Builder.class)
public class PriceModel extends PriceImp {
    public PriceModel() {
    }

    public PriceModel(final PriceModel that) {
        super(that);
    }

    protected PriceModel(final PriceModel.Builder<?> builder) {
        super(builder);
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends PriceModel.Builder<T>>
            extends PriceImp.Builder<T> {
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
        public PriceModel build() {
            return new PriceModel(validate());
        }
    }
}
