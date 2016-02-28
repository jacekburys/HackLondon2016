
package com.grouptravel.grouptravel;

/**
 * Created by jacek on 27/02/16.
 */
public class Friend {

    private String name;
    private String id;
    private boolean isSelected;

    public Friend(String name, String id) {
        this.name = name;
        this.id = id;
        this.isSelected = false;
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