package com.nomi.caysenda.options.model;

import java.util.List;

public class MenuOption {
    Integer id;
    String name;
    String href;
    String icon;
    List<MenuOption> children;

    public MenuOption() {
    }

    public MenuOption(String name, String href) {
        this.name = name;
        this.href = href;
    }

    public MenuOption(String name, String href, List<MenuOption> children) {
        this.name = name;
        this.href = href;
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<MenuOption> getChildren() {
        return children;
    }

    public void setChildren(List<MenuOption> childrent) {
        this.children = childrent;
    }
}
