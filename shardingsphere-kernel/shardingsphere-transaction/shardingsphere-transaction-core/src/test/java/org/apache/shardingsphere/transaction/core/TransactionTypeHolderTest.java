/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.transaction.core;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public final class TransactionTypeHolderTest {
    
    @Before
    public void setUp() {
        TransactionTypeHolder.clear();
    }
    
    @Test
    public void assertGetWithoutSetValue() {
        assertNull(TransactionTypeHolder.get());
    }
    
    @Test
    public void assertGetWithSetXaValue() {
        TransactionTypeHolder.set(TransactionType.XA);
        assertThat(TransactionTypeHolder.get(), is(TransactionType.XA));
    }

    @Test
    public void assertGetWithSetLocalValue() {
        TransactionTypeHolder.set(TransactionType.LOCAL);
        assertThat(TransactionTypeHolder.get(), is(TransactionType.LOCAL));
    }

    @Test
    public void assertGetWithSetBaseValue() {
        TransactionTypeHolder.set(TransactionType.BASE);
        assertThat(TransactionTypeHolder.get(), is(TransactionType.BASE));
    }
    
    @Test
    public void assertClear() {
        TransactionTypeHolder.set(TransactionType.XA);
        TransactionTypeHolder.clear();
        assertNull(TransactionTypeHolder.get());
    }
}
