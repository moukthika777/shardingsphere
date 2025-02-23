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

package org.apache.shardingsphere.data.pipeline.core.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.shardingsphere.data.pipeline.api.config.job.MigrationJobConfiguration;
import org.apache.shardingsphere.data.pipeline.api.config.job.yaml.YamlMigrationJobConfiguration;
import org.apache.shardingsphere.data.pipeline.api.config.job.yaml.YamlMigrationJobConfigurationSwapper;
import org.apache.shardingsphere.data.pipeline.api.datasource.config.PipelineDataSourceConfiguration;
import org.apache.shardingsphere.data.pipeline.api.datasource.config.impl.ShardingSpherePipelineDataSourceConfiguration;
import org.apache.shardingsphere.data.pipeline.api.datasource.config.impl.StandardPipelineDataSourceConfiguration;
import org.apache.shardingsphere.data.pipeline.api.datasource.config.yaml.YamlPipelineDataSourceConfiguration;
import org.apache.shardingsphere.data.pipeline.api.job.JobType;
import org.apache.shardingsphere.data.pipeline.core.api.PipelineAPIFactory;

/**
 * Job configuration builder.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobConfigurationBuilder {
    
    /**
     * Create job configuration.
     *
     * @return created job configuration
     */
    public static MigrationJobConfiguration createJobConfiguration() {
        YamlMigrationJobConfiguration result = new YamlMigrationJobConfiguration();
        result.setTargetDatabaseName("logic_db");
        result.setSourceDataSourceName("standard_0");
        // TODO add autoTables in config file
        result.setSource(createYamlPipelineDataSourceConfiguration(new StandardPipelineDataSourceConfiguration(ConfigurationFileUtil.readFile("migration_standard_jdbc_source.yaml"))));
        result.setTarget(createYamlPipelineDataSourceConfiguration(new ShardingSpherePipelineDataSourceConfiguration(
                ConfigurationFileUtil.readFile("migration_sharding_sphere_jdbc_target.yaml"))));
        result.setSourceTableName("t_order");
        result.setTargetTableName("t_order");
        PipelineAPIFactory.getPipelineJobAPI(JobType.MIGRATION).extendYamlJobConfiguration(result);
        return new YamlMigrationJobConfigurationSwapper().swapToObject(result);
    }
    
    private static YamlPipelineDataSourceConfiguration createYamlPipelineDataSourceConfiguration(final PipelineDataSourceConfiguration config) {
        YamlPipelineDataSourceConfiguration result = new YamlPipelineDataSourceConfiguration();
        result.setType(config.getType());
        result.setParameter(config.getParameter());
        return result;
    }
}
