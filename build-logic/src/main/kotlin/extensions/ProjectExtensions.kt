package extensions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

fun Project.catalog(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
