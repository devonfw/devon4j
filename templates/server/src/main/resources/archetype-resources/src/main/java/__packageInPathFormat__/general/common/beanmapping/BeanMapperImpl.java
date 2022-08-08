package ${package}.general.common.beanmapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.pkg.general.common.beanmapping.CustomMapperEto;
import it.pkg.general.common.entity.GenericEntity;
import it.pkg.general.common.to.AbstractGenericEto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * This is the implementation of {@link ${package}.general.common.beanmapping.beanmapping.common.api.BeanMapper} using orika
 * {@link MapperFacade}.
 */
@ApplicationScoped
@Named
@javax.inject.Named
public class BeanMapperImpl implements BeanMapper {

  private MapperFacade orika;

  /**
   * The constructor.
   */
  public BeanMapperImpl() {

    super();
    MapperFactory factory = new DefaultMapperFactory.Builder().build();
    CustomMapperEto customMapper = new CustomMapperEto();
    factory.classMap(GenericEntity.class, AbstractGenericEto.class).customize(customMapper).byDefault()
        .favorExtension(true).register();
    this.orika = factory.getMapperFacade();
  }

  @Override
  public <API, S extends API, T extends API> T mapTypesafe(Class<API> apiClass, S source, Class<T> targetClass) {

    return map(source, targetClass);
  }

  @Override
  public <T> List<T> mapList(List<?> source, Class<T> targetClass) {

    return mapList(source, targetClass, false);
  }

  @Override
  public <T> List<T> mapList(List<?> source, Class<T> targetClass, boolean suppressNullValues) {

    if ((source == null) || (source.isEmpty())) {
      return new ArrayList<>();
    }
    List<T> result = new ArrayList<>(source.size());
    for (Object sourceObject : source) {
      if ((sourceObject != null) || !suppressNullValues) {
        result.add(map(sourceObject, targetClass));
      }
    }
    return result;
  }

  @Override
  public <T> Set<T> mapSet(Set<?> source, Class<T> targetClass) {

    return mapSet(source, targetClass, false);
  }

  @Override
  public <T> Set<T> mapSet(Set<?> source, Class<T> targetClass, boolean suppressNullValues) {

    if ((source == null) || (source.isEmpty())) {
      return new HashSet<>();
    }
    Set<T> result = new HashSet<>(source.size());
    for (Object sourceObject : source) {
      if ((sourceObject != null) || !suppressNullValues) {
        result.add(map(sourceObject, targetClass));
      }
    }
    return result;
  }

  /**
   * @param orika the orika {@link MapperFacade} to set
   */
  @Inject
  public void setOrika(MapperFacade orika) {

    this.orika = orika;
  }

  @Override
  public <T> T map(Object source, Class<T> targetClass) {

    if (source == null) {
      return null;
    }
    return this.orika.map(source, targetClass);
  }

}
