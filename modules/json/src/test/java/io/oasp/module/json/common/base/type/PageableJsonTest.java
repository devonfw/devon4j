package io.oasp.module.json.common.base.type;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.oasp.module.test.common.base.ModuleTest;

/**
 * Test of mapping {@link Pageable} from/to JSON.
 */
public class PageableJsonTest extends ModuleTest {

  private static final Order SORT_ORDER1 = new Order(Direction.ASC, "ascendingProperty");

  private static final Order SORT_ORDER2 = new Order(Direction.DESC, "descendingProperty");

  private static final int PAGE_SIZE = 50;

  private static final int PAGE_NUMBER = 42;

  private static final String JSON = "{\"pageNumber\":42,\"pageSize\":50,\"sort\":[{\"property\":\"ascendingProperty\",\"direction\":\"ASC\"}," //
      + "{\"property\":\"descendingProperty\",\"direction\":\"DESC\"}]}";

  /**
   * Test of {@link PageableJsonSerializer}.
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
    assertThat(json).isEqualTo(JSON);
  }

  /**
   * Test of {@link PageableJsonDeserializer}.
   *
   * @throws Exception on error
   */
  @Test
  public void testDeserializePageable() throws Exception {

    // given
    ObjectMapper mapper = new TestObjectMapperFactory().createInstance();

    // when
    Pageable pageable = mapper.readValue(JSON, Pageable.class);

    // then
    assertThat(pageable).isNotNull();
    assertThat(pageable.getPageNumber()).isEqualTo(PAGE_NUMBER);
    assertThat(pageable.getPageSize()).isEqualTo(PAGE_SIZE);
    assertThat(pageable.getSort()).containsExactly(SORT_ORDER1, SORT_ORDER2);
  }
}
