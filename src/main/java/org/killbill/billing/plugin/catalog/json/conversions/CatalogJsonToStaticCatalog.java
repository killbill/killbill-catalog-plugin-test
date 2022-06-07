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

package org.killbill.billing.plugin.catalog.json.conversions;

import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.*;
import org.killbill.billing.catalog.api.rules.*;
import org.killbill.billing.plugin.catalog.models.api.*;
import org.killbill.billing.plugin.catalog.json.formats.CatalogJson;


import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

public class CatalogJsonToStaticCatalog implements Function<CatalogJson, StaticCatalog> {
    private final boolean strict = true;

    private final BillingMode missingPlanRecurringBillingMode = BillingMode.IN_ARREAR;
    private final Date missingPlanEffectiveDateForExistingSubscriptions = null;
    private final int missingPlanPlansAllowedInBundle = 1;
    private final PriceList missingPlanPriceList = null;
    private final BillingPeriod missingPlanRecurrringBillingPeriod = BillingPeriod.NO_BILLING_PERIOD;
    private final BillingMode missingUsageBillingMode = BillingMode.IN_ARREAR;
    private final UsageType missingUsageType = UsageType.CONSUMABLE;
    private final String missingUsageName = null;
    private final String missingUsagePrettyName = null;
    private final InternationalPrice missingUsageFixedPrice = null;
    private final InternationalPrice missingUsageRecurringPrice = null;
    private final TierBlockPolicy missingTierBlockPolicy = TierBlockPolicy.ALL_TIERS;
    private final BlockType missingTieredBlockType = BlockType.TIERED;
    private final Double missingTieredBlockMinTopUpCredit = null;

    private final boolean optionPropagatePlanRecurringBillingPeriodToAllPlanPhases = true;
    private final boolean optionLookupPlanPriceListFromAllPriceLists = true;
    private final boolean optionSetInternationalPriceToNullIfPricesIsEmpty = false;
    private final boolean optionSetRecurringToNullIfRecurringPriceIsEmpty = false;
    private final boolean optionSetRecurringToNullIfNoBillingPeriod = true;
    private final boolean optionSetFixedToNullIfPriceIsEmpty = false;

    public CatalogJsonToStaticCatalog() {
    }

    @Override
    public StaticCatalog apply(final CatalogJson input) {
        return toStaticCatalog(input);
    }

    private StaticCatalog toStaticCatalog(final CatalogJson input) {

        StaticCatalog output = null;

        if (input != null) {
            List<Listing> availableBasePlanListings = Utils.<Listing>list();
            String catalogName = input.getName();
            Date effectiveDate = input.getEffectiveDate();
            PlanRules planRules = null;
            List<Plan> plans = Utils.<Plan>list();
            PriceListSet priceLists = null;
            List<Product> products = Utils.<Product>list();
            Currency[] supportedCurrencies = Utils.array(Currency.class, input.getCurrencies());
            Unit[] units = toUnits(input.getUnits());

            List<PriceList> allPriceLists = Utils.<PriceList>list();
            List<CaseChangePlanPolicy> caseChangePlanPolicy = Utils.<CaseChangePlanPolicy>list();
            List<CaseCancelPolicy> caseCancelPolicy = Utils.<CaseCancelPolicy>list();

            output = new StaticCatalogModel.Builder<>()
                    .withAvailableBasePlanListings(availableBasePlanListings)
                    .withCatalogName(catalogName)
                    .withEffectiveDate(effectiveDate)
                    .withPlanRules(planRules)
                    .withPlans(plans)
                    .withPriceLists(priceLists)
                    .withProducts(products)
                    .withSupportedCurrencies(supportedCurrencies)
                    .withUnits(units)
                    .build(deferredPriceListSet(allPriceLists),
                            deferredPlanRules(caseCancelPolicy, caseChangePlanPolicy));

            Map<String, Unit> unitMap = this.<Unit>map(Utils.<Unit>list(units));

            List<Map.Entry<CatalogJson.ProductJson, Product>> productEntries = mapProducts(output, input.getProducts());

            for (Map.Entry<CatalogJson.ProductJson, Product> entry : productEntries) {
                if (entry.getValue() != null) {
                    products.add(entry.getValue());
                }
            }
            Map<String, Product> productMap = this.<Product>map(products);

            List<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>> priceListEntries = mapPriceLists(output, input.getPriceLists());

            for (Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>> entry : priceListEntries) {
                if (entry.getValue().getKey() != null) {
                    allPriceLists.add(entry.getValue().getKey());
                }
            }
            List<Map.Entry<CatalogJson.PlanJson, Plan>> planEntries = mapPlans(output, unitMap, priceListEntries, productEntries);

            for (Map.Entry<CatalogJson.PlanJson, Plan> entry : planEntries) {
                if (entry.getValue() != null) {
                    plans.add(entry.getValue());
                }
            }
            Map<String, Plan> planMap = this.<Plan>map(plans);

            for (Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>> entry : priceListEntries) {
                if (entry.getValue().getKey() != null) {
                    if (entry.getKey().getPlans() != null) {
                        for (String plan : entry.getKey().getPlans()) {
                            if (plan != null) {
                                entry.getValue().getValue().add(lookupPlan(planMap, plan));
                            }
                        }
                    }
                }
            }
            caseCancelPolicy.add(toCaseCancelPolicy(output));
            caseChangePlanPolicy.add(toCaseChangePlanPolicy(output));
        }
        return output;
    }

    private List<Map.Entry<CatalogJson.PlanJson, Plan>> mapPlans(final StaticCatalog catalog,
                                                                 final Map<String, Unit> units,
                                                                 final List<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>> priceLists,
                                                                 final List<Map.Entry<CatalogJson.ProductJson, Product>> input) {
        List<Map.Entry<CatalogJson.PlanJson, Plan>> output = Utils.<Map.Entry<CatalogJson.PlanJson, Plan>>list();
        if (input != null) {
            for (Map.Entry<CatalogJson.ProductJson, Product> entry : input) {
                if (entry.getValue() != null) {
                    if (entry.getKey().getPlans() != null) {
                        for (CatalogJson.PlanJson plan : entry.getKey().getPlans()) {
                            PriceList priceList = optionLookupPlanPriceListFromAllPriceLists ? lookupPriceList(priceLists, plan.getName()) : missingPlanPriceList;
                            Plan result = (plan == null) ? null : toPlan(catalog, units, entry.getValue(), priceList, plan);
                            output.add(Utils.<CatalogJson.PlanJson, Plan>entry(plan, result));
                        }
                    }
                }
            }
        }
        return output;
    }

    private PriceList lookupPriceList(final List<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>> priceLists,
                                      final String plan) {
        if ((priceLists != null) && valid(plan)) {
            for (Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>> entry : priceLists) {
                if (entry.getValue().getKey() != null) {
                    if (entry.getKey().getPlans() != null) {
                        if (entry.getKey().getPlans().contains(plan)) {
                            return entry.getValue().getKey();
                        }
                    }
                }
            }
        }
        throw new RuntimeException("Nonexistent pricelist for plan");
    }

    private Plan toPlan(final StaticCatalog inCatalog, final Map<String, Unit> units,
                        final Product inProduct, final PriceList inPriceList, final CatalogJson.PlanJson input) {
        Plan output = null;
        if (input != null) {
            StaticCatalog catalog = inCatalog;
            Date effectiveDateForExistingSubscriptions = missingPlanEffectiveDateForExistingSubscriptions;
            String name = input.getName();
            int plansAllowedInBundle = missingPlanPlansAllowedInBundle;
            String prettyName = (input.getPrettyName() == null) ? input.getName() : input.getPrettyName();
            BillingMode recurringBillingMode = missingPlanRecurringBillingMode;
            BillingPeriod recurringBillingPeriod = input.getBillingPeriod();
            Product product = inProduct;
            PriceList priceList = inPriceList;

            PlanPhase[] allPhases = toPlanPhases(catalog, units, input.getPhases(), name,
                    optionPropagatePlanRecurringBillingPeriodToAllPlanPhases
                            ? recurringBillingPeriod : missingPlanRecurrringBillingPeriod);
            PlanPhase finalPhase = toFinalPhase(allPhases);
            PlanPhase[] initialPhases = toInitialPhases(allPhases);
            Iterator<PlanPhase> initialPhaseIterator = Utils.<PlanPhase>list(initialPhases).iterator();

            output = new PlanModel.Builder<>()
                    .withAllPhases(allPhases)
                    .withCatalog(catalog)
                    .withEffectiveDateForExistingSubscriptions(effectiveDateForExistingSubscriptions)
                    .withFinalPhase(finalPhase)
                    .withInitialPhaseIterator(initialPhaseIterator)
                    .withInitialPhases(initialPhases)
                    .withName(name)
                    .withPlansAllowedInBundle(plansAllowedInBundle)
                    .withPrettyName(prettyName)
                    .withPriceList(priceList)
                    .withProduct(product)
                    .withRecurringBillingMode(recurringBillingMode)
                    .withRecurringBillingPeriod(recurringBillingPeriod)
                    .build();
        }
        return output;
    }

    private PlanPhase toFinalPhase(final PlanPhase[] allPhases) {
        if ((allPhases != null) && (allPhases.length > 0)) {
            return allPhases[allPhases.length - 1];
        }
        return null;
    }

    private PlanPhase[] toInitialPhases(final PlanPhase[] allPhases) {
        if ((allPhases == null) || (allPhases.length < 2)) {
            return Utils.<PlanPhase>array(PlanPhase.class);
        } else {
            PlanPhase[] output = Utils.<PlanPhase>array(PlanPhase.class, allPhases.length - 1);
            for (int i = 0; i < output.length; i++) {
                output[i] = allPhases[i];
            }
            return output;
        }
    }

    private PlanPhase[] toPlanPhases(final StaticCatalog catalog, final Map<String, Unit> units,
                                     final List<CatalogJson.PhaseJson> input, final String plan, final BillingPeriod recurringBillingPeriod) {
        List<PlanPhase> output = Utils.<PlanPhase>list();
        if (input != null) {
            for (CatalogJson.PhaseJson element : input) {
                output.add(toPlanPhase(catalog, units, element, plan, recurringBillingPeriod));
            }
        }
        return Utils.<PlanPhase>array(PlanPhase.class, output, true);
    }

    private PlanPhase toPlanPhase(final StaticCatalog inCatalog, final Map<String, Unit> units,
                                  final CatalogJson.PhaseJson input, final String plan, final BillingPeriod recurringBillingPeriod) {
        PlanPhase output = null;
        if (input != null) {
            StaticCatalog catalog = inCatalog;
            Duration duration = toDuration(input.getDuration());
            PhaseType phaseType = PhaseType.valueOf(input.getType());
            Fixed fixed = toFixed(input.getFixedPrices());
            String name = phaseName(plan, phaseType);
            String prettyName = name;
            Recurring recurring = toRecurring(input.getPrices(), recurringBillingPeriod);
            Usage[] usages = toUsages(catalog, units, input.getUsages());
            output = new PlanPhaseModel.Builder<>()
                    .withCatalog(catalog)
                    .withDuration(duration)
                    .withFixed(fixed)
                    .withName(name)
                    .withPhaseType(phaseType)
                    .withPrettyName(prettyName)
                    .withRecurring(recurring)
                    .withUsages(usages)
                    .build();
        }
        return output;
    }

    public static String phaseName(final String planName, final PhaseType phasetype) {
        return planName + "-" + phasetype.toString().toLowerCase();
    }

    private Recurring toRecurring(final List<CatalogJson.PriceJson> input, final BillingPeriod billingPeriod) {
        Recurring output = null;
        if (input != null) {
            InternationalPrice recurringPrice = toInternationalPrice(input);
            if ((!optionSetRecurringToNullIfNoBillingPeriod ||
                    ((billingPeriod != null) && (billingPeriod != BillingPeriod.NO_BILLING_PERIOD)))
                /*&& (!optionSetRecurringToNullIfRecurringPriceIsEmpty || !empty(recurringPrice))*/)
                output = new RecurringModel.Builder<>()
                        .withBillingPeriod(billingPeriod)
                        .withRecurringPrice(recurringPrice)
                        .build();

        }
        return output;
    }

    private Fixed toFixed(final List<CatalogJson.PriceJson> input) {
        Fixed output = null;
        if (input != null) {
            InternationalPrice price = toInternationalPrice(input);
            FixedType type = FixedType.ONE_TIME;
            if (!optionSetFixedToNullIfPriceIsEmpty || !empty(price)) {
                output = new FixedModel.Builder<>()
                        .withPrice(price)
                        .withType(type)
                        .build();
            }
        }
        return output;
    }

    private Usage[] toUsages(final StaticCatalog catalog, final Map<String, Unit> units, final List<CatalogJson.UsageJson> input) {
        List<Usage> output = Utils.<Usage>list();
        if (input != null) {
            for (CatalogJson.UsageJson element : input) {
                output.add(toUsage(catalog, units, element));
            }
        }
        return Utils.<Usage>array(Usage.class, output);
    }

    private Usage toUsage(final StaticCatalog inCatalog, final Map<String, Unit> units, final CatalogJson.UsageJson input) {
        Usage output = null;
        if (input != null) {
            BillingMode billingMode = missingUsageBillingMode;
            BillingPeriod billingPeriod = BillingPeriod.valueOf(input.getBillingPeriod());
            Block[] blocks = Utils.<Block>array(Block.class);
            StaticCatalog catalog = inCatalog;
            InternationalPrice fixedPrice = missingUsageFixedPrice;
            Limit[] limits = Utils.<Limit>array(Limit.class);
            String name = missingUsageName;
            String prettyName = missingUsagePrettyName;
            InternationalPrice recurringPrice = missingUsageRecurringPrice;
            TierBlockPolicy tierBlockPolicy = missingTierBlockPolicy;
            Tier[] tiers = toTiers(units, input.getTiers());
            UsageType usageType = missingUsageType;
            output = new UsageModel.Builder<>()
                    .withBillingMode(billingMode)
                    .withBillingPeriod(billingPeriod)
                    .withBlocks(blocks)
                    .withCatalog(catalog)
                    .withFixedPrice(fixedPrice)
                    .withLimits(limits)
                    .withName(name)
                    .withPrettyName(prettyName)
                    .withRecurringPrice(recurringPrice)
                    .withTierBlockPolicy(tierBlockPolicy)
                    .withTiers(tiers)
                    .withUsageType(usageType)
                    .build();
        }
        return output;
    }

    private Tier[] toTiers(final Map<String, Unit> units, final List<CatalogJson.TierJson> input) {
        List<Tier> output = Utils.<Tier>list();
        if (input != null) {
            for (CatalogJson.TierJson element : input) {
                output.add(toTier(units, element));
            }
        }
        return Utils.<Tier>array(Tier.class, output, true);
    }

    private Tier toTier(final Map<String, Unit> units, final CatalogJson.TierJson input) {
        Tier output = null;

        if (input != null) {
            InternationalPrice fixedPrice = toInternationalPrice(input.getFixedPrice());
            Limit[] limits = toLimits(units, input.getLimits());
            InternationalPrice recurringPrice = toInternationalPrice(input.getRecurringPrice());
            TieredBlock[] tieredBlocks = toTieredBlocks(units, input.getBlocks());
            output = new TierModel.Builder<>()
                    .withFixedPrice(fixedPrice)
                    .withLimits(limits)
                    .withRecurringPrice(recurringPrice)
                    .withTieredBlocks(tieredBlocks)
                    .build();
        }
        return output;
    }

    private Limit[] toLimits(final Map<String, Unit> units, final List<CatalogJson.LimitJson> input) {
        List<Limit> output = Utils.<Limit>list();
        if (input != null) {
            for (CatalogJson.LimitJson element : input) {
                output.add(toLimit(units, element));
            }
        }
        return Utils.<Limit>array(Limit.class, output, true);
    }

    private Limit toLimit(final Map<String, Unit> units, final CatalogJson.LimitJson input) {
        Limit output = null;
        if (input != null) {
            Double max = Utils.toDouble(input.getMax());
            Double min = Utils.toDouble(input.getMin());
            Unit unit = lookupUnit(units, input.getUnit());
            output = new LimitModel.Builder<>()
                    .withMax(max)
                    .withMin(min)
                    .withUnit(unit)
                    .build();
        }
        return output;
    }

    private TieredBlock[] toTieredBlocks(final Map<String, Unit> units, final List<CatalogJson.TieredBlockJson> input) {
        List<TieredBlock> output = Utils.<TieredBlock>list();
        if (input != null) {
            for (CatalogJson.TieredBlockJson element : input) {
                output.add(toTieredBlock(units, element));
            }
        }
        return Utils.<TieredBlock>array(TieredBlock.class, output, true);
    }

    private TieredBlock toTieredBlock(final Map<String, Unit> units, final CatalogJson.TieredBlockJson input) {
        TieredBlock output = null;
        if (input != null) {
            Double max = Utils.toDouble(input.getMax());
            Double minTopUpCredit = missingTieredBlockMinTopUpCredit;
            InternationalPrice price = toInternationalPrice(input.getPrices());
            Double size = Utils.toDouble(input.getSize());
            BlockType type = missingTieredBlockType;
            Unit unit = lookupUnit(units, input.getUnit());
            output = new TieredBlockModel.Builder<>()
                    .withMax(max)
                    .withMinTopUpCredit(minTopUpCredit)
                    .withPrice(price)
                    .withSize(size)
                    .withType(type)
                    .withUnit(unit)
                    .build();
        }
        return output;
    }

    private boolean empty(final InternationalPrice input) {
        return (input == null) || (input.getPrices() == null) || (input.getPrices().length == 0);
    }

    private InternationalPrice toInternationalPrice(final List<CatalogJson.PriceJson> input) {
        InternationalPrice output = null;
        if (input != null) {
            Price[] prices = toPrices(input);
            boolean isZero = isZero(prices);
            if (!optionSetInternationalPriceToNullIfPricesIsEmpty || (prices.length > 0))
                output = new InternationalPriceModel.Builder<>()
                        .withPrices(prices)
                        .withIsZero(isZero)
                        .build();
        }
        return output;
    }

    private boolean isZero(final Price[] prices) {
        for (final Price price : prices) {
            try {
                if (price.getValue().compareTo(BigDecimal.ZERO) != 0) {
                    return false;
                }
            } catch (CurrencyValueNull e) {
            }
        }
        return true;
    }

    private Price[] toPrices(final List<CatalogJson.PriceJson> input) {
        List<Price> output = Utils.<Price>list();
        if (input != null) {
            for (CatalogJson.PriceJson element : input) {
                output.add(toPrice(element));
            }
        }
        return Utils.<Price>array(Price.class, output, true);
    }

    private Price toPrice(final CatalogJson.PriceJson input) {
        Price output = null;
        if (input != null) {
            Currency currency = input.getCurrency();
            BigDecimal value = input.getValue();
            output = new PriceModel.Builder<>()
                    .withCurrency(currency)
                    .withValue(value)
                    .build();
        }
        return output;
    }

    private Duration toDuration(final CatalogJson.DurationJson input) {
        Duration output = null;
        if (input != null) {
            int number = input.getNumber();
            TimeUnit unit = input.getUnit();
            output = new DurationModel.Builder<>()
                    .withNumber(number)
                    .withUnit(unit)
                    .build();
        }
        return output;
    }

    private List<Map.Entry<CatalogJson.ProductJson, Product>> mapProducts(final StaticCatalog catalog,
                                                                          final List<CatalogJson.ProductJson> input) {
        List<Map.Entry<CatalogJson.ProductJson, Product>> output = Utils.<Map.Entry<CatalogJson.ProductJson, Product>>list();

        if (input != null) {
            List<List<Product>> available = Utils.<List<Product>>list();
            List<List<Product>> included = Utils.<List<Product>>list();
            List<Product> products = Utils.<Product>list();

            for (CatalogJson.ProductJson element : input) {
                available.add(Utils.<Product>list());
                included.add(Utils.<Product>list());
                Product product = toProduct(catalog, element, available.get(available.size() - 1),
                        included.get(included.size() - 1));
                output.add(Utils.<CatalogJson.ProductJson, Product>entry(element, product));
                if (product != null) {
                    products.add(product);
                }
            }
            Map<String, Product> map = this.<Product>map(products);

            for (int i = 0; i < input.size(); i++) {
                CatalogJson.ProductJson product = input.get(i);
                if (product != null) {
                    for (String name : product.getAvailable()) {
                        available.get(i).add(lookupProduct(map, name));
                    }
                    for (String name : product.getIncluded()) {
                        included.get(i).add(lookupProduct(map, name));
                    }
                }
            }
        }
        return output;
    }

    private Product toProduct(final StaticCatalog inCatalog, final CatalogJson.ProductJson input,
                              final Collection<Product> inAvailable, final Collection<Product> inIncluded) {
        Product output = null;
        if (input != null) {
            Collection<Product> available = inAvailable;
            StaticCatalog catalog = inCatalog;
            String catalogName = (catalog == null) ? null : catalog.getCatalogName();
            ProductCategory category = ProductCategory.valueOf(input.getType());
            Collection<Product> included = inIncluded;
            Limit[] limits = Utils.<Limit>array(Limit.class);
            String name = input.getName();
            String prettyName = input.getPrettyName() == null ? input.getName() : input.getPrettyName();
            output = new ProductModel.Builder<>()
                    .withAvailable(available)
                    .withCatalog(catalog)
                    .withCatalogName(catalogName)
                    .withCategory(category)
                    .withIncluded(included)
                    .withLimits(limits)
                    .withName(name)
                    .withPrettyName(prettyName)
                    .build();
        }
        return output;
    }

    private List<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>> mapPriceLists(final StaticCatalog catalog,
                                                                                                       final List<CatalogJson.PriceListJson> input) {
        List<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>> output
                = Utils.<Map.Entry<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>>list();

        if (input != null) {
            for (CatalogJson.PriceListJson element : input) {
                List<Plan> plans = Utils.<Plan>list();
                PriceList priceList = toPriceList(catalog, element, plans);
                Map.Entry<PriceList, List<Plan>> value = Utils.<PriceList, List<Plan>>entry(priceList, plans);
                output.add(Utils.<CatalogJson.PriceListJson, Map.Entry<PriceList, List<Plan>>>entry(element, value));
            }
        }
        return output;
    }

    private PriceList toPriceList(final StaticCatalog inCatalog, final CatalogJson.PriceListJson input, final Collection<Plan> inPlans) {
        PriceList output = null;
        if (input != null) {
            StaticCatalog catalog = inCatalog;
            String name = input.getName();
            Collection<Plan> plans = inPlans;
            String prettyName = input.getName();
            output = new PriceListModel.Builder<>()
                    .withCatalog(catalog)
                    .withName(name)
                    .withPlans(plans)
                    .withPrettyName(prettyName)
                    .build();
        }
        return output;
    }

    private Unit[] toUnits(final List<CatalogJson.UnitJson> input) {
        List<Unit> output = Utils.<Unit>list();
        if (input != null) {
            for (CatalogJson.UnitJson element : input) {
                output.add(toUnit(element));
            }
        }
        return Utils.<Unit>array(Unit.class, output, true);
    }

    private Unit toUnit(final CatalogJson.UnitJson input) {
        Unit output = null;
        if ((input != null)) {
            String name = input.getName();
            String prettyName = input.getPrettyName() == null ? input.getName() : input.getPrettyName();
            output = new UnitModel.Builder<>()
                    .withName(name)
                    .withPrettyName(prettyName)
                    .build();
        }
        return output;
    }

    private <T extends CatalogEntity> Map<String, T> map(final Iterable<T> input) {
        Map<String, T> output = Utils.<String, T>map();
        if (input != null) {
            for (T element : input) {
                if ((element != null) && valid(element.getName())) {
                    if (!output.containsKey(element.getName())) {
                        output.put(element.getName(), element);
                    } else {
                        throw new RuntimeException("Duplicate key");
                    }
                }
            }
        }
        return output;
    }

    private Plan lookupPlan(final Map<String, Plan> map, final String name) {
        Plan output = map.get(name);
        if (output == null) {
            throw new RuntimeException("Nonexistent plan name");
        }
        return output;
    }

    private Product lookupProduct(final Map<String, Product> map, final String name) {
        Product output = map.get(name);
        if (output == null) {
            throw new RuntimeException("Nonexistent product name");
        }
        return output;
    }

    private Unit lookupUnit(final Map<String, Unit> map, final String name) {
        Unit output = null;
        if (map.containsKey(name)) {
            output = map.get(name);
        } else if (strict) {
            throw new RuntimeException("Nonexistent unit name");
        } else if (valid(name)) {
            output = new UnitModel.Builder<>()
                    .withName(name)
                    .withPrettyName(name)
                    .build();
        }
        return output;
    }

    private PriceListSetModel.Builder<?> deferredPriceListSet(final List<PriceList> inAllPriceLists) {
        StaticCatalog catalog = null;
        List<PriceList> allPriceLists = inAllPriceLists;
        return new PriceListSetModel.Builder<>()
                .withAllPriceLists(allPriceLists)
                .withCatalog(catalog);
    }

    private PlanRulesModel.Builder<?> deferredPlanRules(List<CaseCancelPolicy> inCaseCancelPolicy,
                                                        List<CaseChangePlanPolicy> inCaseChangePlanPolicy) {
        Iterable<CaseBillingAlignment> caseBillingAlignment = Utils.<CaseBillingAlignment>list();
        Iterable<CaseCancelPolicy> caseCancelPolicy = inCaseCancelPolicy;
        Iterable<CaseChangePlanAlignment> caseChangePlanAlignment = Utils.<CaseChangePlanAlignment>list();
        Iterable<CaseChangePlanPolicy> caseChangePlanPolicy = inCaseChangePlanPolicy;
        Iterable<CaseCreateAlignment> caseCreateAlignment = Utils.<CaseCreateAlignment>list();
        Iterable<CasePriceList> casePriceList = Utils.<CasePriceList>list();
        StaticCatalog catalog = null;
        return new PlanRulesModel.Builder<>()
                .withCaseBillingAlignment(caseBillingAlignment)
                .withCaseCancelPolicy(caseCancelPolicy)
                .withCaseChangePlanAlignment(caseChangePlanAlignment)
                .withCaseChangePlanPolicy(caseChangePlanPolicy)
                .withCaseCreateAlignment(caseCreateAlignment)
                .withCasePriceList(casePriceList)
                .withCatalog(catalog);
    }

    private CaseBillingAlignment toCaseBillingAlignment() {
        BillingAlignment billingAlignment = BillingAlignment.ACCOUNT;
        BillingPeriod billingPeriod = null;
        StaticCatalog catalog = null;
        PhaseType phaseType = null;
        PriceList priceList = null;
        Product product = null;
        ProductCategory productCategory = null;
        return new CaseBillingAlignmentModel.Builder<>()
                .withBillingAlignment(billingAlignment)
                .withBillingPeriod(billingPeriod)
                .withCatalog(catalog)
                .withPhaseType(phaseType)
                .withPriceList(priceList)
                .withProduct(product)
                .withProductCategory(productCategory)
                .build();
    }

    private CaseCancelPolicy toCaseCancelPolicy(StaticCatalog inCatalog) {
        BillingActionPolicy billingActionPolicy = BillingActionPolicy.END_OF_TERM;
        BillingPeriod billingPeriod = null;
        StaticCatalog catalog = inCatalog;
        PhaseType phaseType = null;
        PriceList priceList = null;
        Product product = null;
        ProductCategory productCategory = null;
        return new CaseCancelPolicyModel.Builder<>()
                .withBillingActionPolicy(billingActionPolicy)
                .withBillingPeriod(billingPeriod)
                .withCatalog(catalog)
                .withPhaseType(phaseType)
                .withPriceList(priceList)
                .withProduct(product)
                .withProductCategory(productCategory)
                .build();
    }

    private CaseChangePlanAlignment toCaseChangePlanAlignment() {
        PlanAlignmentChange alignment = PlanAlignmentChange.START_OF_BUNDLE;
        StaticCatalog catalog = null;
        BillingPeriod fromBillingPeriod = null;
        PriceList fromPriceList = null;
        Product fromProduct = null;
        ProductCategory fromProductCategory = null;
        PhaseType phaseType = null;
        BillingPeriod toBillingPeriod = null;
        PriceList toPriceList = null;
        Product toProduct = null;
        ProductCategory toProductCategory = null;
        return new CaseChangePlanAlignmentModel.Builder<>()
                .withAlignment(alignment)
                .withCatalog(catalog)
                .withFromBillingPeriod(fromBillingPeriod)
                .withFromPriceList(fromPriceList)
                .withFromProduct(fromProduct)
                .withFromProductCategory(fromProductCategory)
                .withPhaseType(phaseType)
                .withToBillingPeriod(toBillingPeriod)
                .withToPriceList(toPriceList)
                .withToProduct(toProduct)
                .withToProductCategory(toProductCategory)
                .build();
    }

    private CaseChangePlanPolicy toCaseChangePlanPolicy(StaticCatalog inCatalog) {
        BillingActionPolicy billingActionPolicy = BillingActionPolicy.END_OF_TERM;
        StaticCatalog catalog = inCatalog;
        BillingPeriod fromBillingPeriod = null;
        PriceList fromPriceList = null;
        Product fromProduct = null;
        ProductCategory fromProductCategory = null;
        PhaseType phaseType = null;
        BillingPeriod toBillingPeriod = null;
        PriceList toPriceList = null;
        Product toProduct = null;
        ProductCategory toProductCategory = null;
        return new CaseChangePlanPolicyModel.Builder<>()
                .withBillingActionPolicy(billingActionPolicy)
                .withCatalog(catalog)
                .withFromBillingPeriod(fromBillingPeriod)
                .withFromPriceList(fromPriceList)
                .withFromProduct(fromProduct)
                .withFromProductCategory(fromProductCategory)
                .withPhaseType(phaseType)
                .withToBillingPeriod(toBillingPeriod)
                .withToPriceList(toPriceList)
                .withToProduct(toProduct)
                .withToProductCategory(toProductCategory)
                .build();
    }

    private CaseCreateAlignment toCaseCreateAlignment() {
        BillingPeriod billingPeriod = null;
        StaticCatalog catalog = null;
        PlanAlignmentCreate planAlignmentCreate = PlanAlignmentCreate.START_OF_BUNDLE;
        PriceList priceList = null;
        Product product = null;
        ProductCategory productCategory = null;
        return new CaseCreateAlignmentModel.Builder<>()
                .withBillingPeriod(billingPeriod)
                .withCatalog(catalog)
                .withPlanAlignmentCreate(planAlignmentCreate)
                .withPriceList(priceList)
                .withProduct(product)
                .withProductCategory(productCategory)
                .build();
    }

    private CasePriceList toCasePriceList() {
        BillingPeriod billingPeriod = null;
        StaticCatalog catalog = null;
        PriceList destinationPriceList = null;
        PriceList priceList = null;
        Product product = null;
        ProductCategory productCategory = null;
        return new CasePriceListModel.Builder<>()
                .withBillingPeriod(billingPeriod)
                .withCatalog(catalog)
                .withDestinationPriceList(destinationPriceList)
                .withProduct(product)
                .withProductCategory(productCategory)
                .build();
    }

    private boolean valid(final String s) {
        if ((s != null) && (s.length() > 0)) {
            return true;
        } else {
            throw new RuntimeException("Bad key");
        }
    }
}
