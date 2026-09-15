plugins {
    alias(dairy.plugins.teamcode)
    alias(dairy.plugins.sloth.load)
}

ftc {
    kotlin()
}

dependencies {
    implementation(sdk.bundles.sdk)
    implementation(dairy.sloth)

    implementation(pedro.bundles.ivy)
    implementation(pedro.bundles.pedro3)
}