= devon4j

Devon4j is the Java stack of http://devonfw.com[devonfw]. It allows you to build business applications (backends) using Java technology in a highly efficient and standardized way based on established best-practices. To build web-clients as frontend for a devon4j backend we recommend https://github.com/devonfw/devon4ng[devon4ng].

image:https://img.shields.io/github/license/devonfw/devon4j.svg?label=License["Apache License, Version 2.0",link=https://github.com/devonfw/devon4j/blob/develop/LICENSE.txt]
image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-basic.svg?label=Maven%20Central["Maven Central",link=https://search.maven.org/search?q=g:com.devonfw.java.modules]
image:https://github.com/devonfw/devon4j/actions/workflows/build.yml/badge.svg["Build Status",link="https://github.com/devonfw/devon4j/actions/workflows/build.yml"]

For details see xref:modules[modules] below.

== Documentation

* link:documentation/architecture.asciidoc[Architecture Overview]
* link:documentation/guide-domain-layer.asciidoc[Domain Layer]
* link:documentation/guide-logic-layer.asciidoc[Logic Layer]
* link:documentation/guide-service-layer.asciidoc[Service Layer]
* link:documentation/guide-batch-layer.asciidoc[Batch Layer]
* link:documentation/guide-client-layer.asciidoc[Client Layer (GUI)]

=== Coding 

* link:documentation/coding-conventions.asciidoc[Coding Conventions]
* link:documentation/coding-tools.asciidoc[Development Tools]

=== Guides

* link:documentation/guide-dependency-injection.asciidoc[Bean Dependency Injection]
* link:documentation/guide-configuration.asciidoc[Configuration]
* link:documentation/guide-logging.asciidoc[Logging]
* link:documentation/guide-exceptions.asciidoc[Exception Handling]
* link:documentation/guide-i18n.asciidoc[Internationalization (I18N)]
* link:documentation/guide-transferobject.asciidoc[Transferobjects]
* link:documentation/guide-beanmapping.asciidoc[Bean-Mapping]
* link:documentation/guide-datatype.asciidoc[Datatypes]
* link:documentation/guide-xml.asciidoc[XML]
* link:documentation/guide-json.asciidoc[JSON]
* link:documentation/guide-rest.asciidoc[REST]
* link:documentation/guide-soap.asciidoc[SOAP]
* link:documentation/guide-service-client.asciidoc[Service Client]
* link:documentation/guide-kafka.asciidoc[Kafka]
* link:documentation/guide-validation.asciidoc[Validation]
* link:documentation/guide-security.asciidoc[Security]
* link:documentation/guide-access-control.asciidoc[Access Control]
* link:documentation/guide-testing.asciidoc[Testing]
* link:documentation/guide-transactions.asciidoc[Transaction Handling]
* link:documentation/guide-aop.asciidoc[AOP]
* link:documentation/guide-jpa.asciidoc[JPA]
* link:documentation/guide-repository.asciidoc[Repository]
* link:documentation/guide-dao.asciidoc[DAO]
* link:documentation/guide-jpa-query.asciidoc[Queries]
* link:documentation/guide-jpa-performance.asciidoc[JPA Performance]
* link:documentation/guide-auditing.asciidoc[Auditing]
* link:documentation/guide-database-migration.asciidoc[Database Migration]
* link:documentation/guide-accessibility.asciidoc[Accessibility]
* link:documentation/guide-caching.asciidoc[Caching]
* link:documentation/guide-cors-support.asciidoc[CORS support]
* link:documentation/guide-blob-support.asciidoc[BLOB support]
* link:documentation/guide-sql.asciidoc[SQL]
* link:documentation/guide-apm.asciidoc[Application Performance Management]

== Options

* link:documentation/guide-jee.asciidoc[JEE]

=== Tutorials

* link:documentation/tutorial-newapp.asciidoc[Start a new application]

== Modules

Here you can see a list of all provided modules with their JavaDoc and Artifacts

* basic image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-basic/javadoc.svg["basic JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-basic] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-basic.svg?label=Maven%20Central["basic artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-basic]
* batch image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-batch/javadoc.svg["batch JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-batch] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-batch.svg?label=Maven%20Central["batch artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-batch]
* batch-tool image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-batch-tool/javadoc.svg["batch-tool JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-batch-tool] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-batch-tool.svg?label=Maven%20Central["batch-tool artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-batch-tool]
* beanmapping image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-beanmapping/javadoc.svg["beanmapping JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-beanmapping] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-beanmapping.svg?label=Maven%20Central["beanmapping artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-beanmapping]
* beanmapping-dozer image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-beanmapping-dozer/javadoc.svg["beanmapping-dozer JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-beanmapping-dozer] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-beanmapping-dozer.svg?label=Maven%20Central["beanmapping-dozer artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-beanmapping-dozer]
* beanmapping-orika image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-beanmapping-orika/javadoc.svg["beanmapping-orika JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-beanmapping-orika] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-beanmapping-orika.svg?label=Maven%20Central["beanmapping-orika artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-beanmapping-orika]
* cxf-client image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-client/javadoc.svg["cxf-client JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-client] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-client.svg?label=Maven%20Central["cxf-client artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-client]
* cxf-client-rest image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-client-rest/javadoc.svg["cxf-client-rest JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-client-rest] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-client-rest.svg?label=Maven%20Central["cxf-client-rest artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-client-rest]
* cxf-client-ws image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-client-ws/javadoc.svg["cxf-client-ws JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-client-ws] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-client-ws.svg?label=Maven%20Central["cxf-client-ws artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-client-ws]
* cxf-server image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-server/javadoc.svg["cxf-server JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-server] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-server.svg?label=Maven%20Central["cxf-server artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-server]
* cxf-server-rest image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-server-rest/javadoc.svg["cxf-server-rest JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-server-rest] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-server-rest.svg?label=Maven%20Central["cxf-server-rest artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-server-rest]
* cxf-server-ws image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-cxf-server-ws/javadoc.svg["cxf-server-ws JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-cxf-server-ws] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-cxf-server-ws.svg?label=Maven%20Central["cxf-server-ws artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-cxf-server-ws]
* jpa-basic image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-jpa-basic/javadoc.svg["jpa-basic JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-jpa-basic] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-jpa-basic.svg?label=Maven%20Central["jpa-basic artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-jpa-basic]
* jpa-dao image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-jpa-dao/javadoc.svg["jpa-dao JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-jpa-dao] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-jpa-dao.svg?label=Maven%20Central["jpa-dao artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-jpa-dao]
* jpa-envers image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-jpa-envers/javadoc.svg["jpa-envers JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-jpa-envers] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-jpa-envers.svg?label=Maven%20Central["jpa-envers artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-jpa-envers]
* jpa-spring-data image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-jpa-spring-data/javadoc.svg["jpa-spring-data JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-jpa-spring-data] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-jpa-spring-data.svg?label=Maven%20Central["jpa-spring-data artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-jpa-spring-data]
* json image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-json/javadoc.svg["json JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-json] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-json.svg?label=Maven%20Central["json artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-json]
* kafka image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-kafka/javadoc.svg["kafka JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-kafka] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-kafka.svg?label=Maven%20Central["kafka artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-kafka]
* logging image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-logging/javadoc.svg["logging JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-logging] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-logging.svg?label=Maven%20Central["logging artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-logging]
* rest image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-rest/javadoc.svg["rest JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-rest] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-rest.svg?label=Maven%20Central["rest artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-rest]
* security image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-security/javadoc.svg["security JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-security] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-security.svg?label=Maven%20Central["security artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-security]
* security-jwt image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-security-jwt/javadoc.svg["security-jwt JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-security-jwt] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-security-jwt.svg?label=Maven%20Central["security-jwt artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-security-jwt]
* security-keystore image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-security-keystore/javadoc.svg["security-keystore JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-security-keystore] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-security-keystore.svg?label=Maven%20Central["security-keystore artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-security-keystore]
* service image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-service/javadoc.svg["service JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-service] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-service.svg?label=Maven%20Central["service artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-service]
* test image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-test/javadoc.svg["test JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-test] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-test.svg?label=Maven%20Central["test artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-test]
* test-jpa image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-test-jpa/javadoc.svg["test-jpa JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-test-jpa] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-test-jpa.svg?label=Maven%20Central["test-jpa artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-test-jpa]
* web image:https://javadoc.io/badge2/com.devonfw.java.modules/devon4j-web/javadoc.svg["web JavaDoc", link=https://javadoc.io/doc/com.devonfw.java.modules/devon4j-web] image:https://img.shields.io/maven-central/v/com.devonfw.java.modules/devon4j-web.svg?label=Maven%20Central["web artifact",link=https://search.maven.org/search?q=g:com.devonfw.java.modules+a:devon4j-web]
