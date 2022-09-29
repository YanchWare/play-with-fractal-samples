package com.yanchware.fractal.sdk.sample;

import com.yanchware.fractal.sdk.Automaton;
import com.yanchware.fractal.sdk.aggregates.Environment;
import com.yanchware.fractal.sdk.aggregates.LiveSystem;
import com.yanchware.fractal.sdk.domain.entities.livesystem.caas.*;
import com.yanchware.fractal.sdk.domain.entities.livesystem.caas.providers.azure.AzureKubernetesService;
import com.yanchware.fractal.sdk.domain.entities.livesystem.caas.providers.azure.AzurePostgreSQL;
import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;
import com.yanchware.fractal.sdk.valueobjects.ComponentId;
import java.util.List;

import com.yanchware.fractal.sdk.sample.configuration.EnvVarConfiguration;
import static com.yanchware.fractal.sdk.sample.components.Components.getNodePools;
import static com.yanchware.fractal.sdk.domain.entities.livesystem.caas.providers.azure.AzureRegion.EUROPE_WEST;
import static com.yanchware.fractal.sdk.domain.entities.livesystem.caas.providers.azure.AzureSkuName.B_GEN5_1;

public class SdkSample {

  public static void main (String[] args) throws InstantiatorException {

    // CONFIGURATION:

    var configuration = EnvVarConfiguration.getInstance();

    var env = Environment.builder()
        .withId(configuration.getSubscriptionId())
        .withDisplayName(configuration.getEnvironmentDisplayName())
        .withParentId(configuration.getTenantId())
        .withParentType("tenant")
        .build();


    // BLUEPRINTS DEFINITION:


    var aks = AzureKubernetesService.builder()
        .withId(ComponentId.from("aks-1"))
        .withDescription("AKS cluster 1.3")
        .withDisplayName("AKS cluster 1.3")
        .withRegion(EUROPE_WEST)
        .withNodePools(getNodePools())
        .withAPIGateway(Ambassador.builder().withNamespace("fractal").withId("ambassador-1").build())
        .build();

    var postgresDbms = AzurePostgreSQL.builder()
        .withId(ComponentId.from("rdbms-1"))
        .withDescription("PostgreSQL DBMS")
        .withDisplayName("PostgreSQL DBMS")
        .withRegion(EUROPE_WEST)
        .withDatabase(PostgreSQLDB.builder()
            .withId(ComponentId.from("demo-db"))
            .withDisplayName("PostgreSQL Database")
            .withDescription("PostgreSQL Database")
            .withName("demo-db")
            .build()
        )
        .withSkuName(B_GEN5_1)
      .build();


    // INSTANTIATION:

    LiveSystem liveSystem = LiveSystem.builder()
        .withName(configuration.getLiveSystemName())
        .withDescription("Fractal demo")
        .withResourceGroupId(configuration.getResourceGroupId())
        .withComponents(List.of(aks, postgresDbms))
        .withEnvironment(env)
      . build();

    Automaton.instantiate(List.of(liveSystem));
  }
}