package utils;

import java.util.HashMap;
import java.util.function.Function;

/**
 * A container for resources that can be kept in memory or flushed.
 * Whenever a caller requests a resource, we first check if the resource
 * already exists in this container. If so, we just pass along the existing
 * reference instead of loading an entirely new resource.
 * This ensures that only one instance of a given resource will exist in memory
 * atthe time.
 * 
 * Also it enables centralized loading and flushing of resources.
 *
 * 
 * @param <T> The type of resource being stored.
 *
 */
public class ResourceContainer<T extends Resource> {
   public HashMap<String, T> resourcesToBeKeptInMemory;
   public HashMap<String, T> resourcesToBeFlushed;
   private Function<String, T> getMethod;

   public ResourceContainer(Function<String, T> getMethod) {
      this.resourcesToBeKeptInMemory = new HashMap<>();
      this.resourcesToBeFlushed = new HashMap<>();
      this.getMethod = getMethod;
   }

   public T getResource(String fileName, boolean keepInMemory) {
      // First check if resource is already loaded
      if (resourcesToBeKeptInMemory.containsKey(fileName)) {
         return resourcesToBeKeptInMemory.get(fileName);
      } else if (resourcesToBeFlushed.containsKey(fileName)) {
         return resourcesToBeFlushed.get(fileName);
      } else {
         // Else: load resource
         T resource = getMethod.apply(fileName);
         if (keepInMemory) {
            resourcesToBeKeptInMemory.put(fileName, resource);
         } else {
            resourcesToBeFlushed.put(fileName, resource);
         }
         return resource;
      }
   }

   public void flush() {
      for (String resName : resourcesToBeFlushed.keySet()) {
         resourcesToBeFlushed.get(resName).flush();
      }
      resourcesToBeFlushed.clear();
   }

}
