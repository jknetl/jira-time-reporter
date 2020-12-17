plugins {
  id("org.jetbrains.kotlin.jvm") version "1.4.10"
}

group "org.jknetl"
version "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("com.mashape.unirest:unirest-java:1.4.9")
  implementation("org.yaml:snakeyaml:1.27")
}

tasks.withType<Jar> {
  // Otherwise you'll get a "No main manifest attribute" error
  manifest {
    attributes["Main-Class"] = "org.jknetl.jira.MainKt"
  }

  // To add all of the dependencies
  from(sourceSets.main.get().output)

  dependsOn(configurations.runtimeClasspath)
  from({
    configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
  })
}

//tasks.regi {
//  manifest {
//    attributes 'Main-Class': 'org.jknetl.jira.MainKt'
//  }
//  from { configurations.compileClasspath.collect { it.isDirectory() ? it : zipTree(it) } }
//}
