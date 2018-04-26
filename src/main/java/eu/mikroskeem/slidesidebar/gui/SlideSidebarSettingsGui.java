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

package eu.mikroskeem.slidesidebar.gui;

import eu.mikroskeem.slidesidebar.LiteModSlideSidebar;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

/**
 * @author Mark Vainomaa
 */
public final class SlideSidebarSettingsGui extends GuiScreen {
    private LiteModSlideSidebar mod;

    private boolean dragging;
    private int lastMouseX;
    private int lastMouseY;
    private int lastAddX;
    private int lastAddY;
    private int clickCounter;
    private long lastClick;

    public SlideSidebarSettingsGui(LiteModSlideSidebar mod) {
        this.mod = mod;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 6 + 144,
                I18n.format("slidesidebar.gui.settings.displayScoreboard", !this.mod.getConfig().shouldHideSidebar() ? I18n.format("gui.yes") : I18n.format("gui.no"))));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 120,
                I18n.format("slidesidebar.gui.settings.displayScores", !this.mod.getConfig().shouldHideScores() ? I18n.format("gui.yes") : I18n.format("gui.no"))));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 6 + 96,
                I18n.format("slidesidebar.gui.settings.resetPosition")));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if(!guiButton.enabled)
            return;

        switch(guiButton.id) {
            case 1:
                // Display
                this.mod.getConfig().setShouldHideSidebar(!this.mod.getConfig().shouldHideSidebar());
                guiButton.displayString = I18n.format("slidesidebar.gui.settings.displayScoreboard",
                        !this.mod.getConfig().shouldHideSidebar() ? I18n.format("gui.yes") : I18n.format("gui.no"));
                break;
            case 2:
                // Scores
                this.mod.getConfig().setShouldHideScores(!this.mod.getConfig().shouldHideScores());
                guiButton.displayString = I18n.format("slidesidebar.gui.settings.displayScores",
                        !this.mod.getConfig().shouldHideScores() ? I18n.format("gui.yes") : I18n.format("gui.no"));
                break;
            case 3:
                // Reset
                this.mod.getConfig().setAddX(0);
                this.mod.getConfig().setAddY(0);
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            int actualMinX = this.mod.getConfig().getAddX() + mod.getConfig().getMinX();
            int actualMinY = this.mod.getConfig().getAddY() + mod.getConfig().getMinY();
            int actualMaxX = this.mod.getConfig().getAddX() + mod.getConfig().getMaxX();
            int actualMaxY = this.mod.getConfig().getAddY() + mod.getConfig().getMaxY();

            if(!this.dragging && mouseX > actualMinX && mouseX < actualMaxX && mouseY > actualMinY && mouseY < actualMaxY) {
                if (System.currentTimeMillis() - this.lastClick < 300L) {
                    ++this.clickCounter;
                    if (this.clickCounter > 1)
                        this.clickCounter = 0;
                } else {
                    this.clickCounter = 0;
                }

                this.lastClick = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        int actualMinX = this.mod.getConfig().getAddX() + mod.getConfig().getMinX();
        int actualMinY = this.mod.getConfig().getAddY() + mod.getConfig().getMinY();
        int actualMaxX = this.mod.getConfig().getAddX() + mod.getConfig().getMaxX();
        int actualMaxY = this.mod.getConfig().getAddY() + mod.getConfig().getMaxY();

        if(!this.dragging && mouseX > actualMinX && mouseX < actualMaxX && mouseY > actualMinY && mouseY < actualMaxY) {
            this.dragging = true;
            this.lastMouseX = mouseX;
            this.lastMouseY = mouseY;
            this.lastAddX = this.mod.getConfig().getAddX();
            this.lastAddY = this.mod.getConfig().getAddY();
        }

        if(this.dragging) {
            this.mod.getConfig().setAddX(this.lastAddX + (mouseX - this.lastMouseX));
            this.mod.getConfig().setAddY(this.lastAddY + (mouseY - this.lastMouseY));
        }
    }



    @Override
    protected void mouseReleased(int mouseX, int mouseY, int which) {
        super.mouseReleased(mouseX, mouseY, which);

        if(which != -1) {
            this.dragging = false;
            this.lastMouseX = 0;
            this.lastMouseY = 0;
        }
    }

    @Override
    public void onGuiClosed() {
        mod.getConfig().save();
    }
}
