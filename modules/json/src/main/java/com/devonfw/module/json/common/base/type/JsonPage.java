package com.devonfw.module.json.common.base.type;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Workaround to allow JSON mapping for {@link Page}.
 *
 * @param <T> type of the {@link #getContent() contained hits}.
 */
public class JsonPage<T> extends PageImpl<T> {

  private static final long serialVersionUID = 1L;

  /**
   * The constructor.
   *
   * @param content - see {@link #getContent()}.
   * @param pageable - see {@link #getPageable()}.
   * @param total - see {@link #getTotalElements()}.
   */
  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public JsonPage(@JsonProperty("content") List<T> content, @JsonProperty("pageable") Pageable pageable,
      @JsonProperty("totalElements") long total) {

    super(content, pageable, total);
  }

  @Override
  @JsonIgnore
  public boolean isFirst() {

    return super.isFirst();
  }

  @Override
  @JsonIgnore
  public boolean isLast() {

    return super.isLast();
  }

  @Override
  @JsonIgnore
  public int getNumberOfElements() {

    return super.getNumberOfElements();
  }

  @Override
  @JsonIgnore
  public int getTotalPages() {

    return super.getTotalPages();
  }

  @Override
  @JsonIgnore
  public Sort getSort() {

    return super.getSort();
  }

  @Override
  @JsonIgnore
  public boolean isEmpty() {

    return super.isEmpty();
  }

  @Override
  @JsonIgnore
  public int getNumber() {

    return super.getNumber();
  }

  @Override
  @JsonIgnore
  public int getSize() {

    return super.getSize();
  }

}
