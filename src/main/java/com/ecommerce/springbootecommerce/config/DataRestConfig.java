package com.ecommerce.springbootecommerce.config;

import com.ecommerce.springbootecommerce.entity.Product;
import com.ecommerce.springbootecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public DataRestConfig(EntityManager entityManager1){
        entityManager= entityManager1;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] forbiddenActions = {HttpMethod.DELETE, HttpMethod.PUT , HttpMethod.POST};
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(forbiddenActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(forbiddenActions));

        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(forbiddenActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(forbiddenActions));

        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config){

        Set<javax.persistence.metamodel.EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        List<Class> entityClasses = new ArrayList<>();

        for(javax.persistence.metamodel.EntityType  tempEntityType: entities){
            entityClasses.add(tempEntityType.getJavaType());
        }

        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}

