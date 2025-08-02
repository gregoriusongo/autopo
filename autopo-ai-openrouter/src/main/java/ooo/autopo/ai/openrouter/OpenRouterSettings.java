package ooo.autopo.ai.openrouter;

/*
 * This file is part of the Autopo project
 * Created 02/08/25
 * Copyright 2025 by Sober Lemur S.r.l. (info@soberlemur.com).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import ooo.autopo.model.ui.ApiKeyTextField;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.javafx.FontIcon;
import org.pdfsam.persistence.PreferencesRepository;

import static java.util.Optional.ofNullable;
import static ooo.autopo.i18n.I18nContext.i18n;
import static ooo.autopo.model.ui.Views.helpIcon;

/**
 * @author Andrea Vacondio
 */
public class OpenRouterSettings extends GridPane {

    public OpenRouterSettings(PreferencesRepository repo) {
        this.getStyleClass().addAll("ai-tab", "settings-panel");
        
        add(new Label(i18n().tr("URL:")), 0, 0);
        var urlField = new TextField();
        urlField.setId("openRouterUrlField");
        urlField.setPromptText("https://openrouter.ai/api/v1");
        ofNullable(repo.getString(OpenRouterPersistentProperty.URL.key(), "https://openrouter.ai/api/v1")).ifPresent(urlField::setText);
        urlField.textProperty().subscribe((o, n) -> repo.saveString(OpenRouterPersistentProperty.URL.key(), n));
        setFillWidth(urlField, true);
        add(urlField, 1, 0);
        add(helpIcon(i18n().tr("OpenRouter API endpoint URL")), 2, 0);

        add(new Label(i18n().tr("Model:")), 0, 1);
        var modelField = new TextField();
        modelField.setId("openRouterModelField");
        modelField.setPromptText("anthropic/claude-3.5-sonnet");
        ofNullable(repo.getString(OpenRouterPersistentProperty.MODEL.key(), (String) null)).ifPresent(modelField::setText);
        modelField.textProperty().subscribe((o, n) -> repo.saveString(OpenRouterPersistentProperty.MODEL.key(), n));
        setFillWidth(modelField, true);
        add(modelField, 1, 1);
        add(helpIcon(i18n().tr("AI Model to use (e.g., anthropic/claude-3.5-sonnet, openai/gpt-4)")), 2, 1);

        add(new Label(i18n().tr("API key:")), 0, 2);
        var apiField = new ApiKeyTextField();
        ofNullable(repo.getString(OpenRouterPersistentProperty.API_KEY.key(), (String) null)).ifPresent(apiField::setText);
        apiField.passwordProperty().subscribe((o, n) -> repo.saveString(OpenRouterPersistentProperty.API_KEY.key(), n));
        setFillWidth(apiField, true);
        add(apiField, 1, 2, 2, 1);

        add(new Label(i18n().tr("Temperature:")), 0, 3);
        var temperature = new Spinner<Double>(0.0, 2.0, 0.2, 0.1);
        temperature.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        var temperatureValue = repo.getInt(OpenRouterPersistentProperty.TEMPERATURE.key(), -1);
        if (temperatureValue >= 0) {
            temperature.getValueFactory().setValue(Math.round(temperatureValue / 10.0 * 10) / 10.0);
        }
        temperature.valueProperty().subscribe((o, n) -> repo.saveInt(OpenRouterPersistentProperty.TEMPERATURE.key(), (int) (n * 10)));
        add(temperature, 1, 3);
        add(helpIcon(i18n().tr("Higher values make the output more random, lower values make it more deterministic")), 2, 3);

        Button clearButton = new Button(i18n().tr("Clear"));
        clearButton.setTooltip(new Tooltip(i18n().tr("Clear OpenRouter settings")));
        clearButton.setGraphic(FontIcon.of(FluentUiFilledAL.ERASER_24));
        clearButton.setOnAction(e -> {
            repo.clean();
            urlField.setText("https://openrouter.ai/api/v1");
            modelField.setText("");
            apiField.setText("");
            temperature.getValueFactory().setValue(0.2);
        });
        add(clearButton, 0, 4);

    }
}