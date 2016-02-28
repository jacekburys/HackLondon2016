package com.grouptravel.grouptravel;

/**
 * Created by jacek on 28/02/16.
 */
public class Group {

    private String id;
    private String name;
    private boolean isSelected;

    public Group(String name, String id) {
        this.id = id;
        this.name = name;
        isSelected = false;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

}
