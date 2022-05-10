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
import org.killbill.billing.catalog.api.boilerplate.LimitImp;

@JsonDeserialize(builder = LimitModel.Builder.class)
public class LimitModel extends LimitImp {
    public LimitModel() {
    }

    public LimitModel(final LimitModel that) {
        super(that);
    }

    protected LimitModel(final LimitModel.Builder<?> builder) {
        super(builder);
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends LimitModel.Builder<T>>
            extends LimitImp.Builder<T> {
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
        public LimitModel build() {
            return new LimitModel(validate());
        }
    }
}
