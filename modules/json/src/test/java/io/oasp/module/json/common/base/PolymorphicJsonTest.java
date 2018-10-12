package io.oasp.module.json.common.base;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import io.oasp.module.test.common.base.ModuleTest;

/**
 * Test of {@link ObjectMapperFactory} with polymorphic data.
 */
@SuppressWarnings("javadoc")
public class PolymorphicJsonTest extends ModuleTest {

  /**
   *
   */
  private static final String JSON = "{\"animals\":[{\"@type\":\"cat\",\"name\":\"kitty\",\"female\":true}," //
      + "{\"@type\":\"dog\",\"name\":\"woffie\",\"female\":false}]}";

  @Test
  public void testPolymorphicSerialization() throws Exception {

    // given
    Cat cat = new Cat();
    cat.setName("kitty");
    cat.setFemale(true);
    Dog dog = new Dog();
    dog.setName("woffie");
    dog.setFemale(false);
    Zoo zoo = new Zoo();
    zoo.getAnimals().add(cat);
    zoo.getAnimals().add(dog);
    ObjectMapper mapper = new PolymorphicObjectMapperFactory().createInstance();

    // when
    String json = mapper.writeValueAsString(zoo);

    // then
    assertThat(json).isEqualTo(JSON);
  }

  @Test
  public void testPolymorphicDeserialization() throws Exception {

    // given
    ObjectMapper mapper = new PolymorphicObjectMapperFactory().createInstance();

    // when
    Zoo zoo = mapper.readValue(JSON, Zoo.class);

    // then
    assertThat(zoo).isNotNull();
    List<Animal> animals = zoo.getAnimals();
    assertThat(animals).hasSize(2);
    Animal animal1 = animals.get(0);
    assertThat(animal1.getName()).isEqualTo("kitty");
    assertThat(animal1.isFemale()).isTrue();
    assertThat(animal1).isInstanceOf(Cat.class);
    Animal animal2 = animals.get(1);
    assertThat(animal2.getName()).isEqualTo("woffie");
    assertThat(animal2.isFemale()).isFalse();
    assertThat(animal2).isInstanceOf(Dog.class);
  }

  public static abstract class Animal {

    private String name;

    private boolean female;

    public String getName() {

      return this.name;
    }

    public void setName(String name) {

      this.name = name;
    }

    public boolean isFemale() {

      return this.female;
    }

    public void setFemale(boolean female) {

      this.female = female;
    }
  }

  public static class Cat extends Animal {

  }

  public static class Dog extends Animal {

  }

  public static class Zoo {
    private List<Animal> animals = new ArrayList<>();

    public List<Animal> getAnimals() {

      return this.animals;
    }

    public void setAnimals(List<Animal> animals) {

      this.animals = animals;
    }
  }

  public static class PolymorphicObjectMapperFactory extends ObjectMapperFactory {

    public PolymorphicObjectMapperFactory() {

      super();
      addBaseClasses(Animal.class);
      addSubtypes(new NamedType(Cat.class, "cat"), new NamedType(Dog.class, "dog"));
    }
  }

}
