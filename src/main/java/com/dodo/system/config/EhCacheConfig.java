package com.dodo.system.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@EnableCaching
@Configuration
public class EhCacheConfig{
	
		@Bean
		public CacheManager cacheManager() {
			return new EhCacheCacheManager(ehCacheCacheManager().getObject());
		}
		
		@Bean
	    public EhCacheManagerFactoryBean ehCacheCacheManager() {
	        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
	        bean.setConfigLocation(new ClassPathResource("ehcache.xml"));
	        bean.setShared(true);//cacheManager 싱글톤 여부를 지정하는 값. cacheManager 인스턴스가 없으면 생성하도록 true 값
	        return bean;
	    }

}