package org.ovirt.engine.ui.webadmin.section.main.view.popup.configure;

import org.gwtbootstrap3.client.ui.Container;
import org.ovirt.engine.core.common.businessentities.Permission;
import org.ovirt.engine.core.common.businessentities.Role;
import org.ovirt.engine.core.common.businessentities.RoleType;
import org.ovirt.engine.ui.common.MainTableHeaderlessResources;
import org.ovirt.engine.ui.common.MainTableResources;
import org.ovirt.engine.ui.common.system.ClientStorage;
import org.ovirt.engine.ui.common.widget.action.PatternflyActionPanel;
import org.ovirt.engine.ui.common.widget.table.SimpleActionTable;
import org.ovirt.engine.ui.common.widget.table.column.AbstractObjectNameColumn;
import org.ovirt.engine.ui.common.widget.table.column.AbstractTextColumn;
import org.ovirt.engine.ui.uicommonweb.UICommand;
import org.ovirt.engine.ui.webadmin.ApplicationConstants;
import org.ovirt.engine.ui.webadmin.gin.AssetProvider;
import org.ovirt.engine.ui.webadmin.uicommon.model.RoleModelProvider;
import org.ovirt.engine.ui.webadmin.uicommon.model.RolePermissionModelProvider;
import org.ovirt.engine.ui.webadmin.widget.action.WebAdminButtonDefinition;
import org.ovirt.engine.ui.webadmin.widget.table.column.IsLockedImageTypeColumn;
import org.ovirt.engine.ui.webadmin.widget.table.column.RoleTypeColumn;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.inject.Inject;

public class RoleView extends Composite {

    @UiField
    SimplePanel rolesTabContent;

    @UiField
    RadioButton allRolesRadioButton;

    @UiField
    RadioButton adminRolesRadioButton;

    @UiField
    RadioButton userRolesRadioButton;

    private PatternflyActionPanel roleActionPanel;
    private FlowPanel roleTablePanel = new FlowPanel();
    private SimpleActionTable<Role> table;

    private PatternflyActionPanel permissionActionPanel;
    private FlowPanel permissionTablePanel = new FlowPanel();
    private SimpleActionTable<Permission> permissionTable;
    private SplitLayoutPanel splitLayoutPanel;

    private final RoleModelProvider roleModelProvider;
    private final RolePermissionModelProvider permissionModelProvider;

    private final EventBus eventBus;
    private final ClientStorage clientStorage;

    private static final ApplicationConstants constants = AssetProvider.getConstants();

    @Inject
    public RoleView(RoleModelProvider roleModelProvider,
            RolePermissionModelProvider permissionModelProvider,
            EventBus eventBus, ClientStorage clientStorage) {
        this.roleModelProvider = roleModelProvider;
        this.permissionModelProvider = permissionModelProvider;
        this.eventBus = eventBus;
        this.clientStorage = clientStorage;

        initWidget(ViewUiBinder.uiBinder.createAndBindUi(this));

        initRolesFilterRadioButtons();
        initSplitLayoutPanel();

        initRoleTable();
        initPermissionTable();
    }

    private void initSplitLayoutPanel() {
        splitLayoutPanel = new SplitLayoutPanel();
        splitLayoutPanel.setHeight("100%"); //$NON-NLS-1$
        splitLayoutPanel.setWidth("100%"); //$NON-NLS-1$
        rolesTabContent.add(splitLayoutPanel);
    }

    public void setSubTabVisibility(boolean visible) {
        splitLayoutPanel.clear();
        if (visible) {
            splitLayoutPanel.addSouth(permissionTablePanel, 150);
        }
        splitLayoutPanel.add(roleTablePanel);
    }

    private void initRolesFilterRadioButtons() {
        allRolesRadioButton.setValue(true);

        allRolesRadioButton.addValueChangeHandler(event -> {
            if (event.getValue()) {
                roleModelProvider.getModel().setItemsFilter(null);
                roleModelProvider.getModel().forceRefresh();
            }
        });

        adminRolesRadioButton.addValueChangeHandler(event -> {
            if (event.getValue()) {
                roleModelProvider.getModel().setItemsFilter(RoleType.ADMIN);
                roleModelProvider.getModel().forceRefresh();
            }
        });

        userRolesRadioButton.addValueChangeHandler(event -> {
            if (event.getValue()) {
                roleModelProvider.getModel().setItemsFilter(RoleType.USER);
                roleModelProvider.getModel().forceRefresh();
            }
        });

    }

    interface ViewUiBinder extends UiBinder<Container, RoleView> {
        ViewUiBinder uiBinder = GWT.create(ViewUiBinder.class);
    }

    private void initRoleTable() {
        roleActionPanel = new PatternflyActionPanel();
        this.table = new SimpleActionTable<>(roleModelProvider,
                getTableHeaderlessResources(), getTableResources(), eventBus, clientStorage);
        roleTablePanel.add(roleActionPanel);
        roleTablePanel.add(table);

        this.table.enableColumnResizing();
        AbstractTextColumn<Role> nameColumn = new AbstractTextColumn<Role>() {
            @Override
            public String getValue(Role object) {
                return object.getName();
            }
        };
        nameColumn.makeSortable();

        table.addColumn(new IsLockedImageTypeColumn(), constants.empty(), "25px"); //$NON-NLS-1$

        table.addColumn(new RoleTypeColumn(), constants.empty(), "25px"); //$NON-NLS-1$

        table.addColumn(nameColumn, constants.nameRole(), "175px"); //$NON-NLS-1$

        AbstractTextColumn<Role> descColumn = new AbstractTextColumn<Role>() {
            @Override
            public String getValue(Role object) {
                return object.getDescription();
            }
        };
        descColumn.makeSortable();
        table.addColumn(descColumn, constants.descriptionRole(), "575px"); //$NON-NLS-1$

        roleActionPanel.addButtonToActionGroup(
        table.addActionButton(new WebAdminButtonDefinition<Role>(constants.newRole()) {
            @Override
            protected UICommand resolveCommand() {
                return roleModelProvider.getModel().getNewCommand();
            }
        }));

        roleActionPanel.addButtonToActionGroup(
        table.addActionButton(new WebAdminButtonDefinition<Role>(constants.editRole()) {
            @Override
            protected UICommand resolveCommand() {
                return roleModelProvider.getModel().getEditCommand();
            }
        }));

        roleActionPanel.addButtonToActionGroup(
        table.addActionButton(new WebAdminButtonDefinition<Role>(constants.copyRole()) {
            @Override
            protected UICommand resolveCommand() {
                return roleModelProvider.getModel().getCloneCommand();
            }
        }));

        roleActionPanel.addButtonToActionGroup(
        table.addActionButton(new WebAdminButtonDefinition<Role>(constants.removeRole()) {
            @Override
            protected UICommand resolveCommand() {
                return roleModelProvider.getModel().getRemoveCommand();
            }
        }));

        splitLayoutPanel.add(roleTablePanel);

        table.getSelectionModel().addSelectionChangeHandler(event -> {
            roleModelProvider.setSelectedItems(table.getSelectionModel().getSelectedList());
            if (table.getSelectionModel().getSelectedList().size() > 0) {
                setSubTabVisibility(true);
            } else {
                setSubTabVisibility(false);
            }
        });

    }

    private void initPermissionTable() {
        permissionActionPanel = new PatternflyActionPanel();
        permissionTable = new SimpleActionTable<>(permissionModelProvider,
                getTableHeaderlessResources(), getTableResources(), eventBus, clientStorage);
        permissionTablePanel.add(permissionActionPanel);
        permissionTablePanel.add(permissionTable);

        permissionTable.enableColumnResizing();

        AbstractTextColumn<Permission> userColumn = new AbstractTextColumn<Permission>() {
            @Override
            public String getValue(Permission object) {
                return object.getOwnerName();
            }
        };
        userColumn.makeSortable();
        permissionTable.addColumn(userColumn, constants.userPermission());

        AbstractTextColumn<Permission> permissionColumn = new AbstractObjectNameColumn<Permission>() {
            @Override
            protected Object[] getRawValue(Permission object) {
                return new Object[] { object.getObjectType(), object.getObjectName() };
            }
        };
        permissionColumn.makeSortable();
        permissionTable.addColumn(permissionColumn, constants.objectPermission());

        permissionActionPanel.addButtonToActionGroup(
        permissionTable.addActionButton(new WebAdminButtonDefinition<Permission>(constants.removePermission()) {
            @Override
            protected UICommand resolveCommand() {
                return permissionModelProvider.getModel().getRemoveCommand();
            }
        }));

        permissionTable.getSelectionModel().addSelectionChangeHandler(event ->
                permissionModelProvider.setSelectedItems(permissionTable.getSelectionModel().getSelectedList()));
    }

    protected Resources getTableHeaderlessResources() {
        return (Resources) GWT.create(MainTableHeaderlessResources.class);
    }

    protected Resources getTableResources() {
        return (Resources) GWT.create(MainTableResources.class);
    }

}
