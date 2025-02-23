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

package org.apache.shardingsphere.sharding.yaml.swapper;

import org.apache.shardingsphere.infra.yaml.config.pojo.algorithm.YamlAlgorithmConfiguration;
import org.apache.shardingsphere.infra.yaml.config.swapper.rule.YamlRuleConfigurationSwapper;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.constant.ShardingOrder;
import org.apache.shardingsphere.sharding.yaml.config.YamlShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.rule.YamlShardingAutoTableRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.config.rule.YamlTableRuleConfiguration;
import org.apache.shardingsphere.sharding.yaml.swapper.rule.YamlShardingAutoTableRuleConfigurationSwapper;
import org.apache.shardingsphere.sharding.yaml.swapper.rule.YamlShardingTableRuleConfigurationSwapper;
import org.apache.shardingsphere.sharding.yaml.swapper.strategy.YamlKeyGenerateStrategyConfigurationSwapper;
import org.apache.shardingsphere.sharding.yaml.swapper.strategy.YamlShardingAuditStrategyConfigurationSwapper;
import org.apache.shardingsphere.sharding.yaml.swapper.strategy.YamlShardingStrategyConfigurationSwapper;

import java.util.Map.Entry;

/**
 * YAML algorithm provider sharding rule configuration swapper.
 */
public final class YamlShardingRuleAlgorithmProviderConfigurationSwapper implements YamlRuleConfigurationSwapper<YamlShardingRuleConfiguration, AlgorithmProvidedShardingRuleConfiguration> {
    
    private final YamlShardingTableRuleConfigurationSwapper tableSwapper = new YamlShardingTableRuleConfigurationSwapper();
    
    private final YamlShardingStrategyConfigurationSwapper shardingStrategySwapper = new YamlShardingStrategyConfigurationSwapper();
    
    private final YamlKeyGenerateStrategyConfigurationSwapper keyGenerateStrategySwapper = new YamlKeyGenerateStrategyConfigurationSwapper();
    
    private final YamlShardingAuditStrategyConfigurationSwapper auditStrategySwapper = new YamlShardingAuditStrategyConfigurationSwapper();
    
    private final YamlShardingAutoTableRuleConfigurationSwapper autoTableYamlSwapper = new YamlShardingAutoTableRuleConfigurationSwapper();
    
    @Override
    public YamlShardingRuleConfiguration swapToYamlConfiguration(final AlgorithmProvidedShardingRuleConfiguration data) {
        YamlShardingRuleConfiguration result = new YamlShardingRuleConfiguration();
        data.getTables().forEach(each -> result.getTables().put(each.getLogicTable(), tableSwapper.swapToYamlConfiguration(each)));
        data.getAutoTables().forEach(each -> result.getAutoTables().put(each.getLogicTable(), autoTableYamlSwapper.swapToYamlConfiguration(each)));
        result.getBindingTables().addAll(data.getBindingTableGroups());
        result.getBroadcastTables().addAll(data.getBroadcastTables());
        setYamlStrategies(data, result);
        setYamlAlgorithms(data, result);
        result.setDefaultShardingColumn(data.getDefaultShardingColumn());
        result.setScalingName(data.getScalingName());
        return result;
    }
    
    private void setYamlStrategies(final AlgorithmProvidedShardingRuleConfiguration data, final YamlShardingRuleConfiguration yamlConfig) {
        if (null != data.getDefaultDatabaseShardingStrategy()) {
            yamlConfig.setDefaultDatabaseStrategy(shardingStrategySwapper.swapToYamlConfiguration(data.getDefaultDatabaseShardingStrategy()));
        }
        if (null != data.getDefaultTableShardingStrategy()) {
            yamlConfig.setDefaultTableStrategy(shardingStrategySwapper.swapToYamlConfiguration(data.getDefaultTableShardingStrategy()));
        }
        if (null != data.getDefaultKeyGenerateStrategy()) {
            yamlConfig.setDefaultKeyGenerateStrategy(keyGenerateStrategySwapper.swapToYamlConfiguration(data.getDefaultKeyGenerateStrategy()));
        }
        if (null != data.getDefaultAuditStrategy()) {
            yamlConfig.setDefaultAuditStrategy(auditStrategySwapper.swapToYamlConfiguration(data.getDefaultAuditStrategy()));
        }
    }
    
    private void setYamlAlgorithms(final AlgorithmProvidedShardingRuleConfiguration data, final YamlShardingRuleConfiguration yamlConfig) {
        if (null != data.getShardingAlgorithms()) {
            data.getShardingAlgorithms().forEach((key, value) -> yamlConfig.getShardingAlgorithms().put(key, new YamlAlgorithmConfiguration(value.getType(), value.getProps())));
        }
        if (null != data.getKeyGenerators()) {
            data.getKeyGenerators().forEach((key, value) -> yamlConfig.getKeyGenerators().put(key, new YamlAlgorithmConfiguration(value.getType(), value.getProps())));
        }
        if (null != data.getAuditors()) {
            data.getAuditors().forEach((key, value) -> yamlConfig.getAuditors().put(key, new YamlAlgorithmConfiguration(value.getType(), value.getProps())));
        }
    }
    
    @Override
    public AlgorithmProvidedShardingRuleConfiguration swapToObject(final YamlShardingRuleConfiguration yamlConfig) {
        AlgorithmProvidedShardingRuleConfiguration result = new AlgorithmProvidedShardingRuleConfiguration();
        for (Entry<String, YamlTableRuleConfiguration> entry : yamlConfig.getTables().entrySet()) {
            YamlTableRuleConfiguration tableRuleConfig = entry.getValue();
            tableRuleConfig.setLogicTable(entry.getKey());
            result.getTables().add(tableSwapper.swapToObject(tableRuleConfig));
        }
        for (Entry<String, YamlShardingAutoTableRuleConfiguration> entry : yamlConfig.getAutoTables().entrySet()) {
            YamlShardingAutoTableRuleConfiguration tableRuleConfig = entry.getValue();
            tableRuleConfig.setLogicTable(entry.getKey());
            result.getAutoTables().add(autoTableYamlSwapper.swapToObject(tableRuleConfig));
        }
        result.getBindingTableGroups().addAll(yamlConfig.getBindingTables());
        result.getBroadcastTables().addAll(yamlConfig.getBroadcastTables());
        setStrategies(yamlConfig, result);
        result.setDefaultShardingColumn(yamlConfig.getDefaultShardingColumn());
        return result;
    }
    
    private void setStrategies(final YamlShardingRuleConfiguration yamlConfig, final AlgorithmProvidedShardingRuleConfiguration ruleConfig) {
        if (null != yamlConfig.getDefaultDatabaseStrategy()) {
            ruleConfig.setDefaultDatabaseShardingStrategy(shardingStrategySwapper.swapToObject(yamlConfig.getDefaultDatabaseStrategy()));
        }
        if (null != yamlConfig.getDefaultTableStrategy()) {
            ruleConfig.setDefaultTableShardingStrategy(shardingStrategySwapper.swapToObject(yamlConfig.getDefaultTableStrategy()));
        }
        if (null != yamlConfig.getDefaultKeyGenerateStrategy()) {
            ruleConfig.setDefaultKeyGenerateStrategy(keyGenerateStrategySwapper.swapToObject(yamlConfig.getDefaultKeyGenerateStrategy()));
        }
        if (null != yamlConfig.getDefaultAuditStrategy()) {
            ruleConfig.setDefaultAuditStrategy(auditStrategySwapper.swapToObject(yamlConfig.getDefaultAuditStrategy()));
        }
    }
    
    @Override
    public Class<AlgorithmProvidedShardingRuleConfiguration> getTypeClass() {
        return AlgorithmProvidedShardingRuleConfiguration.class;
    }
    
    @Override
    public String getRuleTagName() {
        return "SHARDING";
    }
    
    @Override
    public int getOrder() {
        return ShardingOrder.ALGORITHM_PROVIDER_ORDER;
    }
}
