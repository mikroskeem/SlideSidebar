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

import com.mumfrey.liteloader.core.LiteLoader;

/**
 * @author Mark Vainomaa
 */
public final class SlideSidebarConfig {
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private boolean showGui = false;

    private boolean hideSidebar;
    private boolean hideScores;
    private int addX;
    private int addY;

    SlideSidebarConfig() {
        hideSidebar = LiteModSlideSidebar.getInstance().hideSidebar;
        hideScores = LiteModSlideSidebar.getInstance().hideScores;
        addX = LiteModSlideSidebar.getInstance().addX;
        addY = LiteModSlideSidebar.getInstance().addY;
    }

    public boolean shouldHideSidebar() {
        return this.hideSidebar;
    }

    public void setShouldHideSidebar(boolean hideSidebar) {
        this.hideSidebar = hideSidebar;
    }

    public boolean shouldHideScores() {
        return this.hideScores;
    }

    public void setShouldHideScores(boolean hideScores) {
        this.hideScores = hideScores;
    }

    public int getAddX() {
        return this.addX;
    }

    public void setAddX(int addX) {
        this.addX = addX;
    }

    public int getAddY() {
        return this.addY;
    }

    public void setAddY(int addY) {
        this.addY = addY;
    }

    public boolean shouldShowGui() {
        return showGui;
    }

    public void setShowGui(boolean value) {
        this.showGui = value;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void save() {
        LiteModSlideSidebar.getInstance().hideSidebar = hideSidebar;
        LiteModSlideSidebar.getInstance().hideScores = hideScores;
        LiteModSlideSidebar.getInstance().addX = addX;
        LiteModSlideSidebar.getInstance().addY = addY;
        LiteLoader.getInstance().writeConfig(LiteModSlideSidebar.getInstance());
    }
}
