package com.yanchware.fractal;

import com.yanchware.fractal.azure.elastic.datastore.sample.configuration.EnvVarConfiguration;
import com.yanchware.fractal.sdk.Automaton;
import com.yanchware.fractal.sdk.aggregates.Environment;
import com.yanchware.fractal.sdk.aggregates.LiveSystem;
import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;

import java.util.List;

import static com.yanchware.fractal.azure.elastic.datastore.sample.components.AksComponent.getAks;

public class ElasticDatastoreSample {
  public static void main(String[] args) throws InstantiatorException {
    // CONFIGURATION:
    var configuration = EnvVarConfiguration.getInstance();

    var env = Environment.builder()
        .withId(configuration.getSubscriptionId())
        .withDisplayName(configuration.getEnvironmentDisplayName())
        .withParentId(configuration.getTenantId())
        .withParentType("tenant")
        .build();

    // INSTANTIATION:
    LiveSystem liveSystem = LiveSystem.builder()
        .withName(configuration.getLiveSystemName())
        .withDescription("Elastic Logging in AKS sample")
        .withResourceGroupId(configuration.getResourceGroupId())
        .withComponent(getAks("aks-1"))
        .withEnvironment(env)
        .build();

    Automaton.instantiate(List.of(liveSystem));
  }
}