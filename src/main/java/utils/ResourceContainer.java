package utils;

import java.util.HashMap;
import java.util.function.Function;

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
