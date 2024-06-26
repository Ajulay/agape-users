plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "agape"
include("application")
include("liquibase")
include("liquibase:generator")
findProject(":liquibase:generator")?.name = "generator"
include("liquibase:migration")
findProject(":liquibase:migration")?.name = "migration"
