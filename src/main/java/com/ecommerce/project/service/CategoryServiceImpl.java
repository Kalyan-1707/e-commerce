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
        category.setCategoryId(categories.size() + 1L);
        categories.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId){
        Category category = categories.stream().filter(cat -> cat.getCategoryId().equals(categoryId)).findFirst().orElse(null);
        if(category == null)
            return "category not found";
        categories.remove(category);
        return "Category of id " + categoryId + " delete successfully";
    }
}
