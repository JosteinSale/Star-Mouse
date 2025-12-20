package utils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for creating singleton instances.
 * Subclasses do not need to explicitly call super(); the superclass constructor
 * is invoked implicitly by Java. This implementation enforces one instance
 * per concrete subclass.
 */
public abstract class Singleton {
   // tracks which concrete subclasses already have an instance
   private static final Set<Class<?>> INSTANCES = ConcurrentHashMap.newKeySet();

   protected Singleton() {
      Class<?> cls = getClass();
      if (!INSTANCES.add(cls)) {
         throw new IllegalStateException("Singleton instance of " + cls.getName() + " already created");
      }
   }
}