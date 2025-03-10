package com.ecommerce.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.project.model.Category;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<Category>();

    @Override
    public List<Category> getAllCategories(){
        return categories;
    }

    @Override
    public void createCategory(Category category){
        categories.add(category);
    }
}
