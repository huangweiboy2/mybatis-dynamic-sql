/*
 *    Copyright 2016-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.dynamic.sql.where.condition;

import static org.mybatis.dynamic.sql.util.StringUtilities.spaceAfter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mybatis.dynamic.sql.AbstractListValueCondition;
import org.mybatis.dynamic.sql.Callback;

public class IsInWhenPresent<T> extends AbstractListValueCondition<T, IsInWhenPresent<T>> {

    protected IsInWhenPresent(Builder<T> builder) {
        super(builder);
    }

    @Override
    public String renderCondition(String columnName, Stream<String> placeholders) {
        return spaceAfter(columnName)
                + placeholders.collect(Collectors.joining(",", "in (", ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    public IsInWhenPresent<T> withListEmptyCallback(Callback callback) {
        return new Builder<T>()
                .withValues(values)
                .withValueStreamTransformer(valueStreamTransformer)
                .withEmptyCallback(callback)
                .build();
    }

    @SafeVarargs
    public static <T> IsInWhenPresent<T> of(T... values) {
        return of(Arrays.asList(values));
    }

    public static <T> IsInWhenPresent<T> of(Collection<T> values) {
        return new Builder<T>()
                .withValues(values)
                .withValueStreamTransformer(s -> s.filter(Objects::nonNull))
                .build();
    }

    public static class Builder<T> extends AbstractListConditionBuilder<T, Builder<T>> {
        @Override
        protected Builder<T> getThis() {
            return this;
        }

        protected IsInWhenPresent<T> build() {
            return new IsInWhenPresent<>(this);
        }
    }
}
