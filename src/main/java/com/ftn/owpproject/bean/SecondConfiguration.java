package com.ftn.owpproject.bean;

import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ftn.owpproject.model.TravelCategory;
import com.ftn.owpproject.service.TravelCategoryService;


//import com.ftn.owpproject.service.TravelCategoryService;

@Configuration
public class SecondConfiguration {

	@Bean(name= {"memorijaAplikacije"}, 
			initMethod="init", destroyMethod="destroy")
	public ApplicationMemory getApplicationMemory() {
		return new ApplicationMemory();
	}
	
	@Bean 
	public TravelCategoryService travelCategoryService() {
		return new TravelCategoryService() {
			
			@Override
			public TravelCategory update(TravelCategory travelCategory) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TravelCategory save(TravelCategory travelCategory) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TravelCategory findOne(Long id) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<TravelCategory> findAll() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public TravelCategory delete(Long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Long getIdByName(String categoryName) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
	
	public class ApplicationMemory extends HashMap<Object, Object> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			return "ApplicationMemory"+this.hashCode();
		}
		
		public void init() {
			//inicijalizacija
			System.out.println("init method called");
		}
		
		public void destroy() {
			//brisanje
			System.out.println("destroy method called");
		}
	}
}
