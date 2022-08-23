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

package org.apache.shardingsphere.sqltranslator.jooq;

import org.apache.shardingsphere.infra.database.type.DatabaseType;
import org.apache.shardingsphere.sql.parser.sql.common.statement.SQLStatement;
import org.apache.shardingsphere.sqltranslator.exception.SQLTranslationException;
import org.apache.shardingsphere.sqltranslator.exception.UnsupportedTranslatedSQLException;
import org.apache.shardingsphere.sqltranslator.spi.SQLTranslator;
import org.jooq.Query;
import org.jooq.impl.DSL;

/**
 * JOOQ SQL translator.
 */
public final class JooQSQLTranslator implements SQLTranslator {
    
    @Override
    public String translate(final String sql, final SQLStatement statement, final DatabaseType protocolType, final DatabaseType storageType) throws SQLTranslationException {
        try {
            Query query = DSL.using(JooQDialectRegistry.getSQLDialect(protocolType)).parser().parseQuery(sql);
            return DSL.using(JooQDialectRegistry.getSQLDialect(storageType)).render(query);
            // CHECKSTYLE:OFF
        } catch (final Exception ex) {
            // CHECKSTYLE:ON
            throw new UnsupportedTranslatedSQLException(sql);
        }
    }
    
    @Override
    public String getType() {
        return "JOOQ";
    }
}
