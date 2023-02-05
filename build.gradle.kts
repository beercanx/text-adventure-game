// Here so what when './gradlew wrapper --gradle-version $LATEST_VERSION' is run it always uses the all distro.
tasks.withType<Wrapper>().configureEach {
    distributionType = Wrapper.DistributionType.ALL
}