package ${package}.general.common.beanmapping;

import jakarta.inject.Inject;

/**
 * This abstract class provides {@link #getBeanMapper() access} to the {@link BeanMapper}.
 */
public abstract class AbstractBeanMapperSupport {

  @Inject
  @javax.inject.Inject
  private BeanMapper beanMapper;

  /**
   * @return the {@link BeanMapper} instance.
   */
  protected BeanMapper getBeanMapper() {

    return this.beanMapper;
  }

}
