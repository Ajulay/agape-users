package com.ajulay.liquibase


import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.jdbc.datasource.init.ScriptUtils.executeSqlScript
import javax.sql.DataSource

private const val CHANGELOG_PATH = "classpath:/db/changelog/db.changelog-master.yaml"
private const val PROJECT_SCHEMA_NAME = "agape"

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(LiquibaseProperties::class)
class LiquibaseConfiguration(
    private val dataSource: DataSource,
) {

    @Autowired
    private lateinit var liquibaseProperties: LiquibaseProperties

    init {
        executeSqlScript(
            dataSource.connection,
            ByteArrayResource("create schema if not exists $PROJECT_SCHEMA_NAME".toByteArray()),
        )
    }

    @Bean
    fun liquibase(dataSourceProvider: ObjectProvider<DataSource>): SpringLiquibase =
        SpringLiquibase().apply {
            changeLog = CHANGELOG_PATH
            isClearCheckSums = liquibaseProperties.isClearChecksums
            contexts = liquibaseProperties.contexts
            defaultSchema = PROJECT_SCHEMA_NAME
            liquibaseTablespace = liquibaseProperties.liquibaseTablespace
            databaseChangeLogTable = liquibaseProperties.databaseChangeLogTable
            databaseChangeLogLockTable = liquibaseProperties.databaseChangeLogLockTable
            isDropFirst = liquibaseProperties.isDropFirst
            setShouldRun(liquibaseProperties.isEnabled)
            labelFilter = liquibaseProperties.labelFilter
            setChangeLogParameters(liquibaseProperties.parameters)
            setRollbackFile(liquibaseProperties.rollbackFile)
            isTestRollbackOnUpdate = liquibaseProperties.isTestRollbackOnUpdate
            tag = liquibaseProperties.tag
            dataSource = dataSourceProvider.ifAvailable
                ?: throw IllegalStateException("Datasource not available")
        }

}