package ${package}.general.common.base.test;

import javax.inject.Inject;
import javax.inject.Named;
#if ($dbMigration == 'liquibase')  
 import liquibase.Liquibase;
#else 
import org.flywaydb.core.Flyway;
#end
/**
 * This class provides methods for handling the database during testing where resets (and other operations) may be
 * necessary.
 */
@Named
public class DbTestHelper {
 #if ($dbMigration == 'liquibase')  
   private Liquibase liquibase; 
 #else 
   private Flyway flyway;
 #end
 
 #if ($dbMigration == 'liquibase')  
   /**
    * The constructor.
    *
    * @param liquibase an instance of type {@link Liquibase}.
    */
   public DbTestHelper( Liquibase liquibase) {
     super();
       this.liquibase = liquibase;
 }
 #else 
   /**
    * The constructor.
    *
    * @param flyway an instance of type {@link Flyway}.
    */
   public DbTestHelper( Flyway flyway) {
     super();
       this.flyway = flyway;
 }
 #end
 
  #if ($dbMigration == 'flyway')
  /**
   * Calls {@link #dropDatabase()} internally, and migrates to the highest available migration (default) or to the
   * {@code migrationVersion} specified by {@link #setMigrationVersion(String)}.
   */
  public void resetDatabase() {
    
      this.flyway.migrate();   
  }
  #end

#if ($dbMigration == 'liquibase')  
  /**
   * Drops the whole database.
   */
  public void cleanup() {

    try {
      this.liquibase.dropAll();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
 #else
   /**
    * Drops the whole database.
    */
   public void dropDatabase() {
       this.flyway.clean();
   }
#end

}
