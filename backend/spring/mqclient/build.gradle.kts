plugins {
	id("java-library")
	id("org.springframework.boot") version "3.1.0" apply false
	id("io.spring.dependency-management") version "1.1.0"
}

group = "hu.bme.mit.alf.manuel"
version = findProperty("commonVersion")!!
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.amqp:spring-rabbit-test")
}

dependencyManagement {
	imports {
		mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
