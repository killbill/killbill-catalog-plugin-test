# killbill-catalog-test

This repo is intended as a starting point to build a custom catalog plugin.


## Kill Bill compatibility

killbill-catalog-test
A test plugin to verify the behavior of the CatalogPluginApi.

The plugin will load a static json catalog (WeaponsHire.pretty.json) and serve that catalog through the CatalogPluginApi.

Minimal functionality has been implemented at this point.

Kill Bill compatibility


| Plugin version | Kill Bill version |
| -------------: | ----------------: |
| 0.2.y          | 0.18.z            |
| 0.3.y          | 0.20.z            |
| 0.4.y          | 0.22.z            |


## Requirements

There is no requirement aside from running Kill Bill.
Based on the configuration, the plugin may expect specific resources to be available or access to file system to load some catalog versions.

## Installation

Locally:

```
kpm install_java_plugin kb:catalog --from-source-file target/catalog-test-*-SNAPSHOT.jar  --destination /var/tmp/bundles
```

## Code Organization

The plugin depends on the `killbill-base-plugin` like most Kill Bill plugins. See this [repo](https://github.com/killbill/killbill-plugin-framework-java).
In particular, it leverages some of the POJOs (builders) available from this base plugin (`boilerplate/*Imp.java`).

Additional POJOs for the catalog have also been implemented on top of these base POJOs in order to allow for proper deserialization using JSON (`models/*Model`)
However, in order to deserialize XML input files, we have also imported the `killbill-catalog` jar.

TODO: Enhance existing models to also support XML deserialization and remove the `killbill-catalog` dependency.

# Use Cases

The plugin allows for a flexible per-tenant YAML based configuration to either:
* Specify an input XML-based resource file (`resources/Weapons.Hire.xml`) to be used as the catalog
* Specify a file-system based directory to provide the XML versions. This option also allow to serve per-account catalogs.


Assuming a proper `yaml` configuration, one can configure the plugin using:

```
curl -v \
-X POST \
-u admin:password \
-H 'X-Killbill-ApiKey: bob' \
-H 'X-Killbill-ApiSecret: lazar' \
-H 'X-Killbill-CreatedBy: admin' \
-H 'Content-Type: text/plain' \
-d@<path_to_yam_config> \
http://127.0.0.1:8080/1.0/kb/tenants/uploadPluginConfig/killbill-catalog-test
```

## Resource

If you just want to try out the plugin, the easiest is to use the existing `WeaponsHire.xml` resources being shipped in the plugin.

The YAML configuration would look like the following:

```
!!org.killbill.billing.plugin.catalog.CatalogYAMLConfiguration
  uri: WeaponsHire.xml
  validateAccount: false
  accountCatalog: false
```

## Filesystem Based Catalog

In order to serve a real XML-based catalog, the configuration would look like the following:

```
!!org.killbill.billing.plugin.catalog.CatalogYAMLConfiguration
  uri: file:///<some_catalog_directory>
  validateAccount: false
  accountCatalog: true
```

When using the `accountCatalog=true`, the directory structure should look like the following:

```
\ some_catalog_directory \
                           - \ <kb_account_id_1>
                                               - \ v1.xml
                                               - \ v2.xml
                                               - \ v3.xml
                                               - \ ...
                           - \ <kb_account_id_2>
                                 ...
```

The config `validateAccount` specifies whether plugin should check for the existence of the account prior serving the catalog.
