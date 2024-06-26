package com.ajulay.generator

import com.ajulay.migration.generator.ChangeLog
import com.ajulay.migration.generator.ChangeSet
import com.ajulay.migration.generator.ChangeSetData
import com.ajulay.migration.generator.SqlFile
import com.ajulay.migration.generator.SqlFileData
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.nio.file.Files


const val SQL_DELIMITER = "SQL_END"
const val RESOURCE_DIR = "../migration/src/main/resources/db/changelog"

fun main() {
    val objectMapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
        .setSerializationInclusion(Include.NON_NULL)

    generateChangelogFile(objectMapper)
}

private fun generateChangelogFile(objectMapper: ObjectMapper) {
    val root = File(RESOURCE_DIR)

    val changeSet = root.walkTopDown()
        .filter { it.isFile && it.extension == "sql" }
        .sortedBy { it.name }
        .map {
            ChangeSet(
                ChangeSetData(
                    FilenameUtils.getBaseName(it.name),
                    listOf(
                        SqlFile(
                            SqlFileData(
                                path = it.absolutePath.removePrefix("${File(RESOURCE_DIR).absolutePath}/"),
                                endDelimiter = getEndDelimiter(it),
                            ),
                        ),
                    ),
                ),
            )
        }.toList()

    val changelogFilePath = "${root.absolutePath}/db.changelog-master.yaml"
    val changelogFile = File(changelogFilePath)

    // Write content to YAML file
    objectMapper.writeValue(changelogFile, ChangeLog(changeSet))
    println("Changelog file generated at: ${changelogFile.absolutePath}")
}

fun getEndDelimiter(file: File): String? {
    return if (Files.readAllLines(file.toPath()).last() == SQL_DELIMITER) {
        SQL_DELIMITER
    } else {
        null
    }
}