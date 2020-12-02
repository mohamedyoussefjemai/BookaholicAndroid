package com.example.piandroid;

public class MainCategory {
    Integer categoryLogo;
    String categoryName ;

    public  MainCategory(Integer categoryLogo,String categoryName){
        this.categoryLogo = categoryLogo;
        this.categoryName = categoryName;
    }
    public Integer getCategoryLogo(){
        return categoryLogo;
    }
    public String getCategoryName(){
        return categoryName;
    }

}