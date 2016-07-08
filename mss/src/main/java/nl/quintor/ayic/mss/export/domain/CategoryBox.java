package nl.quintor.ayic.mss.export.domain;

import nl.quintor.ayic.mss.category.domain.Category;

import java.util.ArrayList;

public class CategoryBox {
    private boolean includeRelatedCategories;
    private ArrayList<Category> categories;

    public boolean isIncludeRelatedCategories() {
        return includeRelatedCategories;
    }

    public void setIncludeRelatedCategories(boolean includeRelatedCategories) {
        this.includeRelatedCategories = includeRelatedCategories;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public void addCategories(ArrayList<Category> categories) {
        this.categories.addAll(categories);
    }
}
