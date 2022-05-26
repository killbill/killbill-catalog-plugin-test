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
import org.killbill.billing.catalog.api.boilerplate.PlanPhaseImp;

import java.util.Arrays;
import java.util.Objects;

@JsonDeserialize(builder = PlanPhaseModel.Builder.class)
public class PlanPhaseModel extends PlanPhaseImp {
    public PlanPhaseModel() {
    }

    public PlanPhaseModel(final PlanPhaseModel that) {
        super(that);
    }

    protected PlanPhaseModel(final PlanPhaseModel.Builder<?> builder) {
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
        final PlanPhaseModel that = (PlanPhaseModel) o;
        if (!Safe.equals(this.catalog, that.catalog)) {
            return false;
        }
        if (!Objects.equals(this.duration, that.duration)) {
            return false;
        }
        if (!Objects.equals(this.fixed, that.fixed)) {
            return false;
        }
        if (!Objects.equals(this.name, that.name)) {
            return false;
        }
        if (!Objects.equals(this.phaseType, that.phaseType)) {
            return false;
        }
        if (!Objects.equals(this.prettyName, that.prettyName)) {
            return false;
        }
        if (!Objects.equals(this.recurring, that.recurring)) {
            return false;
        }
        if (!Arrays.deepEquals(this.usages, that.usages)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(Safe.hashCode(this.catalog));
        result = (31 * result) + Objects.hashCode(this.duration);
        result = (31 * result) + Objects.hashCode(this.fixed);
        result = (31 * result) + Objects.hashCode(this.name);
        result = (31 * result) + Objects.hashCode(this.phaseType);
        result = (31 * result) + Objects.hashCode(this.prettyName);
        result = (31 * result) + Objects.hashCode(this.recurring);
        result = (31 * result) + Arrays.deepHashCode(this.usages);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("catalog=").append(Safe.toString(this.catalog));
        sb.append(", ");
        sb.append("duration=").append(this.duration);
        sb.append(", ");
        sb.append("fixed=").append(this.fixed);
        sb.append(", ");
        sb.append("name=");
        if (this.name == null) {
            sb.append(this.name);
        } else {
            sb.append("'").append(this.name).append("'");
        }
        sb.append(", ");
        sb.append("phaseType=").append(this.phaseType);
        sb.append(", ");
        sb.append("prettyName=");
        if (this.prettyName == null) {
            sb.append(this.prettyName);
        } else {
            sb.append("'").append(this.prettyName).append("'");
        }
        sb.append(", ");
        sb.append("recurring=").append(this.recurring);
        sb.append(", ");
        sb.append("usages=").append(Arrays.toString(this.usages));
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends PlanPhaseModel.Builder<T>>
            extends PlanPhaseImp.Builder<T> {
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
        public PlanPhaseModel build() {
            return new PlanPhaseModel(validate());
        }
    }
}
