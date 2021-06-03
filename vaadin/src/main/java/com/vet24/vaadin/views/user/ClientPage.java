package com.vet24.vaadin.views.user;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vet24.vaadin.models.user.Client;
import com.vet24.vaadin.template.user.ClientTemplateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Route("/client")
@StyleSheet("./style.css")
@PageTitle("Client Page")
public class ClientPage extends VerticalLayout {

    private static final String GET_URL_CLIENT = "http://localhost:8080/api/client";

    private HorizontalLayout header = new HorizontalLayout();
    private VerticalLayout navBar = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();
    private HorizontalLayout body = new HorizontalLayout();
    private HorizontalLayout footer = new HorizontalLayout();

    private Grid<Client> clientGrid = new Grid<>(Client.class);

    private Button save = new Button("Add new client", VaadinIcon.PLUS.create(), e -> Notification.show("client save will be here"));
    private Button cancel = new Button("Cancel", VaadinIcon.CLOSE_SMALL.create(), e -> Notification.show("cancel"));
    private Button delete = new Button("Delete",VaadinIcon.TRASH.create(), e -> Notification.show("client delete will be here"));
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);

    public ClientPage(@Autowired ClientTemplateService clientTemplateService) {

        // div
        Div divWrapperPage = new Div();
        divWrapperPage.addClassName("wrapper_page");
        Div divWrapperContent = new Div();
        divWrapperContent.addClassName("wrapper_content");
        Div divLogoImg = new Div();
        divLogoImg.addClassName("logo_img");
        Div divHeaderTitle = new Div();
        divHeaderTitle.addClassName("header_title");
        divHeaderTitle.setWidth("100%");
        Div divGridClients = new Div();
        divGridClients.addClassName("grid_clients");
        Div divButtonsForGridClients = new Div();
        divButtonsForGridClients.addClassName("buttons_for_grid_clients");

        // span
        Span textHeader = new Span("Pet clinic");
        textHeader.addClassName("text_header");
        Span textNavigation = new Span("Navigation");
        textHeader.addClassName("text_navigation");
        Span textFooter = new Span("Footer");
        textHeader.addClassName("text_footer");

        // Configure layouts
        setSizeFull();
        setPadding(false);
        setSpacing(false);

        // header
        header.addClassName("header_color");
        header.setWidth("100%");
        header.setPadding(true);
        Image logo = new Image("./img/template-cover-cat.png", "Logo pet-clinic");
        logo.setWidth(75, Unit.PIXELS);
        logo.setHeight(75, Unit.PIXELS);
        divLogoImg.add(logo);
        header.add(divLogoImg);
        divHeaderTitle.add(textHeader);
        header.add(divHeaderTitle);

        // body
        body.setWidth("100%");

        // Navigation
        navBar.addClassName("navigation_color");
        navBar.setWidth("120px");
        navBar.add(textNavigation);

        // content
        content.setWidth("100%");
        Client client = clientTemplateService.getEntity(GET_URL_CLIENT, Client.class);
        clientGrid.addClassName("grid_client");
        divGridClients.add(clientGrid);
        divGridClients.setWidth("100%");
        clientGrid.setItems(client);
        clientGrid.getColumnByKey("firstname").setAutoWidth(true);
        clientGrid.getColumnByKey("lastname").setAutoWidth(true);
        clientGrid.getColumnByKey("email").setAutoWidth(true);
        clientGrid.getColumnByKey("pets").setAutoWidth(true);
        clientGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        clientGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        MultiSelect<Grid<Client>, Client> multiSelect =
                clientGrid.asMultiSelect();
        multiSelect.addValueChangeListener(e -> {
            Notification.show(e.getValue() + " is selected");
            Set<Client> selectedPersons = e.getValue();
        });

        actions.addClassName("buttons");
        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");
        divButtonsForGridClients.add(actions);
        divButtonsForGridClients.setWidth("100%");
        content.add(divGridClients, divButtonsForGridClients);

        // footer
        footer.addClassName("footer_color");
        footer.setWidth("100%");
        footer.setPadding(true);
        footer.add(textFooter);

        // Compose layout
        body.add(navBar, content);
        divWrapperContent.add(header, body, footer);
        divWrapperPage.add(divWrapperContent);
        add(divWrapperPage);
    }
}
