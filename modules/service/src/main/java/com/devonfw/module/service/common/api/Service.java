package com.devonfw.module.service.common.api;

/**
 * This is a marker interface for a <em>remote service</em>. Such service if offered by an application and can be called
 * from other applications via the network. Such services often use HTTP (such as REST services or SOAP services) but
 * may also use other protocols. It is recommended that you define the API of the service via an interface and then
 * provide the implementation as a class implementing that interface. You are not forced to extend this marker interface
 * by your service API interface but doing so gives you some advantages like auto-registration of your services when
 * using the according spring-boot-starter with zero configuration. If you want to decouple your code as much as
 * possible you are free to ignore this interface or simply copy it to your own project and package.
 *
 * @since 3.0.0
 */
public interface Service {

}
