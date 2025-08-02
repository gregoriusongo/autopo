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

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import javafx.scene.layout.Pane;
import ooo.autopo.model.ai.AIModelDescriptor;
import org.pdfsam.persistence.PreferencesRepository;

import java.util.Set;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Andrea Vacondio
 */
public class OpenRouterModelDescriptor implements AIModelDescriptor {
    static final String MODEL_ID = "OPENROUTER";
    private final PreferencesRepository repo = new PreferencesRepository("/ooo/autopo/ai/settings/openrouter");

    @Override
    public String id() {
        return MODEL_ID;
    }

    @Override
    public String name() {
        return "OpenRouter";
    }

    @Override
    public Pane settingsPane() {
        return new OpenRouterSettings(repo);
    }

    @Override
    public ChatModel translationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OpenRouterPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            
            var baseUrl = repo.getString(OpenRouterPersistentProperty.URL.key(), "https://openrouter.ai/api/v1");
            var model = repo.getString(OpenRouterPersistentProperty.MODEL.key(), "anthropic/claude-3.5-sonnet");
            var apiKey = repo.getString(OpenRouterPersistentProperty.API_KEY.key(), "");
            
            return OpenAiChatModel.builder()
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .modelName(model)
                    .temperature(temperature)
                    .logRequests(true)
                    .build();
        }
        return null;
    }

    @Override
    public ChatModel validationModel() {
        if (isUsable()) {
            var temperature = 0.2d;
            var temperatureIntValue = repo.getInt(OpenRouterPersistentProperty.TEMPERATURE.key(), -1);
            if (temperatureIntValue >= 0) {
                temperature = Math.round(temperatureIntValue / 10.0 * 10) / 10.0;
            }
            
            var baseUrl = repo.getString(OpenRouterPersistentProperty.URL.key(), "https://openrouter.ai/api/v1");
            var model = repo.getString(OpenRouterPersistentProperty.MODEL.key(), "anthropic/claude-3.5-sonnet");
            var apiKey = repo.getString(OpenRouterPersistentProperty.API_KEY.key(), "");
            
            return OpenAiChatModel.builder()
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .modelName(model)
                    .temperature(temperature)
                    .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                    .strictJsonSchema(true)
                    .logRequests(true)
                    .build();
        }
        return null;
    }

    @Override
    public boolean isUsable() {
        return isNotBlank(repo.getString(OpenRouterPersistentProperty.API_KEY.key(), "")) &&
               isNotBlank(repo.getString(OpenRouterPersistentProperty.MODEL.key(), ""));
    }
}