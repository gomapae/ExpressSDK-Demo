package com.gomap.tangram_map.model;

import com.wendjia.base.BaseBean;

/**
 * 描述：导航-路线规划类型
 *
 * @author coo_fee.
 * @Time 2020/4/27.
 */
public class NavigationTypeBean extends BaseBean {
    private String name;
    private int iconImgResId;
    private int iconImgResIdSelected;
    private int textColor;
    private int textColorSelected;
    private boolean isSelected;
    private Type type = Type.drive;

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextColorSelected() {
        return textColorSelected;
    }

    public void setTextColorSelected(int textColorSelected) {
        this.textColorSelected = textColorSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconImgResId() {
        return iconImgResId;
    }

    public void setIconImgResId(int iconImgResId) {
        this.iconImgResId = iconImgResId;
    }

    public int getIconImgResIdSelected() {
        return iconImgResIdSelected;
    }

    public void setIconImgResIdSelected(int iconImgResIdSelected) {
        this.iconImgResIdSelected = iconImgResIdSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public NavigationTypeBean() {
    }

    public NavigationTypeBean(String name, int iconImgResId, int iconImgResIdSelected, int textColor, int textColorSelected, boolean isSelected, Type type) {
        this.name = name;
        this.iconImgResId = iconImgResId;
        this.iconImgResIdSelected = iconImgResIdSelected;
        this.textColor = textColor;
        this.textColorSelected = textColorSelected;
        this.isSelected = isSelected;
        this.type = type;
    }

    public enum Type{
        /*驾车*/
        drive("drive",1),
        /*公交*/
        bus("bus",999),
        /*步行*/
        walk("walk",2),
        /*自行车*/
        bike("bike",3),
        /*飞机*/
        plane("plane",998),
        /*打车*/
        taxi("taxi",997);
        private String name;
        private int index;

        public static Type parseIndex(int typeIndex){

           switch (typeIndex){
               case 1:{
                   return drive;
               }
               case 2:{
                   return walk;
               }
               case 3:{
                   return bike;
               }
               case 997:{
                   return taxi;
               }
               case 998:{
                   return plane;
               }
               case 999:{
                   return bus;
               }
               default:
                   return drive;
           }
        }

        Type(String s,int id) {
            this.name = s;
            this.index = id;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

    }
}
