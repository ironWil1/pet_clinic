package com.vet24.vaadin.components;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@StyleSheet("frontend://styles/style.css")
public class DivLayoutView extends Div {

    public DivLayoutView() {
        // Instantiate layouts
        /*HorizontalLayout header = new HorizontalLayout();
        VerticalLayout navBar = new VerticalLayout();
        VerticalLayout content = new VerticalLayout();
        Div center = new Div();
        HorizontalLayout footer = new HorizontalLayout();

        // Configure layouts
        setSizeFull();
        header.setPadding(true);
        footer.setPadding(true);
        addClassName("main-view");
        header.addClassName("header");
        navBar.addClassName("navbar");
        center.addClassName("center");
        content.addClassName("content");
        footer.addClassName("footer");

        // Compose layout
        center.add(navBar, content);
        add(header, center, footer);
*/
        Div wrapperPage = new Div();
        Div wrapperContent = new Div();
        wrapperPage.addClassName("wrapper_page");
        wrapperContent.addClassName("wrapper_content");

    }
}
