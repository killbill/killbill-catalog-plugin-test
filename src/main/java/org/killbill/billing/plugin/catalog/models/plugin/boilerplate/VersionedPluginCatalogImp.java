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

package org.killbill.billing.plugin.catalog.models.plugin.boilerplate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;

import java.io.Serializable;
import java.util.Objects;

@JsonDeserialize(builder = VersionedPluginCatalogImp.Builder.class)
public class VersionedPluginCatalogImp implements VersionedPluginCatalog, Serializable {

    private static final long serialVersionUID = 0x40702BFEB20D4F4BL;

    protected String catalogName;
    protected Iterable<StandalonePluginCatalog> standalonePluginCatalogs;

    public VersionedPluginCatalogImp(final VersionedPluginCatalogImp that) {
        this.catalogName = that.catalogName;
        this.standalonePluginCatalogs = that.standalonePluginCatalogs;
    }

    protected VersionedPluginCatalogImp(final VersionedPluginCatalogImp.Builder<?> builder) {
        this.catalogName = builder.catalogName;
        this.standalonePluginCatalogs = builder.standalonePluginCatalogs;
    }

    protected VersionedPluginCatalogImp() {
    }

    @Override
    public String getCatalogName() {
        return this.catalogName;
    }

    @Override
    public Iterable<StandalonePluginCatalog> getStandalonePluginCatalogs() {
        return this.standalonePluginCatalogs;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        final VersionedPluginCatalogImp that = (VersionedPluginCatalogImp) o;
        if (!Objects.equals(this.catalogName, that.catalogName)) {
            return false;
        }
        if (!Objects.equals(this.standalonePluginCatalogs, that.standalonePluginCatalogs)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = (31 * result) + Objects.hashCode(this.catalogName);
        result = (31 * result) + Objects.hashCode(this.standalonePluginCatalogs);
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.getClass().getSimpleName());
        sb.append("{");
        sb.append("catalogName=");
        if (this.catalogName == null) {
            sb.append(this.catalogName);
        } else {
            sb.append("'").append(this.catalogName).append("'");
        }
        sb.append(", ");
        sb.append("standalonePluginCatalogs=").append(this.standalonePluginCatalogs);
        sb.append("}");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static class Builder<T extends VersionedPluginCatalogImp.Builder<T>> {

        protected String catalogName;
        protected Iterable<StandalonePluginCatalog> standalonePluginCatalogs;

        public Builder() {
        }

        public Builder(final Builder that) {
            this.catalogName = that.catalogName;
            this.standalonePluginCatalogs = that.standalonePluginCatalogs;
        }

        public T withCatalogName(final String catalogName) {
            this.catalogName = catalogName;
            return (T) this;
        }

        public T withStandalonePluginCatalogs(final Iterable<StandalonePluginCatalog> standalonePluginCatalogs) {
            this.standalonePluginCatalogs = standalonePluginCatalogs;
            return (T) this;
        }

        public T source(final VersionedPluginCatalog that) {
            this.catalogName = that.getCatalogName();
            this.standalonePluginCatalogs = that.getStandalonePluginCatalogs();
            return (T) this;
        }

        protected Builder validate() {
            return this;
        }

        public VersionedPluginCatalogImp build() {
            return new VersionedPluginCatalogImp(this.validate());
        }
    }
}
