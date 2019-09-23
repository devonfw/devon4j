package com.devonfw.module.json.common.base.type;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.devonfw.module.json.common.base.ObjectMapperFactory;
import com.devonfw.module.test.common.base.ModuleTest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test of {@link ObjectMapperFactory} to test specific configurations and mappings from/to JSON.
 */
public class ObjectMapperFactoryTest extends ModuleTest {

  private static final Order SORT_ORDER1 = new Order(Direction.ASC, "ascendingProperty");

  private static final Order SORT_ORDER2 = new Order(Direction.DESC, "descendingProperty");

  private static final int PAGE_SIZE = 50;

  private static final int PAGE_NUMBER = 42;

  private static final String PAGEABLE_JSON = "{\"pageNumber\":42,\"pageSize\":50,\"sort\":[{\"property\":\"ascendingProperty\",\"direction\":\"ASC\"}," //
      + "{\"property\":\"descendingProperty\",\"direction\":\"DESC\"}]}";

  private static final String PAGE_JSON = "{\"content\":[\"foo\",\"bar\",\"some\"],\"pageable\":" + PAGEABLE_JSON
      + ",\"totalElements\":123456}";

  /**
   * Test serialization of {@link Pageable} with {@link PageableJsonSerializer}.
   *
   * @throws Exception on error
   */
  @Test
  public void testSerializePageable() throws Exception {

    // given
    Sort sort = Sort.by(SORT_ORDER1, SORT_ORDER2);
    Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, sort);
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when
    String json = mapper.writeValueAsString(pageable);

    // then
    assertThat(json).isEqualTo(PAGEABLE_JSON);
  }

  /**
   * Test deserialization of {@link Pageable} with {@link PageableJsonDeserializer}.
   *
   * @throws Exception on error
   */
  @Test
  public void testDeserializePageable() throws Exception {

    // given
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when
    Pageable pageable = mapper.readValue(PAGEABLE_JSON, Pageable.class);

    // then
    assertThat(pageable).isNotNull();
    assertThat(pageable.getPageNumber()).isEqualTo(PAGE_NUMBER);
    assertThat(pageable.getPageSize()).isEqualTo(PAGE_SIZE);
    assertThat(pageable.getSort()).containsExactly(SORT_ORDER1, SORT_ORDER2);
  }

  /**
   * Test serialization of {@link Page}.
   *
   * @throws Exception on error
   */
  @Test
  public void testSerializePage() throws Exception {

    // given
    Sort sort = Sort.by(SORT_ORDER1, SORT_ORDER2);
    Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, sort);
    List<String> content = Arrays.asList("foo", "bar", "some");
    Page<?> page = new PageImpl<>(content, pageable, 123456);
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when
    String json = mapper.writeValueAsString(page);

    // then
    assertThat(json).isEqualTo(PAGE_JSON);
  }

  /**
   * Test deserialization of {@link Page}.
   *
   * @throws Exception on error
   */
  @Test
  public void testDeserializePage() throws Exception {

    // given
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when
    Page<?> page = mapper.readValue(PAGE_JSON, Page.class);

    // then
    assertThat(page).isNotNull();
    Pageable pageable = page.getPageable();
    assertThat(pageable).isNotNull();
    assertThat(pageable.getPageNumber()).isEqualTo(PAGE_NUMBER);
    assertThat(pageable.getPageSize()).isEqualTo(PAGE_SIZE);
    assertThat(pageable.getSort()).containsExactly(SORT_ORDER1, SORT_ORDER2);
  }

  /**
   * Test serialization and deserialization of {@code java.time} types.
   *
   * @throws Exception on error
   */
  @Test
  public void testJavaTime() throws Exception {

    // given
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when + then
    check(mapper, Year.class, Year.of(1999), "\"1999\"");
    check(mapper, Instant.class, Instant.parse("1999-12-31T23:59:59Z"), "\"1999-12-31T23:59:59Z\"");
    check(mapper, LocalDateTime.class, LocalDateTime.parse("1999-12-31T23:59:59"), "\"1999-12-31T23:59:59\"");
    check(mapper, LocalDate.class, LocalDate.parse("1999-12-31"), "\"1999-12-31\"");
    check(mapper, LocalTime.class, LocalTime.parse("23:59:59"), "\"23:59:59\"");
  }

  /**
   * Test serialization and deserialization of {@link ZonedDateTime}.
   *
   * @throws Exception on error
   */
  @Test
  @Disabled("https://github.com/devonfw/devon4j/issues/116")
  public void testJavaTimeZonedDateTime() throws Exception {

    // given
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when + then
    String zonedDateTimeString = "1999-12-31T23:59:59+02:00";
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(zonedDateTimeString);
    assertThat(zonedDateTime.toString()).isEqualTo(zonedDateTimeString);
    check(mapper, ZonedDateTime.class, zonedDateTime, "\"" + zonedDateTimeString + "\"");
  }

  private <T> void check(ObjectMapper mapper, Class<T> type, T value, String json) throws Exception {

    assertThat(mapper.writeValueAsString(value)).isEqualTo(json);
    assertThat(mapper.readValue(json, type)).isEqualTo(value);
  }

}
