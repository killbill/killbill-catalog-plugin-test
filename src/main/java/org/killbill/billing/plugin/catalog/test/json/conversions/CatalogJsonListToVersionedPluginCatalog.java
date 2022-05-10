package org.killbill.billing.plugin.catalog.test.json.conversions;

import org.killbill.billing.catalog.api.StaticCatalog;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.plugin.catalog.test.json.formats.CatalogJson;
import org.killbill.billing.plugin.catalog.test.models.plugin.VersionedPluginCatalogModel;

import java.util.List;
import java.util.function.Function;

public class CatalogJsonListToVersionedPluginCatalog implements Function<List<CatalogJson>, VersionedPluginCatalog> {

    private final CatalogJsonToStaticCatalog staticCatalogConversion;
    private final StaticCatalogToStandalonePluginCatalog standalonePluginCatalogConversion;

    public CatalogJsonListToVersionedPluginCatalog() {
        this.staticCatalogConversion = new CatalogJsonToStaticCatalog();
        this.standalonePluginCatalogConversion = new StaticCatalogToStandalonePluginCatalog();
    }

    @Override
    public VersionedPluginCatalog apply(List<CatalogJson> input) {
        return toVersionedPluginCatalog(input);
    }

    private VersionedPluginCatalog toVersionedPluginCatalog(List<CatalogJson> jsonCatalogs) {
        VersionedPluginCatalog output = null;
        if (jsonCatalogs != null) {
            List<StaticCatalog> staticCatalogs = Utils.<StaticCatalog>list();
            List<StandalonePluginCatalog> standalonePluginCatalogs = Utils.<StandalonePluginCatalog>list();
            for (CatalogJson jsonCatalog : jsonCatalogs) {
                if (jsonCatalog != null) {
                    StaticCatalog staticCatalog = this.staticCatalogConversion.apply(jsonCatalog);
                    staticCatalogs.add(staticCatalog);
                    StandalonePluginCatalog standalonePluginCatalog = this.standalonePluginCatalogConversion.apply(staticCatalog);
                    standalonePluginCatalogs.add(standalonePluginCatalog);
                }
            }
            if (standalonePluginCatalogs.size() > 0) {
                output = new VersionedPluginCatalogModel.Builder<>()
                        .withCatalogName(staticCatalogs.get(0).getCatalogName())
                        .withStandalonePluginCatalogs(standalonePluginCatalogs)
                        .build();
            }
        }
        return output;
    }
}
