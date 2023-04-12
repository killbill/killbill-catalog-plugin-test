# killbill-catalog-test
![Maven Central](https://img.shields.io/maven-central/v/org.kill-bill.billing.plugin.java.catalog/catalog-test-plugin?color=blue&label=Maven%20Central)

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
| 0.5.y          | 0.24.z            |


## Requirements

There is no requirement aside from running Kill Bill.Based on the configuration, the plugin may expect specific resources to be available or access to file system to load some catalog versions.

## Build

```
mvn clean install -DskipTests
```

## Installation

Locally:

```
kpm install_java_plugin kb:catalog --from-source-file target/catalog-test-*-SNAPSHOT.jar  --destination /var/tmp/bundles
```

## Use Cases

The main use case for a catalog plugin is to interact with your catalog service existing outside of Kill Bill and map the results into a format compatible with the Kill Bill abstraction by implementing the CatalogPluginApi.

The plugin allows for a flexible per-tenant YAML based configuration to achieve the below :
* Specify an input XML-based resource file (`resources/Weapons.Hire.xml`) to be used as the catalog.
* Specify a file-system based directory to provide the XML versions. This option also allows to serve per-account catalogs.

## Configuration Instructions

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

* If you are looking to specify an input XML-based resource file to be used as the catalog, then the YAML configuration would look like :

```
!!org.killbill.billing.plugin.catalog.CatalogYAMLConfiguration
  uri: WeaponsHire.xml
  validateAccount: false
  accountCatalog: false
```

* If you are looking to specify file-system based directory to provide the XML versions which can be used at serve account-level catalogs, then the YAML configuration would look like :

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

## Testing

* Build and Install the plugin as mentioned in the above instructions.
* If you want to test the existing catalog service,then the easiest way is to specify the existing `WeaponsHire.xml` as described in the above configuration section. The same will be visible in KAUI as well as through API.
* If you want to test the file system based per-account level catalog, then kindly specify the catalog for the account using the directory structure explained in the above configuration section. The same will be useable for any subscription actions on the account. 

