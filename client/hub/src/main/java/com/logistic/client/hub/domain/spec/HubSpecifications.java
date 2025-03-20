package com.logistic.client.hub.domain.spec;

import com.logistic.client.hub.domain.model.Hub;
import org.springframework.data.jpa.domain.Specification;

public class HubSpecifications {

  public static Specification<Hub> nameOrLocationContains(String keyword) {
    return (root, query, builder) -> {
      if (keyword == null || keyword.isEmpty()) {
        return builder.conjunction();
      }

      String likePattern = "%" + keyword.toLowerCase() + "%";

      return builder.or(
          builder.like(builder.lower(root.get("name")),likePattern),
          builder.like(builder.lower(root.get("location")),likePattern)
      );
    };
  }

}