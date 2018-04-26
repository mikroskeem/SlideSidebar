/*
 * This file is part of project SlideSidebar, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.slidesidebar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import eu.mikroskeem.slidesidebar.gui.SlideSidebarSettingsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * @author Mark Vainomaa
 */
@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "slidesidebar.json")
public final class LiteModSlideSidebar implements LiteMod, Configurable, Tickable {
    private static LiteModSlideSidebar instance;

    private KeyBinding configKeyBinding = new KeyBinding("key.slidesidebar.settings",
            Keyboard.KEY_NONE, "key.categories.slidesidebar");

    private Logger logger = LogManager.getLogger(LiteModSlideSidebar.class);

    private SlideSidebarConfig config;

    @Expose
    @SerializedName("hide_sidebar")
    boolean hideSidebar = false;

    @Expose
    @SerializedName("hide_scores")
    boolean hideScores = false;

    @Expose
    @SerializedName("add_x")
    int addX = 0;

    @Expose
    @SerializedName("add_y")
    int addY = 0;

    public LiteModSlideSidebar() {
        instance = this;
    }

    @Override
    public String getName() {
        return "SlideSidebar";
    }

    @Override
    public String getVersion() {
        return "0.0.1-SNAPSHOT";
    }

    @Override
    public Class<? extends ConfigPanel> getConfigPanelClass() {
        // No configuration panel
        return null;
    }

    @Override
    public void init(@Nonnull File configPath) {
        logger.info("Hello World!");
        config = new SlideSidebarConfig();
        LiteLoader.getInput().registerKeyBinding(configKeyBinding);
    }

    @Override
    public void upgradeSettings(@Nonnull String version, @Nonnull File configPath, @Nonnull File oldConfigPath) {
        // TODO
    }

    @Override
    public void onTick(@Nonnull Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
        // Useful only when player is in game
        if(!inGame)
            return;

        if(config.shouldShowGui()) {
            // If mod should show gui, open it
            minecraft.displayGuiScreen(new SlideSidebarSettingsGui(this));
            config.setShowGui(false);
        } else if(configKeyBinding.isPressed()) {
            // Open GUI on key press
            config.setShowGui(true);
        }
    }

    @Nonnull
    public SlideSidebarConfig getConfig() {
        return config;
    }

    @Nonnull
    public static LiteModSlideSidebar getInstance() {
        return instance;
    }
}