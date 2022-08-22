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

package org.apache.shardingsphere.sql.parser.sql.dialect.handler.ddl;

import org.apache.shardingsphere.sql.parser.sql.common.segment.ddl.index.IndexTypeSegment;
import org.apache.shardingsphere.sql.parser.sql.common.statement.ddl.CommentStatement;
import org.apache.shardingsphere.sql.parser.sql.common.value.identifier.IdentifierValue;
import org.apache.shardingsphere.sql.parser.sql.dialect.statement.oracle.ddl.OracleCommentStatement;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommentStatementHandlerTest {
    
    @Test
    public void assertGetIndexTypeSegmentWithIndexTypeSegment() {
        OracleCommentStatement statement = new OracleCommentStatement();
        statement.setIndexType(new IndexTypeSegment(0, 0, new IdentifierValue("")));
        Optional<IndexTypeSegment> indexTypeSegment = CommentStatementHandler.getIndexType(statement);
        assertTrue(indexTypeSegment.isPresent());
    }
    
    @Test
    public void assertGetIndexTypeSegmentWithoutIndexTypeSegment() {
        CommentStatement statement = Mockito.mock(CommentStatement.class);
        Optional<IndexTypeSegment> indexTypeSegment = CommentStatementHandler.getIndexType(statement);
        assertFalse(indexTypeSegment.isPresent());
    }
}
