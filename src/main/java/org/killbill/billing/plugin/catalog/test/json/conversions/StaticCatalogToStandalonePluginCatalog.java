package org.killbill.billing.plugin.catalog.test.json.conversions;

import org.joda.time.DateTime;
import org.killbill.billing.catalog.api.*;
import org.killbill.billing.catalog.api.rules.PlanRules;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.plugin.catalog.test.models.plugin.StandalonePluginCatalogModel;

import java.util.List;
import java.util.function.Function;

;

public class StaticCatalogToStandalonePluginCatalog implements Function<StaticCatalog, StandalonePluginCatalog> {
    public StaticCatalogToStandalonePluginCatalog() {
    }

    @Override
    public StandalonePluginCatalog apply(StaticCatalog input) {
        return toStandalonePluginCatalog(input);
    }

    private StandalonePluginCatalog toStandalonePluginCatalog(StaticCatalog input) {
        StandalonePluginCatalog output = null;
        if (input != null) {
            try {
                Iterable<PriceList> childrenPriceList = toChildrenPriceList(input.getPriceLists());
                Iterable<Currency> currencies = Utils.<Currency>list(input.getSupportedCurrencies());
                PriceList defaultPriceList = toDefaultPriceList(input.getPriceLists());
                DateTime effectiveDate = Utils.toDateTime(input.getEffectiveDate());
                PlanRules planRules = input.getPlanRules();
                Iterable<Plan> plans = Utils.<Plan>list(input.getPlans());
                Iterable<Product> products = Utils.<Product>list(input.getProducts());
                Iterable<Unit> units = Utils.<Unit>list(input.getUnits());
                output = new StandalonePluginCatalogModel.Builder<>()
                        .withChildrenPriceList(childrenPriceList)
                        .withCurrencies(currencies)
                        .withDefaultPriceList(defaultPriceList)
                        .withEffectiveDate(effectiveDate)
                        .withPlanRules(planRules)
                        .withPlans(plans)
                        .withProducts(products)
                        .withUnits(units)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return output;
    }

    private List<PriceList> toChildrenPriceList(PriceListSet priceLists) {
        List<PriceList> output = Utils.<PriceList>list();
        if ((priceLists != null) && (priceLists.getAllPriceLists() != null)
                && (priceLists.getAllPriceLists().size() > 1)) {
            for (int i = 1; i < priceLists.getAllPriceLists().size(); i++) {
                output.add(priceLists.getAllPriceLists().get(i));
            }
        }
        return output;
    }

    private PriceList toDefaultPriceList(PriceListSet priceLists) {
        PriceList output = null;
        if ((priceLists != null) && (priceLists.getAllPriceLists() != null)
                && (priceLists.getAllPriceLists().size() > 0)) {
            output = priceLists.getAllPriceLists().get(0);
        }
        return output;
    }
}
