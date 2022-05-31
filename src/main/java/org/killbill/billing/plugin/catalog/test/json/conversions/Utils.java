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

package org.killbill.billing.plugin.catalog.test.json.conversions;

import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

final class Utils {

    private Utils() {
    }

    private static class Compose<U, V, R> implements Function<U, R> {
        private final Function<U, V> inner;
        private final Function<V, R> outer;

        public Compose(Function<U, V> inner, Function<V, R> outer) {
            this.inner = inner;
            this.outer = outer;
        }

        public R apply(U input) {
            return this.outer.apply(this.inner.apply(input));
        }
    }

    public <U, V, R> Function<U, R> compose(Function<U, V> inner, Function<V, R> outer) {
        return new Compose<U, V, R>(inner, outer);
    }

    public static <T> List<T> list() {
        return new ArrayList<T>();
    }

    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<K, V>(key, value);
    }

    public static <K, V> Map<K, V> map() {
        return new HashMap<K, V>();
    }

    public static <K, V> Map<K, V> map(List<K> keys, List<V> values) {
        Map<K, V> output = Utils.<K, V>map();
        if (keys != null && values != null) {
            int size = keys.size() < values.size() ? keys.size() : values.size();
            for (int i = 0; i < size; i++) {
                output.put(keys.get(i), values.get(i));
            }
        }
        return output;
    }

    public static <T> List<T> list(Iterator<T> input) {
        return Utils.<T>list(input, false);
    }

    public static <T> List<T> list(Iterator<T> input, boolean strip) {
        List<T> output = Utils.<T>list();
        if (input != null) {
            while (input.hasNext()) {
                T element = input.next();
                if (!strip || (element != null)) {
                    output.add(element);
                }
            }
        }
        return output;
    }

    public static <T> List<T> list(Iterable<T> input) {
        return Utils.<T>list(input, false);
    }

    public static <T> List<T> list(Iterable<T> input, boolean strip) {
        List<T> output = Utils.<T>list();
        if (input != null) {
            for (T element : input) {
                if (!strip || (element != null)) {
                    output.add(element);
                }
            }
        }
        return output;
    }

    public static <T> List<T> list(T[] input) {
        return Utils.<T>list(input, false);
    }

    public static <T> List<T> list(T[] input, boolean strip) {
        List<T> output = Utils.<T>list();
        if (input != null) {
            for (T element : input) {
                if (!strip || (element != null)) {
                    output.add(element);
                }
            }
        }
        return output;
    }

    public static <T, R> List<R> transform(Iterable<T> input, Function<T, R> function) {
        List<R> output = null;
        if (input != null) {
            output = new ArrayList<R>();
            for (T arg : input) {
                R result = function.apply(arg);
                output.add(result);
            }
        }
        return output;
    }

    public static <T> List<T> filter(Iterable<T> input, Predicate<T> predicate) {
        List<T> output = Utils.<T>list();
        if (input != null && predicate != null) {
            for (T element : input) {
                if (predicate.test(element)) {
                    output.add(element);
                }
            }
        }
        return output;
    }

    public static <T> T[] array(Class<T> cl, Iterable<T> input) {
        return Utils.<T>array(cl, input, false);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(Class<T> cl, Iterable<T> input, boolean strip) {
        List<T> ls = Utils.<T>list(input, strip);
        T[] output = Utils.<T>array(cl, ls.size());
        for (int i = 0; i < ls.size(); i++) {
            output[i] = ls.get(i);
        }
        return output;
    }

    public static <T> T[] array(Class<T> cl) {
        return Utils.array(cl, 0);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] array(Class<T> cl, int capacity) {
        return (T[]) Array.newInstance(cl, capacity);
    }

    public static DateTime toDateTime(Date input) {
        DateTime output = null;
        if (input != null) {
            output = new DateTime(input);
        }
        return output;
    }

    public static Double toDouble(String s) {
        return Double.valueOf(s);
    }
}
