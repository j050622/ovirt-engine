package org.ovirt.engine.ui.webadmin.section.main.presenter;

import java.util.ArrayList;
import java.util.List;

import org.ovirt.engine.ui.common.uicommon.model.MainModelProvider;
import org.ovirt.engine.ui.uicommonweb.models.SearchableListModel;
import org.ovirt.engine.ui.uicommonweb.models.bookmarks.BookmarkListModel;
import org.ovirt.engine.ui.uicommonweb.models.tags.TagModel;
import org.ovirt.engine.ui.webadmin.uicommon.model.BookmarkModelProvider;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class SearchPanelPresenterWidget<T, M extends SearchableListModel> extends PresenterWidget<SearchPanelPresenterWidget.ViewDef> {

    public interface ViewDef<M extends SearchableListModel> extends View {

        String getSearchString();

        void setSearchString(String searchString);

        void setSearchStringPrefix(String searchStringPrefix);

        void setHasSelectedTags(boolean hasSelectedTags);

        HasClickHandlers getBookmarkButton();

        HasClickHandlers getClearButton();

        HasClickHandlers getSearchButton();

        HasKeyDownHandlers getSearchInputHandlers();

        void hideSuggestionBox();

        void enableSearchBar(boolean status);

        void setModel(SearchableListModel model);

    }

    private final M model;

    private final BookmarkModelProvider bookmarkModelProvider;

    @Inject
    public SearchPanelPresenterWidget(EventBus eventBus, ViewDef<M> view, MainModelProvider<T, M> modelProvider,
            BookmarkModelProvider bookmarkModelProvider) {
        super(eventBus, view);
        this.model = modelProvider.getModel();
        this.bookmarkModelProvider = bookmarkModelProvider;
        addModelListeners();
        updateViewSearchStringPrefix();
    }

    void addModelListeners() {
        model.getPropertyChangedEvent().addListener((event, sender, args) -> {
            // Update search string when 'SearchString' property changes
            if ("SearchString".equals(args.propertyName)) { //$NON-NLS-1$
                updateViewSearchString();
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getBookmarkButton().addClickHandler(event -> {
            BookmarkListModel bookmarkListModel = bookmarkModelProvider.getModel();
            bookmarkListModel.setSearchString(getView().getSearchString());
            bookmarkListModel.getNewCommand().execute();
        }));

        registerHandler(getView().getClearButton().addClickHandler(event -> {
            model.setSearchString(""); //$NON-NLS-1$
            updateViewSearchString();
            updateModelSearchString();
        }));

        registerHandler(getView().getSearchButton().addClickHandler(event -> updateModelSearchString()));

        registerHandler(getView().getSearchInputHandlers().addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                updateModelSearchString();
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                getView().hideSuggestionBox();
            }
        }));
    }

    void updateModelSearchString() {
        model.setSearchString(getView().getSearchString());
        model.search();
    }

    void updateViewSearchString() {
        getView().setSearchStringPrefix(model.getDefaultSearchString());
        getView().setSearchString(model.getSearchString());
    }

    private void updateViewSearchStringPrefix() {
        getView().setSearchStringPrefix(model.getDefaultSearchString());
    }

    public void setTags(List<TagModel> tags) {
        List<String> tagString = new ArrayList<>();
        for (TagModel tagModel: tags) {
            tagString.add(tagModel.getName().getEntity());
        }
        model.setTagStrings(tagString);
        if (isVisible()) {
            model.executeCommand(model.getSearchCommand());
        }
    }

}
