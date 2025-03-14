plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kotlin-collection-manager-server"
include("invoker-service")
include("collection-service")
include("file-service")