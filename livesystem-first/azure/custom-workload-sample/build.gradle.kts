plugins {
    id("samples.java-conventions")
}

dependencies {
    implementation(project(":azure.shared.config"))
    testImplementation(testFixtures(project(":azure.shared.config")))
}

description = "custom-workload-sample"

tasks.jar {
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.MF")

    manifest {
        attributes(
            "Implementation-Version" to archiveVersion,
            "Main-Class" to "com.yanchware.fractal.samples.azure.customworkload.Sample"
        )
    }

    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}