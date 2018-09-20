package com.devonfw.module.rest.service.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

/**
 * This class helps to deal with {@link UriInfo} and {@link MultivaluedMap} from the JAX-RS API. E.g. if you have a REST
 * query operation for a collection URI you can use {@link UriInfo} in case you want to support a mixture of optional
 * and required parameters. The methods provided here throw according exceptions such as {@link BadRequestException} and
 * already support conversion of values.
 *
 * @since 2.0.0
 */
public class RequestParameters {

  private final MultivaluedMap<String, String> parameters;

  /**
   * The constructor.
   *
   * @param parameters is the {@link MultivaluedMap} containing the parameters to wrap.
   */
  public RequestParameters(MultivaluedMap<String, String> parameters) {

    super();
    this.parameters = parameters;
  }

  /**
   * Gets the single parameter in a generic and flexible way.
   *
   * @param <T> is the generic type of <code>targetType</code>.
   * @param key is the {@link java.util.Map#get(Object) key} of the parameter to get.
   * @param targetType is the {@link Class} reflecting the type to convert the value to. Supports common Java standard
   *        types such as {@link String}, {@link Long}, {@link Double}, {@link BigDecimal}, etc.
   * @param required - {@code true} if the value is required and a {@link BadRequestException} is thrown if it is not
   *        present, {@code false} otherwise (if optional).
   * @return the value for the given <code>key</code> converted to the given <code>targetType</code>. May be
   *         {@code null} if <code>required</code> is {@code false} .
   * @throws WebApplicationException if an error occurred. E.g. {@link BadRequestException} if a required parameter is
   *         missing or {@link InternalServerErrorException} if the given <code>targetType</code> is not supported.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key, Class<T> targetType, boolean required) throws WebApplicationException {

    String value = get(key);
    if (value == null) {
      if (required) {
        throw new BadRequestException("Missing parameter: " + key);
      }
      Object result = null;
      if (targetType.isPrimitive()) {
        if (targetType == boolean.class) {
          result = Boolean.FALSE;
        } else if (targetType == int.class) {
          result = Integer.valueOf(0);
        } else if (targetType == long.class) {
          result = Long.valueOf(0);
        } else if (targetType == double.class) {
          result = Double.valueOf(0);
        } else if (targetType == float.class) {
          result = Float.valueOf(0);
        } else if (targetType == byte.class) {
          result = Byte.valueOf((byte) 0);
        } else if (targetType == short.class) {
          result = Short.valueOf((short) 0);
        } else if (targetType == char.class) {
          result = '\0';
        }
      }
      return (T) result;
    }
    try {
      return convertValue(value, targetType);
    } catch (WebApplicationException e) {
      throw e;
    } catch (Exception e) {
      throw new BadRequestException("Failed to convert '" + value + "' to type " + targetType);
    }
  }

  /**
   * Converts the given <code>value</code> to the given <code>targetType</code>.
   *
   * @param <T> is the generic type of <code>targetType</code>.
   * @param value is the value to convert.
   * @param targetType is the {@link Class} reflecting the type to convert the value to.
   * @return the converted value.
   * @throws ParseException if parsing of the given <code>value</code> failed while converting.
   */
  @SuppressWarnings("unchecked")
  protected <T> T convertValue(String value, Class<T> targetType) throws ParseException {

    if (value == null) {
      return null;
    }
    Object result;
    if (targetType == String.class) {
      result = value;
    } else if (targetType.isEnum()) {
      for (T instance : targetType.getEnumConstants()) {
        Enum<?> e = (Enum<?>) instance;
        if (e.name().equalsIgnoreCase(value)) {
          return instance;
        }
      }
      throw new IllegalArgumentException("Enum constant not found!");
    } else if ((targetType == boolean.class) || (targetType == Boolean.class)) {
      result = Boolean.parseBoolean(value);
    } else if ((targetType == int.class) || (targetType == Integer.class)) {
      result = Integer.valueOf(value);
    } else if ((targetType == long.class) || (targetType == Long.class)) {
      result = Long.valueOf(value);
    } else if ((targetType == double.class) || (targetType == Double.class)) {
      result = Double.valueOf(value);
    } else if ((targetType == float.class) || (targetType == Float.class)) {
      result = Float.valueOf(value);
    } else if ((targetType == short.class) || (targetType == Short.class)) {
      result = Short.valueOf(value);
    } else if ((targetType == byte.class) || (targetType == Byte.class)) {
      result = Byte.valueOf(value);
    } else if (targetType == BigDecimal.class) {
      result = new BigDecimal(value);
    } else if (targetType == BigInteger.class) {
      result = new BigInteger(value);
    } else if (targetType == Date.class) {
      result = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss").parseObject(value);
    } else {
      throw new InternalServerErrorException("Unsupported type " + targetType);
    }
    // do not use type.cast() as not working for primitive types.
    return (T) result;
  }

  /**
   * Gets the parameter as single value with the given <code>key</code> as {@link String}.
   *
   * @param key is the {@link java.util.Map#get(Object) key} of the parameter to get.
   * @return the requested parameter. Will be {@code null} if the parameter is not present.
   * @throws BadRequestException if the parameter is defined multiple times (see {@link #getList(String)}).
   */
  public String get(String key) throws BadRequestException {

    List<String> list = this.parameters.get(key);
    if ((list == null) || (list.isEmpty())) {
      return null;
    }
    if (list.size() > 1) {
      throw new BadRequestException("Duplicate parameter: " + key);
    }
    return list.get(0);
  }

  /**
   * Gets the parameter with the given <code>key</code> as {@link String}. Unlike {@link #get(String)} this method will
   * not throw an exception if the parameter is multi-valued but just return the first value.
   *
   * @param key is the {@link java.util.Map#get(Object) key} of the parameter to get.
   * @return the first value of the requested parameter. Will be {@code null} if the parameter is not present.
   */
  public String getFirst(String key) {

    return this.parameters.getFirst(key);
  }

  /**
   * Gets the {@link List} of all value for the parameter with with the given <code>key</code>. In general you should
   * avoid multi-valued parameters (e.g. http://host/path?query=a&query=b). The JAX-RS API supports this exotic case as
   * first citizen so we expose it here but only use it if you know exactly what you are doing.
   *
   * @param key is the {@link java.util.Map#get(Object) key} of the parameter to get.
   * @return the {@link List} with all values of the requested parameter. Will be an {@link Collections#emptyList()
   *         empty list} if the parameter is not present.
   */
  public List<String> getList(String key) {

    List<String> list = this.parameters.get(key);
    if (list == null) {
      list = Collections.emptyList();
    }
    return list;
  }

  /**
   * @param uriInfo is the {@link UriInfo}.
   * @return a new instance of {@link RequestParameters} for {@link UriInfo#getQueryParameters()}.
   */
  public static RequestParameters fromQuery(UriInfo uriInfo) {

    return new RequestParameters(uriInfo.getQueryParameters());
  }

  /**
   * @param uriInfo is the {@link UriInfo}.
   * @return a new instance of {@link RequestParameters} for {@link UriInfo#getPathParameters()}.
   */
  public static RequestParameters fromPath(UriInfo uriInfo) {

    return new RequestParameters(uriInfo.getPathParameters());
  }

}
