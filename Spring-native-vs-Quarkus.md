# Spring Native vs Quarkus

Nowadays it is very common to write an application and deploy it to cloud. 
Serverless computing and Function-as-a-Service (FaaS) have become very popular. 
While there are a lot of challenges to deploy a Java application into the latest cloud environment, the biggest challenges developers are facing is memory footprint and the startup time required for the Java application as more of these keeps the host costs high in public clouds and Kubernetes clusters. 
With the introduction of frameworks like micronaut and microprofile java processes are getting faster and more lightweight. 
In a similar context Spring has introduced [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#overview) which aims to solve the big memory footprint of Spring and its slow startup time to potentially rival the new framework called [Quarkus](https://quarkus.io) from Red Hat. 
This document shortly discusses both of these two frameworks and their potential suitabilities with devonfw.

## Quarkus

Quarkus is a full-stack, Kubernetes-native Java framework made for JVMs. 
With its container-first-philosophy and its native compilation Quarkus optimizes JAVA for containers with low memory usage and fast startup times.

Quarkus achieves this by the following ways:

* **First Class Support for GraalVM**
* **Build Time Metadata Processing:** As much processing as possible is done at build time, so your application will only contain the classes that are actually needed at runtime. This results in less memory usage, and also faster startup time as all metadata processing has already been done.
* **Reduction in Reflection Usage:** ToDo
  As much as possible Quarkus tries to avoid reflection, reducing startup time and memory usage.
* **Native Image Pre Boot:** When running in a native image Quarkus pre-boots as much of the framework as possible during the native image build process. This means that the resulting native image has already run most of the startup code and serialized the result into the executable, resulting in an even faster startup-time.

 This gives Quarkus the potential for a great platform for serverless, cloud and Kubernetes environments. For more information about Quarkus and its support for devonfw please refer to the [Quarkus introduction guide](https://github.com/devonfw/devon4j/blob/master/documentation/quarkus.asciidoc).

## Spring Native

**Note: Spring Native is still in Beta. The current version of Spring Native 0.10.5 is designed to be used with Spring Boot 2.5.6.**

Spring Native provides support for compiling Spring applications to native executables using the GraalVM native-image compiler. 
Using a native image provides some key advantages, such as instant startup, instant peak performance, and reduced memory consumption.
But there are also some drawbacks: Creating a native image is a heavy process which is slower than a regular application. 
A native image also has fewer runtime optimizations after its warmup.
Furthermore, it is less mature than the JVM with some different behaviors.

Difference in regular JVM vs GraalVM:

* Static analysis of the application from the main entry point is performed at build time.

* Unused parts are removed at build time.

* Configuration required for reflection, resources, and dynamic proxies.

* Classpath is fixed at build time.

* No class lazy loading: everything shipped in the executables will be loaded in memory on startup.

* Some code will run at build time.

There are [limitations](https://github.com/oracle/graal/blob/master/docs/reference-manual/native-image/Limitations.md) around some aspects of Java applications that are not fully supported and therefore should be aware of before using Spring Native.

## Build time and start time for apps

| Framework| build time | start time |
| ----------- | ----------- | ----------- |
| Spring Native | 19.615s | 2.913s |
| Quarkus | 52.818s | 0.802s |

## Memory footprints

| Framework| memory footprint |
| ----------- | ----------- |
| Spring Native | 109 MB |
| Quarkus | 75 MB |

## Considering devonfw best practices: 

Devonfw supports only Quarkus and Spring but not Spring Native. 
While Quarkus is rather new we still consider it as stable and mature enough to be supported. 
Spring on the hand is supported as it is widely used and is unmatched regarding stability and support, even though being much slower in build and start times and having a much bigger memory footprint in general. 
While Spring Native handles these problems quite well, it is still in beta stage and therefore not considered stable enough to be an option for devonfw. 
Maybe this will change in the future.

## General recommendations and conclusion:

For Greenfield projects and projects which are mainly focusing on microservices or cloud Quarkus can be recommended as it is better suitable for their requirements. 
For Spring projects, they should be able to use Spring Native in the future in order to improve their performance. We also provide a [guide for Spring developers](https://github.com/devonfw/devon4j/blob/master/documentation/quarkus/getting-started-for-spring-developers.asciidoc) if you want to adopt or try Quarkus for your (next) projects as it really has some gamechanging advantages over Spring.
As of now Quarkus and Spring Native both have their own use cases but neither of them can be considered as a general recommendation for everybody.





