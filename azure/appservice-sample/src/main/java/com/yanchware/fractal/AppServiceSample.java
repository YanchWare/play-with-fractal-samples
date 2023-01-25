package com.yanchware.fractal;

import com.yanchware.fractal.azure.sample.configuration.EnvVarConfiguration;
import com.yanchware.fractal.sdk.Automaton;
import com.yanchware.fractal.sdk.aggregates.Environment;
import com.yanchware.fractal.sdk.aggregates.LiveSystem;
import com.yanchware.fractal.sdk.domain.entities.livesystem.paas.providers.azure.AzureRegion;
import com.yanchware.fractal.sdk.domain.entities.livesystem.paas.providers.azure.AzureResourceGroup;
import com.yanchware.fractal.sdk.domain.exceptions.InstantiatorException;

import java.util.List;

import static com.yanchware.fractal.azure.sample.components.WebAppComponent.*;

public class AppServiceSample {
  public static void main(String[] args) throws InstantiatorException {

    // CONFIGURATION:
    var configuration = EnvVarConfiguration.getInstance();

    var env = Environment.builder()
        .withId(configuration.getSubscriptionId())
        .withDisplayName(configuration.getEnvironmentDisplayName())
        .withParentId(configuration.getTenantId())
        .withParentType("tenant")
        .build();

    // LIVE-SYSTEM DEFINITION:
    LiveSystem liveSystem = LiveSystem.builder()
        .withName(configuration.getLiveSystemName())
        .withDescription("WebApp sample")
        .withResourceGroupId(configuration.getResourceGroupId())
        .withComponent(
            getJavaWebAppComponent(
                "sample-web-app", 
                new AzureResourceGroup(AzureRegion.AUSTRALIA_CENTRAL, "Sample-WebApps")))
        .withEnvironment(env)
    .build();

    // LIVE-SYSTEM INSTANTIATION:
    Automaton.instantiate(List.of(liveSystem));;
  }
}