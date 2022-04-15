package org.killbill.billing.plugin.catalog.test.json; 

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;

import org.killbill.billing.plugin.catalog.test.json.formats.CatalogJson;

import org.killbill.billing.plugin.catalog.test.json.conversions.CatalogJsonListToVersionedPluginCatalog;

import org.killbill.billing.plugin.catalog.test.models.plugin.VersionedPluginCatalogModel;

public class VersionedPluginCatalogJsonDeserializer extends StdDeserializer<VersionedPluginCatalog> {

    private final TypeReference formatType;
    private final CatalogJsonListToVersionedPluginCatalog versionedPluginCatalogConversion;

    public VersionedPluginCatalogJsonDeserializer() { 
        this(null); 
    }
    private VersionedPluginCatalogJsonDeserializer(Class<?> vc) { 
        super(vc); 
        this.formatType = new TypeReference<List<CatalogJson>>(){};
        this.versionedPluginCatalogConversion = new CatalogJsonListToVersionedPluginCatalog() ;
    }
    @Override
    public VersionedPluginCatalog deserialize(JsonParser jp, DeserializationContext ctx) 
        throws IOException, JsonProcessingException 
    { 
        VersionedPluginCatalog output = null;
        List<CatalogJson> jsonCatalogs = jp.readValueAs(this.formatType);
        if(jsonCatalogs != null) {
            output = this.versionedPluginCatalogConversion.apply(jsonCatalogs);
        }
        return output;
    }
}
