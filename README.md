# Gradle Skeleton

This is a skeleton project demonstrating the following:
- Multi-project Gradle configuration.
- Sharing of common build logic using script plugins.
- Delegation of dependency management (including plugins and Gradle itself) to a private repository.
- Minimizing repetition of configuration boilerplate at the settings and project level.
- Using Kotlin to test Java projects.  (Specifically, JUnit, Spek and Kluent.)

See `*.gradle` for specifics and discussion or below for more about setup and background.

## Artifactory Setup

This example assumes you want to use a private repository server for all remote dependencies of the build.  (If you don't, see `repos.gradle` for a workaround.)  A private server has several advantages:
- It increases the likelihood that your build will work consistently even if upstream providers are down or slow.
- You have the flexibility to selectively deploy edge or override releases to problematic dependencies without losing the convenience of Gradle-style dependency declaration.
- Larger organizations may use a private repository for license monitoring or security purposes.

Using a private repository with Gradle can be challenging because there are multiple ways that Gradle tries to resolve build components.  In addition to basic dependencies, there are also plugin dependencies and Gradle itself.  All can be delegated, but the configuration to do so varies which is what this example tries to unify.

[Artifactory](https://jfrog.com/open-source/) is a popular repository server choice.  It's easy to run and has an open-source version.  These instructions assume that Artifactory has been setup with:
- A `gradle-distributions` generic remote repository backed by `https://services.gradle.org/distributions/`.
- A `gradle-plugins` maven-format remote repository backed by `https://plugins.gradle.org/m2/`.
- A maven-format remote repository backed by an upstream provider such as [jcenter](https://jcenter.bintray.com) or [maven central](http://repo1.maven.org/maven2/).
- A `libs-release` maven-format virtual repository merging the plugin and main maven repositories.

Docker installation (one option of many):
```
docker pull docker.bintray.io/jfrog/artifactory-oss
docker volume create --name my-artifactory-data
docker run --restart=unless-stopped -d --name jfrog-artifactory-oss-latest -p 8085:8081 -v my-artifactory-data/:/var/opt/jfrog/artifactory docker.bintray.io/jfrog/artifactory-oss:latest
```

## Common Build Configuration

In a multi-project build or just across multiple single-project builds you will likely find that there are configuration patterns that recur.  This is particularly true when it comes to repository configuration, but there may also be dependencies, versions or task logic that it's redundant to keep declaring.

The absolute minimum for a multi-project build is a `settings.gradle` in the root and then a `build.gradle` for each subproject.

In this example, reused configuration added in `gradle/common` and referenced in the project build files.  There's nothing special about that location; it's just out of the way.  Gradle's meta `buildSrc` project can be useful for reused snippets as well, but it's unnecessary in this case.

For multiple standalone projects wanting to share common configuration, it might be appropriate to maintain the common directory on its own and use a pegged external in the source control configuration to graft it into projects that need it.   

## Why?

Not all of these approaches may be ideal, but they represent natural impulses when dealing with repeated elements across similar builds, and discovering how to accomplish them with the tools Gradle provides can be challenging.  Hopefully future versions of Gradle will continue to improve so that the goals of centralizing repository configuration and encapsulating transitive plugin dependency relationships are better served.

## Comparison
For comparison, a single project, public repository version of what this example tries to abstract might look like the following `build.gradle`.
```
buildscript {
	repositories {
		maven {
			url 'https://plugins.gradle.org/m2'
		}
	}
	dependencies {
		classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.31'
		classpath 'org.junit.platform:junit-platform-gradle-plugin:1.1.0'
	}
}

plugins {
	id 'java'
}
apply plugin: 'kotlin'
apply plugin: 'org.junit.platform.gradle.plugin'

repositories {
	jcenter()
}

dependencies {
	testCompile 'org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.31'
	testRuntime 'org.jetbrains.kotlin:kotlin-reflect:1.2.31'

	testCompile 'org.junit.jupiter:junit-jupiter-api:5.1.0'

	testCompile('org.jetbrains.spek:spek-api:1.1.5') {
		exclude group: 'org.jetbrains.kotlin'
	}
	testRuntime('org.jetbrains.spek:spek-junit-platform-engine:1.1.5') {
		exclude group: 'org.junit.platform'
		exclude group: 'org.jetbrains.kotlin'
	}

	testCompile 'org.amshove.kluent:kluent:1.35'
}

compileTestKotlin {
	kotlinOptions {
		jvmTarget = '1.8'
	}
}

junitPlatform {
	filters {
		engines {
			include 'spek'
		}
	}
}
```
