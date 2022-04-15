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
import org.killbill.billing.catalog.api.Tier;
import org.killbill.billing.catalog.api.boilerplate.TierImp;

@JsonDeserialize( builder = TierModel.Builder.class )
public class TierModel extends TierImp {
    public TierModel() { }
    public TierModel(final TierModel that) {
        super(that);
    }
    protected TierModel(final TierModel.Builder<?> builder) {
        super(builder);
    }
    @SuppressWarnings("unchecked")
    public static class Builder<T extends TierModel.Builder<T>> 
        extends TierImp.Builder<T> {
        public Builder() { }
        public Builder(final Builder that) {
            super(that);
        }
        @Override
        protected Builder validate() {
            return this;
        }
        @Override
        public TierModel build() {
            return new TierModel(validate());
        }
    }
}
