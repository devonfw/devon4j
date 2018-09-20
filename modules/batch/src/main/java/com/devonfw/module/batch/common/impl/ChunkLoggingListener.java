package com.devonfw.module.batch.common.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.SkipListener;

/**
 * Spring Batch listener that logs exceptions together with the item(s) being processed at the time the exceptions
 * occurred.
 *
 */
public class ChunkLoggingListener<T, S> implements SkipListener<T, S>, ItemReadListener<T>, ItemProcessListener<T, S>,
    ItemWriteListener<S> {

  private static final Logger LOG = LoggerFactory.getLogger(ChunkLoggingListener.class);

  protected String itemToString(Object item) {

    return item.toString();
  }

  @Override
  public void onReadError(Exception e) {

    LOG.error("Failed to read item.", e);
  }

  @Override
  public void onProcessError(T item, Exception e) {

    LOG.error("Failed to process item: " + itemToString(item), e);
  }

  @Override
  public void onWriteError(Exception e, List<? extends S> items) {

    LOG.error("Failed to write items: " + itemToString(items), e);
  }

  @Override
  public void onSkipInRead(Throwable t) {

    LOG.warn("Skipped item in read.", t);
  }

  @Override
  public void onSkipInProcess(T item, Throwable t) {

    LOG.warn("Skipped item in process: " + itemToString(item), t);
  }

  @Override
  public void onSkipInWrite(S item, Throwable t) {

    LOG.warn("Skipped item in write: " + itemToString(item), t);
  }

  @Override
  public void beforeRead() {

  }

  @Override
  public void afterRead(T item) {

  }

  @Override
  public void beforeProcess(T item) {

  }

  @Override
  public void afterProcess(T item, S result) {

  }

  @Override
  public void beforeWrite(List<? extends S> items) {

  }

  @Override
  public void afterWrite(List<? extends S> items) {

  }

}