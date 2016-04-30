package hr.m2stanic.smartbuilding.web;

import lombok.Getter;

@Getter
public enum MenuOption {

    HOME("Home", "Home"), HOW("How", "How"), CONTACT("Contact", "Contact");

    private String displayName;

    private String displayNamePlural;

    MenuOption(String displayName, String displayNamePlural) {
        this.displayName = displayName;
        this.displayNamePlural = displayNamePlural;
    }
}
