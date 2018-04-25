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

package eu.mikroskeem.slidesidebar.mixins;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import eu.mikroskeem.slidesidebar.LiteModSlideSidebar;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Scoreboard renderer mixin
 *
 * @author Mark Vainomaa
 */
@Mixin(value = GuiIngame.class)
public abstract class MixinGuiIngame {
    @Shadow public abstract FontRenderer getFontRenderer();

    /**
     * @reason Rewrite whole method to redirect scoreboard logic
     * @author Mark Vainomaa, Mojang (original code)
     */
    @Overwrite
    private void renderScoreboard(ScoreObjective objective, ScaledResolution scaledRes) {
        LiteModSlideSidebar mod = LiteModSlideSidebar.getInstance();

        if(!mod.getConfig().shouldHideSidebar()) {
            Scoreboard scoreboard = objective.getScoreboard();
            List<Score> scores = scoreboard.getSortedScores(objective).stream()
                    .filter(p -> p.getPlayerName() != null && !p.getPlayerName().startsWith("#"))
                    .collect(Collectors.toList());

            // Limit to 15 items max
            if(scores.size() > 15)
                scores = Lists.newArrayList(Iterables.skip(new ArrayList<>(scores), scores.size() - 15));

            // Scoreboard dimensions
            int scoreboardHeight = scores.size() * this.getFontRenderer().FONT_HEIGHT;
            int scoreboardWidth = this.getFontRenderer().getStringWidth(objective.getDisplayName());

            // Figure out scoreboard max width by iterating over all objectives
            for(Score score : scores) {
                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                String formatted = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());

                // Add points to the end as well if configured so
                if(!mod.getConfig().shouldHideScores())
                    formatted = formatted + ": " + EnumChatFormatting.RED + score.getScorePoints();

                // Get max width
                scoreboardWidth = Math.max(scoreboardWidth, this.getFontRenderer().getStringWidth(formatted));
            }

            int j1 = scaledRes.getScaledHeight() / 2 + scoreboardHeight / 3;
            int rightPadding = 3;
            int l1 = scaledRes.getScaledWidth() - scoreboardWidth - rightPadding;

            int addX = mod.getConfig().getAddX();
            int addY = mod.getConfig().getAddY();

            int index = 0;
            for (Score score : scores) {
                index++;

                ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                String formattedName = ScorePlayerTeam.formatPlayerName(team, score.getPlayerName());
                String points = EnumChatFormatting.RED + "" + score.getScorePoints();

                int scoreX = scaledRes.getScaledWidth() - rightPadding + 2;
                int scoreY = j1 - index * this.getFontRenderer().FONT_HEIGHT;

                // Draw scoreboard background
                GuiIngame.drawRect(addX + (l1 - 2), addY + scoreY, addX + scoreX, addY + (scoreY + this.getFontRenderer().FONT_HEIGHT), 1342177280);

                // Draw player name
                this.getFontRenderer().drawString(formattedName, addX + l1, addY + scoreY, 553648127);

                // Draw points if configured so
                if(!mod.getConfig().shouldHideScores())
                    this.getFontRenderer().drawString(points, addX + (scoreX - this.getFontRenderer().getStringWidth(points)), addY + scoreY, 553648127);

                // If it's not the last scoreboard line
                if (index != scores.size())
                    continue;

                // Set clickable area coordinates
                mod.getConfig().setMinX(l1 - 2);
                mod.getConfig().setMinY(scoreY - this.getFontRenderer().FONT_HEIGHT - 1);
                mod.getConfig().setMaxX(scoreX);
                mod.getConfig().setMaxY(j1);

                // Draw objective name
                String objectiveName = objective.getDisplayName();
                GuiIngame.drawRect(addX + (l1 - 2), addY + (scoreY - this.getFontRenderer().FONT_HEIGHT - 1), addX + scoreX, addY + (scoreY - 1), 1610612736);
                GuiIngame.drawRect(addX + (l1 - 2), addY + (scoreY - 1), addX + scoreX, addY + scoreY, 1342177280);
                this.getFontRenderer().drawString(objectiveName, addX + (l1 + scoreboardWidth / 2 - this.getFontRenderer().getStringWidth(objectiveName) / 2), addY + (scoreY - this.getFontRenderer().FONT_HEIGHT), 553648127);
            }
        }
    }
}
