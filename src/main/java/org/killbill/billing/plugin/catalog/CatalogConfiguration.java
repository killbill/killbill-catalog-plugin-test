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

package org.killbill.billing.plugin.catalog;

import com.google.common.io.Resources;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class CatalogConfiguration {

    private URI uri;

    private boolean validateAccount = false;

    private boolean accountCatalog = false;

    public CatalogConfiguration(final CatalogYAMLConfiguration raw) {
        try {
            this.uri = new URI(raw.uri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(String.format("Unexpected URI % (cannot parse)", uri), e);
        }
        this.validateAccount = raw.validateAccount;
        this.accountCatalog = raw.accountCatalog;
    }

    public URI getURI() {
        return uri;
    }

    public boolean shouldValidateAccount() {
        return validateAccount;
    }

    public boolean isPerAccount() {
        return accountCatalog;
    }

    public Scheme getScheme() {
        final URI uri = getURI();
        String scheme = uri.getScheme();
        if (scheme == null) {
            return Scheme.RESOURCE;
        } else if (scheme.equals("file")) {
            return Scheme.FILE_BASED;
        }
        throw new RuntimeException(String.format("Unexpected URI %s (bad scheme)", uri));
    }

    public enum Scheme {
        RESOURCE,
        FILE_BASED;
    }

    private static URL toURL(final URI inputURI) throws IOException, URISyntaxException {
        String scheme = inputURI.getScheme();
        URI uri;
        if (scheme == null) {
            uri = new URI(Resources.getResource(inputURI.toString()).toExternalForm());
        } else if (scheme.equals("file") && !inputURI.getSchemeSpecificPart().startsWith("/")) {
            uri = (new File(inputURI.getSchemeSpecificPart())).toURI();
        } else {
            uri = inputURI;
        }
        return uri.toURL();
    }

}
